package club.malygin.telegram

import java.util.UUID


import io.circe.parser._
import io.circe.syntax._
import akka.actor.Actor
import club.malygin.data.cache.UserPairCache
import club.malygin.data.dataBase.pg.dao.{QuizQuestionDaoImpl, QuizResultsDaoImpl, UsersDaoImpl}
import club.malygin.data.dataBase.pg.model.{CallbackMessage, QuizQuestions, QuizResults, Users}
import club.malygin.web.model._
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}


class UserActor(cache: UserPairCache[Long, Long]) extends Actor with Commands with LazyLogging {

  import context._

  val q = new QuizQuestionDaoImpl()
  val r = new QuizResultsDaoImpl()
  val u = new UsersDaoImpl()
  become(init)
  private val actorName = self.path.name

  /** -------------------------------------------------------- */
  def init: PartialFunction[Any, Unit] = {
    case message: Message =>
      message.text match {
        case Some(text) if message.entities.isDefined && text == "/start" =>
          sendMessage(greeting, message.from.get.id)
          u.saveOrUpdate(Users(
            message.from.get.id,
            message.from.get.first_name,
            message.from.get.last_name,
            message.from.get.username
          ))
          become(registerStart)
        case _ => sendMessage("to start use /start command", message.from.get.id)
      }
    case callback: CallbackQuery => invalidateCallback(callback)
    case _ => logger.warn("Someone accessing to bot without /start message")
  }

  def registerStart: PartialFunction[Any, Unit] = {
    case message: Message => {
      message.text match {
        case Some(text) if message.entities.isDefined && text == "/register" =>
          q.getActive.onComplete { e =>
            e.getOrElse(Seq.empty[QuizQuestions]).foreach(
              question => {
                sendKeyboard(message.from.get.id.toString, question.text,
                  Array(
                    InlineKeyboardButton(question.firstOption, Some(CallbackMessage(question.quizIdd.toString, Some(true)).asJson.toString.replaceAll("\\s", ""))),
                    InlineKeyboardButton(question.secondOption, Some(CallbackMessage(question.quizIdd.toString, Some(false)).asJson.toString.replaceAll("\\s", "")))))
              })
          }
          become(awaitingRegister)
        case _ => sendMessage("please register using /register command", actorName.toInt)
      }
    }
    case callback: CallbackQuery => invalidateCallback(callback)
    case _ => logger.warn("Someone accessing to bot without /start message")
  }


  def awaitingRegister: PartialFunction[Any, Unit] = {
    case callback: CallbackQuery =>
      callback.message match {
        case Some(message) =>
          callback.data match {
            case Some(data) =>
              decode[CallbackMessage](data) match {
                case Right(entity) if entity.ans.isDefined =>
                  r.saveOrUpdate(
                    QuizResults(
                      UUID.randomUUID,
                      callback.from.id,
                      UUID.fromString(entity.id),
                      entity.ans.get //todo
                    ))
                  answerCallbackQueryAndRemove(
                    callback.id,
                    message.chat.id.toString,
                    message.message_id.intValue,
                    callback.from.id
                  )
                case Left(_) => logger.warn("decoding callback error")
                case _ => invalidateCallback(callback)
              }
            case None => logger.warn("no result in callback")
          }
        case None => logger.warn("callback without message, possible API change")
      }
    case message: Message => {
      message.text match {
        case Some(text) if message.entities.isDefined && text == "/startChat" =>
          cache.loadFromCache(actorName.toLong)
            .andThen {
              case Failure(_) =>
                q.getCurrentwithAnswer(actorName.toLong).map(_.map(q => InlineKeyboardButton(q.text, Some(CallbackMessage(q.quizIdd.toString).asJson.toString.replaceAll("\\s", "")))))
                  .map(k => sendKeyboard(message.from.get.id.toString, "choose topic", k.toArray[InlineKeyboardButton])
                  )
                become(awaitingTopic)
              case Success(_) =>
                sendMessage("you are in chat, use /stopChat", actorName.toInt)
            }
        case Some(text) if message.entities.isDefined && text == "/register" =>
          q.getActive.onComplete { e =>
            e.getOrElse(Seq.empty[QuizQuestions]).foreach(
              question => {
                sendKeyboard(message.from.get.id.toString, question.text,
                  Array(
                    InlineKeyboardButton(question.firstOption, Some(CallbackMessage(question.quizIdd.toString, Some(true)).asJson.toString.replaceAll("\\s", ""))),
                    InlineKeyboardButton(question.secondOption, Some(CallbackMessage(question.quizIdd.toString, Some(false)).asJson.toString.replaceAll("\\s", "")))))
              })
          }
        case _ => sendMessage("please complete registration and you /startChat command", actorName.toInt)
      }
    }
  }

