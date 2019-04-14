package club.malygin.telegram

import java.util.UUID

import akka.actor.Actor
import akka.actor.Status.Success
import club.malygin.data.cache.UserPairCache
import club.malygin.data.dataBase.pg.dao.{QuizQuestionDaoImpl, QuizResultsDaoImpl, UsersDaoImpl}
import club.malygin.data.dataBase.pg.model.{QuizQuestions, QuizResults, Users}
import club.malygin.web.model._
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContext.Implicits.global

class UserActor(cache: UserPairCache[Long, Long]) extends Actor with Commands with LazyLogging {

  val q = new QuizQuestionDaoImpl()
  val r = new QuizResultsDaoImpl()
  val u = new UsersDaoImpl()

  private val actorName = self.path.name

  override def receive: Receive = {
    case callback: CallbackQuery => {
      callback.message match {
        case Some(message) =>
          val callbackMessage = callback.data.get.split(",")
          r.saveOrUpdate(
            QuizResults(
              UUID.randomUUID,
              callback.from.id,
              UUID.fromString(callbackMessage(1)),
              callbackMessage(0).toBoolean
            ))

          answerCallbackQueryAndRemove(
            callback.id,
            message.chat.id.toString,
            message.message_id.intValue,
            callback.from.id
          )

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
              val qId = question.quizIdd.toString

              sendKeyboard(message.from.get.id.toString, question.text,
                Array(
                  InlineKeyboardButton(question.firstOption, Some(s"true,$qId")),
                  InlineKeyboardButton(question.secondOption, Some(s"false,$qId"))))
            })
        }
      }


      case "/startChat" => "asd" //Работа с базой
      case "/stopChat" => "asd"
      case _ => "asd"

    }


  val buttons = Array(InlineKeyboardButton("yes", Some("yes")), InlineKeyboardButton("false", Some("false")))
  val title = "Today's question is: true or false?"

  val greeting =
    "Hello friend!\nAnswer the question so that I can pick up the interlocutor\nYou can change your choice with the command\n /register"


}
