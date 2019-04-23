package club.malygin.telegram

import java.util.UUID

import io.circe.parser._
import io.circe.syntax._
import akka.actor.{Actor, ReceiveTimeout}
import club.malygin.data.cache.UserPairCache
import club.malygin.data.dataBase.cassandra.{CassandraDatabase, ChatLogsModel}
import club.malygin.data.dataBase.pg.dao.{QuizQuestionService, QuizResultsService, UsersService}
import club.malygin.data.dataBase.pg.model.{CallbackMessage, QuizQuestions, QuizResults, Users}
import club.malygin.web.model._
import com.typesafe.scalalogging.LazyLogging
import org.joda.time.DateTime

import scala.concurrent.duration._
import scala.util.{Failure, Success}

class UserActor(cache: UserPairCache[Long, Long]) extends Actor with Commands with LazyLogging {

  import context._

  context.setReceiveTimeout(5.minutes)

  override def postRestart(reason: Throwable): Unit = {
    super.postRestart(reason)
    logger.info(s"restarting actor $self")
  }

  private val actorName = self.path.name
  context.parent ! ActorState("?", actorName)

  def init: PartialFunction[Any, Unit] = {
    case message: Message =>
      message.text match {
        case Some(text) if message.entities.isDefined && text == "/start" =>
          sendMessage(greeting, actorName.toInt)
          message.from match {
            case Some(from) =>
              UsersService.saveOrUpdate(
                Users(
                  from.id,
                  from.first_name,
                  from.last_name,
                  from.username
                )
              )
              context.parent ! ActorState("registerStart", actorName)
              become(registerStart)
            case None => logger.warn("entering without address")
          }
        case _ => sendMessage("to start use /start command", actorName.toInt)
      }
    case callback: CallbackQuery => invalidateCallback(callback)
    case ReceiveTimeout =>
      context.stop(self)

  }

  def registerStart: PartialFunction[Any, Unit] = {
    case message: Message =>
      message.text match {
        case Some(text) if message.entities.isDefined && text == "/register" =>
          sendMessage("after quiz use /search command to start chat", actorName.toInt)
          QuizQuestionService.getActive.onComplete { e =>
            e.getOrElse(Seq.empty[QuizQuestions])
              .foreach(question => {
                sendKeyboard(
                  actorName,
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
        case _ => sendMessage("please register using /register command", actorName.toInt)
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
                case Right(entity) if entity.ans.isDefined =>
                  QuizResultsService.saveOrUpdate(
                    QuizResults(
                      UUID.randomUUID,
                      callback.from.id,
                      UUID.fromString(entity.id),
                      entity.ans.get //todo
                    )
                  )
                  answerCallbackQueryAndRemove(
                    callback.id,
                    message.chat.id.toString,
                    message.message_id.intValue,
                    callback.from.id
                  )
                case Left(_) => logger.warn("decoding callback error")
                case _       => invalidateCallback(callback)
              }
            case None => logger.warn("no result in callback")
          }
        case None => logger.warn("callback without message, possible API change")
      }
    case message: Message =>
      message.text match {
        case Some(text) if message.entities.isDefined && text == "/search" =>
          QuizQuestionService
            .getActiveWithAnswer(actorName.toLong)
            .map(
              _.map(
                q =>
                  InlineKeyboardButton(
                    q.text,
                    Some(CallbackMessage(q.quizIdd.toString).asJson.noSpaces)
                  )
              )
            )
            .map(k => sendKeyboard(actorName, "choose topic", k.toArray[InlineKeyboardButton]))
          context.parent ! ActorState("awaitingTopic", actorName)
          become(awaitingTopic)
        case Some(text) if message.entities.isDefined && text == "/register" =>
          sendMessage("after quiz use /search command to start chat", actorName.toInt)
          QuizQuestionService.getActive.onComplete { e =>
            e.getOrElse(Seq.empty[QuizQuestions])
              .foreach(question => {
                sendKeyboard(
                  actorName,
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
        case _ => sendMessage("please complete registration and use /search command", actorName.toInt)
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
                  UsersService.find(callback.from.id, UUID.fromString(entity.id)).onComplete {
                    case Success(res) =>
                      UsersService
                        .setPair(callback.from.id, res, UUID.fromString(entity.id))
                        .andThen {
                          case Success(_) =>
                            cache.addToCache(callback.from.id, res)
                            cache.addToCache(res, callback.from.id)
                        }
                        .andThen {
                          case Success(_) =>
                            sendMessage(s"Chat connected\n Have fun!", callback.from.id)
                            sendMessage(s"Chat connected\n Have fun!", res.toInt)
                            context.parent ! ActorState("chatting", actorName)
                            context.parent ! ActorState("chatting", res.toString)
                        }
                    case Failure(_) =>
                      UsersService
                        .updateStatusToActive(callback.from.id, UUID.fromString(entity.id))
                        .andThen {
                          case Success(_) =>
                            sendMessage("Added to queue, wait please", callback.from.id)
                            context.parent ! ActorState("searching", actorName)
                            become(searching)
                        }

                  }
                case Left(_) => logger.warn("decoding callback error")
                case _       => invalidateCallback(callback)
              }
            case None => logger.warn("no result in callback")
          }
        case None => logger.warn("callback without message, possible API change")
      }
    case message: Message =>
      message.text match {
        case Some(text) if message.entities.isDefined && text == "/register" =>
          sendMessage("after quiz use /search command to start chat", actorName.toInt)
          QuizQuestionService.getActive.onComplete { e =>
            e.getOrElse(Seq.empty[QuizQuestions])
              .foreach(question => {
                sendKeyboard(
                  actorName,
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
          sendMessage(
            "choose topic in list, sended before, you can change your answer with /register command",
            actorName.toInt
          )
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
          sendMessage("searching stopped", actorName.toInt)
          become(awaitingRegister)
        case _ => sendMessage("noone is hearing you, use /leave to stop searching", actorName.toInt)
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
          cache.loadFromCache(actorName.toLong).map {
            case -1L =>
              sendMessage("error, returning",actorName.toInt)
            case user =>
              UsersService.clearPair(actorName.toLong, user).andThen {
                case Success(_) =>
                  cache.deletePair(user, actorName.toLong)
                  sendMessage("chat disconnected", user.toInt)
                  context.parent ! ActorState("awaitingRegister", actorName)
                  context.parent ! ActorState("awaitingRegister", user.toString)
                  sendMessage(
                    "chat disconnected\n use /register to change answers\n or start new chat with /search",
                    actorName.toInt
                  )
              }
          }
          become(awaitingRegister)

        case Some(text) =>
          cache
            .loadFromCache(actorName.toLong)
            .map(e => {
              CassandraDatabase.save(ChatLogsModel(UUID.randomUUID(), actorName.toLong, e, text, DateTime.now()))
              sendMessage(text, e.intValue)
            })
            .recover { case _ => sendMessage("You are not in active chat", actorName.toInt) }
        case _ => logger.warn("unsupported type in chatting")
      }
    case callback: CallbackQuery => invalidateCallback(callback)
    case ReceiveTimeout =>
      context.stop(self)
    case state: ActorState if state.value == "awaitingRegister" =>
      become(awaitingRegister)
  }

  val greeting =
    "Hello friend!\nAnswer the question to pick up the interlocutor\nYou can change your choice with the command\n /register"

  override def receive: Receive = {
    case state: ActorState =>
      logger.info(state.toString)
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