  def awaitingTopic: PartialFunction[Any, Unit] = {
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
                  val k = u.find(callback.from.id, UUID.fromString(entity.id))

                  k.onComplete {
                    case Success(res) => {
                      u.setPair(callback.from.id, res, UUID.fromString(entity.id)).andThen { case Success(_) =>
                        cache.addToCache(callback.from.id, res)
                        cache.addToCache(res, callback.from.id)
                      }.andThen { case Success(_) =>
                        sendMessage(s"Chat connected\n Have fun!", callback.from.id)
                        sendMessage(s"Chat connected\n Have fun!", res.toInt)
                        become(chatting)
                      }
                    }
                    case Failure(_) =>
                      u.updateStatusToActive(callback.from.id, UUID.fromString(entity.id))
                        .andThen {
                          case Success(_) => sendMessage("Added to queue, wait please", callback.from.id)
                            become(searching)
                        }

                  }
                case Left(_) => logger.warn("decoding callback error")
                case _ => invalidateCallback(callback)
              }
            case None => logger.warn("no result in callback")
          }
        case None => logger.warn("callback without message, possible API change")
      }
    case message: Message =>
      message.text match {
        case Some(text) if message.entities.isDefined && text == "/register" =>
          q.getActive.onComplete { e =>
            e.getOrElse(Seq.empty[QuizQuestions]).foreach(
              question => {
                sendKeyboard(message.from.get.id.toString, question.text,
                  Array(
                    InlineKeyboardButton(question.firstOption, Some(CallbackMessage(question.quizIdd.toString, Some(true)).asJson.toString.replaceAll("\\s", ""))),
                    InlineKeyboardButton(question.secondOption, Some(CallbackMessage(question.quizIdd.toString, Some(false)).asJson.toString.replaceAll("\\s", "")))))
              })
          }
          become(awaitingRegister)
        case _ => sendMessage("choose topic in list, sended before, you can change your answer with /register command", actorName.toInt)
      }
  }

  def searching: PartialFunction[Any, Unit] = {
    case message: Message => {
      message.text match {
        case Some(text) if message.entities.isDefined && text == "/stopchat" =>
        case _ => sendMessage("noone is hearing you, use /stopchat to stop searching", actorName.toInt)
      }
    }
    case callback: CallbackQuery => invalidateCallback(callback)
    case None => logger.warn("possible callback in searching")
  }

  def chatting: PartialFunction[Any, Unit] = {
    case message: Message => {
      message.text match {
        case Some(text) if message.entities.isDefined && text == "/stopchat" =>
          cache.loadFromCache(message.from.get.id).map(user =>
            u.clearPair(message.from.get.id, user).andThen { case Success(_) => {
              cache.deletePair(user, message.from.get.id)
              sendMessage("chat disconnected", user.toInt)
              sendMessage("chat disconnected", message.from.get.id.intValue)
            }
            }).recoverWith { case _ => sendMessage("you are not in chat", message.from.get.id) }
        //  become()

        case Some(text) =>
          cache.loadFromCache(actorName.toLong)
            .map(e =>
              sendMessage(text, e.intValue))
            .recover { case _ => sendMessage("You are not in active chat", actorName.toInt) }
        case _ => //todo
      }
    }
    case callback: CallbackQuery => invalidateCallback(callback)
  }


  /** -------------------------------------------------------- */


  val greeting =
    "Hello friend!\nAnswer the question so that I can pick up the interlocutor\nYou can change your choice with the command\n /register"
  override def receive: Receive = {
    case _ => print("asd")
  }
}
