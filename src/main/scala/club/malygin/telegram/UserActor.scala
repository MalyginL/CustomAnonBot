package club.malygin.telegram

import java.util.UUID

import io.circe.parser._
import io.circe.syntax._
import akka.actor.{Actor, ReceiveTimeout}
import club.malygin.data.cache.UserPairCache
import club.malygin.data.dataBase.cassandra.{CassandraDatabase, ChatLogsModel}
import club.malygin.data.dataBase.pg.dao._
import club.malygin.data.dataBase.pg.model.{CallbackMessage, QuizQuestions, QuizResults, Users}
import club.malygin.web.model._
import com.typesafe.scalalogging.LazyLogging
import javax.inject.Inject
import org.joda.time.DateTime

import scala.concurrent.duration._
import scala.util.{Failure, Success}

class UserActor @Inject()(
    cache: UserPairCache[Long, Long],
    usersDao: UsersDao,
    quizResultsDao: QuizResultsDao,
    quizQuestionDao: QuizQuestionDao
) extends Actor
    with Commands
    with LazyLogging {

  import context._

  context.setReceiveTimeout(5.minutes)

  override def postRestart(reason: Throwable): Unit = {
    super.postRestart(reason)
    logger.info(s"restarting actor $self")
  }

  private val actorName = self.path.name
  private val userId    = actorName.toInt
  context.parent ! ActorState("?", actorName)

  def init: PartialFunction[Any, Unit] = {
    case message: Message =>
      message.text match {
        case Some(text) if message.entities.isDefined && text == "/start" =>
          sendMessage(greeting, userId)
          message.from match {
            case Some(from) =>
              usersDao.saveOrUpdate(
                Users(
                  from.id,
                  from.first_name,
                  from.last_name,
                  from.username
                )
              )
              context.parent ! ActorState("registerStart", actorName)
              become(registerStart)
            case None => logger.warn("entering without address in init")
          }
        case _ => sendMessage("Use /start command.", userId)
      }
    case callback: CallbackQuery => invalidateCallback(callback)
    case ReceiveTimeout =>
      context.stop(self)

  }

  def registerStart: PartialFunction[Any, Unit] = {
    case message: Message =>
      message.text match {
        case Some(text) if message.entities.isDefined && text == "/register" =>
          sendMessage("After passing poll use /search to start chatting.", userId)
          quizQuestionDao.getActive.onComplete { e =>
            e.getOrElse(Seq.empty[QuizQuestions])
              .foreach(question => {
                sendKeyboard(
                  userId.toString,
                  question.text,
                  Array(
                    InlineKeyboardButton(
                      question.firstOption,
                      Some(
                        CallbackMessage(question.quizIdd.toString, Some(true)).asJson.noSpaces
                      )
                    ),
                    InlineKeyboardButton(
                      question.secondOption,
                      Some(
                        CallbackMessage(question.quizIdd.toString, Some(false)).asJson.noSpaces
                      )
                    )
                  )
                )
              })
          }

          context.parent ! ActorState("awaitingRegister", actorName)
          become(awaitingRegister)
        case _ => sendMessage("Please start poll using /register command.", userId)
      }
    case callback: CallbackQuery => invalidateCallback(callback)
    case ReceiveTimeout =>
      context.stop(self)
  }

  def awaitingRegister: PartialFunction[Any, Unit] = {
    case callback: CallbackQuery =>
      callback.message match {
        case Some(message) =>
          callback.data match {
            case Some(data) =>
              decode[CallbackMessage](data) match {
                case Right(CallbackMessage(entityId, Some(entityAns))) =>
                  quizResultsDao.saveOrUpdate(
                    QuizResults(
                      UUID.randomUUID,
                      callback.from.id,
                      UUID.fromString(entityId),
                      entityAns
                    )
                  )
                  answerCallbackQueryAndRemove(
                    callback.id,
                    message.chat.id.toString,
                    message.message_id.intValue,
                    callback.from.id
                  )
                case Left(_) => logger.warn("decoding callback error in awaitingRegister")
                case _       => invalidateCallback(callback)
              }
            case None => logger.warn("no result in callback in awaitingRegister")
          }
        case None => logger.warn("callback without message in awaitingRegister, possible API change")
      }
    case message: Message =>
      message.text match {
        case Some(text) if message.entities.isDefined && text == "/search" =>
          quizQuestionDao
            .getActiveWithAnswer(userId.toLong)
            .map(
              _.map(
                q =>
                  InlineKeyboardButton(
                    q.text,
                    Some(CallbackMessage(q.quizIdd.toString).asJson.noSpaces)
                  )
              )
            )
            .map(k => sendKeyboard(userId.toString, "Choose topic", k.toArray[InlineKeyboardButton]))
          context.parent ! ActorState("awaitingTopic", actorName)
          become(awaitingTopic)
        case Some(text) if message.entities.isDefined && text == "/register" =>
          sendMessage("After passing poll use /search to start chatting.", userId)
          quizQuestionDao.getActive.onComplete { e =>
            e.getOrElse(Seq.empty[QuizQuestions])
              .foreach(question => {
                sendKeyboard(
                  userId.toString,
                  question.text,
                  Array(
                    InlineKeyboardButton(
                      question.firstOption,
                      Some(CallbackMessage(question.quizIdd.toString, Some(true)).asJson.noSpaces)
                    ),
                    InlineKeyboardButton(
                      question.secondOption,
                      Some(
                        CallbackMessage(question.quizIdd.toString, Some(false)).asJson.noSpaces
                      )
                    )
                  )
                )
              })
          }
        case _ =>
          sendMessage("Use\n/search to start chatting.\n/register again to answer question in a different way.", userId)
      }

    case ReceiveTimeout =>
      context.stop(self)
  }

  def awaitingTopic: PartialFunction[Any, Unit] = {
    case state: ActorState if state.value == "chatting" =>
      become(chatting)
    case callback: CallbackQuery =>
      callback.message match {
        case Some(message) =>
          callback.data match {
            case Some(data) =>
              decode[CallbackMessage](data) match {
                case Right(entity) if entity.ans.isEmpty =>
                  answerCallbackQueryAndRemove(
                    callback.id,
                    message.chat.id.toString,
                    message.message_id.intValue,
                    callback.from.id
                  )
                  usersDao.findAndPairUsersTransactionally(callback.from.id, UUID.fromString(entity.id)).andThen {
                    case Success(res) =>
                      cache.addToCache(callback.from.id, res)
                      cache.addToCache(res, callback.from.id)
                      sendMessage(s"Chat connected.\nHave fun!", callback.from.id)
                      sendMessage(s"Chat connected.\nHave fun!", res.toInt)
                      context.parent ! ActorState("chatting", actorName)
                      context.parent ! ActorState("chatting", res.toString)
                    case Failure(_) =>
                      usersDao
                        .updateStatusToActive(callback.from.id, UUID.fromString(entity.id))
                        .andThen {
                          case Success(_) =>
                            sendMessage("Waiting...\n/leave to stop searching.", callback.from.id)
                            context.parent ! ActorState("searching", actorName)
                            become(searching)
                        }
                  }
                case Left(_) => logger.warn("decoding callback error in awaitingTopic")
                case _       => invalidateCallback(callback)
              }
            case None => logger.warn("no result in callback in awaitingTopic")
          }
        case None => logger.warn("callback without message in awaitingTopic, possible API change")
      }
    case message: Message =>
      message.text match {
        case Some(text) if message.entities.isDefined && text == "/register" =>
          sendMessage("After passing poll use /search to start chatting.", userId)
          quizQuestionDao.getActive.onComplete { e =>
            e.getOrElse(Seq.empty[QuizQuestions])
              .foreach(question => {
                sendKeyboard(
                  userId.toString,
                  question.text,
                  Array(
                    InlineKeyboardButton(
                      question.firstOption,
                      Some(CallbackMessage(question.quizIdd.toString, Some(true)).asJson.noSpaces)
                    ),
                    InlineKeyboardButton(
                      question.secondOption,
                      Some(CallbackMessage(question.quizIdd.toString, Some(false)).asJson.noSpaces)
                    )
                  )
                )
              })
          }
          become(awaitingRegister)
        case _ =>
          sendMessage("Choose topic, please!\n /register again to answer question in a different way.", userId)
      }
    case ReceiveTimeout =>
      context.stop(self)

  }

  def searching: PartialFunction[Any, Unit] = {
    case state: ActorState if state.value == "chatting" =>
      become(chatting)
    case message: Message =>
      message.text match {
        case Some(text) if message.entities.isDefined && text == "/leave" =>
          sendMessage(
            "Searching stopped!\n/search to start chatting\n/register again to answer question in a different way",
            userId
          )
          become(awaitingRegister)
        case _ => sendMessage("Nobody hears you!\n/leave to stop searching", userId)
      }

    case callback: CallbackQuery => invalidateCallback(callback)
    case ReceiveTimeout =>
      context.stop(self)

    case _ => logger.warn("possible callback in searching")
  }

  def chatting: PartialFunction[Any, Unit] = {
    case message: Message =>
      message.text match {
        case Some(text) if message.entities.isDefined && text == "/leave" =>
          cache.loadFromCache(userId.toLong).map {
            case -1L =>
              sendMessage("Error, returning", userId)
            case user =>
              usersDao.clearPair(userId.toLong, user).andThen {
                case Success(_) =>
                  cache.deletePair(user, userId.toLong)
                  sendMessage(
                    "Chat disconnected!\n/search to start chatting\n/register again to answer question in a different way",
                    user.toInt
                  )
                  context.parent ! ActorState("awaitingRegister", actorName)
                  context.parent ! ActorState("awaitingRegister", user.toString)
                  sendMessage(
                    "Chat disconnected!\n/search to start chatting\n/register again to answer question in a different way",
                    userId
                  )
              }
          }
          become(awaitingRegister)
        case Some(text) =>
          cache
            .loadFromCache(userId.toLong)
            .map(e => {
              CassandraDatabase.save(ChatLogsModel(UUID.randomUUID(), userId.toLong, e, text, DateTime.now()))
              sendMessage(text, e.intValue)
            })
            .recover { case _ => sendMessage("You are not in active chat!", userId) }
        case _ =>
          message.sticker match {
            case Some(sticker) =>
              cache.loadFromCache(userId.toLong).map(e => sendSticker(sticker.file_id, e.intValue)).recover {
                case _ => sendMessage("You are not in active chat!", userId)
              }
            case _ =>
              message.animation match {
                case Some(animation) =>
                  cache.loadFromCache(userId.toLong).map(e => sendAnimation(animation.file_id, e.intValue)).recover {
                    case _ => sendMessage("You are not in active chat!", userId)
                  }
                case _ =>
                  message.audio match {
                    case Some(audio) =>
                      cache.loadFromCache(userId.toLong).map(e => sendAudio(audio.file_id, e.intValue)).recover {
                        case _ => sendMessage("You are not in active chat!", userId)
                      }
                    case _ =>
                      message.photo match {
                        case Some(photo) =>
                          cache
                            .loadFromCache(userId.toLong)
                            .map(e => {
                              val res = photo.map(p => p.width)
                              sendPhoto(photo(res.indexOf(res.max)).file_id, e.intValue)
                            })
                            .recover { case _ => sendMessage("You are not in active chat!", userId) }
                        case _ =>
                          message.voice match {
                            case Some(voice) =>
                              cache
                                .loadFromCache(userId.toLong)
                                .map(e => sendVoice(voice.file_id, e.intValue))
                                .recover { case _ => sendMessage("You are not in active chat!", userId) }
                            case _ => sendMessage("unsupported type", userId)
                          }
                      }
                  }
              }

          }

      }

    case callback: CallbackQuery => invalidateCallback(callback)
    case ReceiveTimeout =>
      context.stop(self)
    case state: ActorState if state.value == "awaitingRegister" =>
      become(awaitingRegister)
  }

  val greeting =
    "Welcome!\nPlease, /register"

  override def receive: Receive = {
    case state: ActorState =>
      state.value match {
        case "init"             => become(init)
        case "registerStart"    => become(registerStart)
        case "awaitingRegister" => become(awaitingRegister)
        case "awaitingTopic"    => become(awaitingTopic)
        case "searching"        => become(searching)
        case "chatting"         => become(chatting)
      }
  }
}
