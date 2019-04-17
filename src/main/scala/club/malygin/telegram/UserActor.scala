package club.malygin.telegram

import java.util.UUID

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import akka.actor.Actor
import club.malygin.data.cache.{UserPairCache}
import club.malygin.data.dataBase.pg.dao.{QuizQuestionDaoImpl, QuizResultsDaoImpl, UsersDaoImpl}
import club.malygin.data.dataBase.pg.model.{CallbackMessage, QuizQuestions, QuizResults, Users}
import club.malygin.web.model._
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class UserActor(cache: UserPairCache[Long, Long]) extends Actor with Commands with LazyLogging {


  val q = new QuizQuestionDaoImpl()
  val r = new QuizResultsDaoImpl()
  val u = new UsersDaoImpl()

  private val actorName = self.path.name

  override def receive: Receive = {
    case callback: CallbackQuery => {
      callback.message match {
        case Some(message) => {
          callback.data match {
            case Some(data) => {
              val p = decode[CallbackMessage](data)
              logger.info(p.toString)
              p match {
                case Right(entity) => {
                  entity.ans match {
                    case Some(answer) => {

                      /** Registration type of message */
                      r.saveOrUpdate(
                        QuizResults(
                          UUID.randomUUID,
                          callback.from.id,
                          UUID.fromString(entity.id),
                          answer
                        ))

                      answerCallbackQueryAndRemove(
                        callback.id,
                        message.chat.id.toString,
                        message.message_id.intValue,
                        callback.from.id
                      )
                    }
                    case None => {
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
                          }
                        }
                        case Failure(_) =>
                          u.updateStatusToActive(callback.from.id, UUID.fromString(entity.id))
                            .andThen {
                              case Success(_) => sendMessage("Added to queue, wait please", callback.from.id)
                            }

                      }
                    }


                    /** Starting chat type of message */
                  }


                }
                case Left(entity)
                => logger.warn(s"decoding fail $entity")
              }
            }
          }
        }
      }
    }
    case message: Message => {
      message.text match {

        case Some(text) if message.entities.isDefined => {
          solve(text, message)
        }
        case Some(text) =>
          cache.loadFromCache(actorName.toLong)
            .map(e => sendMessage(text, e.intValue))
            .recover { case _ => sendMessage("You are not in active chat", actorName.toInt) }
      }
    }
  }


  def solve(command: String, message: Message) =
    command match {
      case "/start" => {
        sendMessage(greeting, message.from.get.id)
        u.saveOrUpdate(Users(
          message.from.get.id,
          message.from.get.first_name,
          message.from.get.last_name,
          message.from.get.username
        ))
      }
      case "/register" => {
        q.getActive.onComplete { e =>
          e.getOrElse(Seq.empty[QuizQuestions]).foreach(
            question => {
              sendKeyboard(message.from.get.id.toString, question.text,
                Array(
                  InlineKeyboardButton(question.firstOption, Some(CallbackMessage(question.quizIdd.toString, Some(true)).asJson.toString.replaceAll("\\s", ""))),
                  InlineKeyboardButton(question.secondOption, Some(CallbackMessage(question.quizIdd.toString, Some(false)).asJson.toString.replaceAll("\\s", "")))))
            })
        }
      }

      case "/startChat" => {
        val t = q.getActive.map(_.map(q => InlineKeyboardButton(q.text, Some(CallbackMessage(q.quizIdd.toString).asJson.toString.replaceAll("\\s", "")))))
          .map(k => sendKeyboard(message.from.get.id.toString, "choose topic", k.toArray[InlineKeyboardButton])
          )
      }

      case "/stopChat" => "asd"
      case _ => "asd"

    }

  val greeting =
    "Hello friend!\nAnswer the question so that I can pick up the interlocutor\nYou can change your choice with the command\n /register"


}
