package club.malygin.telegram

import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import club.malygin.{Application, Config}
import club.malygin.telegram.botMethods.{AnswerCallbackQuery, EditMessageReplyMarkup, SendMessage}
import club.malygin.web.JsonEncoders
import club.malygin.web.model.{InlineKeyboardButton, InlineKeyboardMarkup}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait CommandService extends JsonEncoders with FailFastCirceSupport {

  val buttons = Array(InlineKeyboardButton("true", Some("true")), InlineKeyboardButton("false", Some("false")))
  val title   = "Today's question is: true or false?"

  val greeting =
    "Hello friend!\nAnswer the question so that I can pick up the interlocutor\nYou can change your choice with the command\n /register"

  def solve(command: String, id: Int) =
    command match {
      case "start" => //Future.sequence(Seq(start(id),register(id)))
        start(id).andThen { case _ => register(id) }
      case "register"  => register(id)
      case "startChat" => //Работа с базой
      case "stopChat"  =>
      case _           =>
    }

  def register(id: Int): Future[HttpResponse] =
    Marshal(SendMessage(id, title, reply_markup = Option(InlineKeyboardMarkup(Array(buttons)))))
      .to[RequestEntity]
      .map(k => HttpRequest(HttpMethods.POST, Uri(Config.apiBaseUrl + "sendMessage"), entity = k))
      .flatMap(Application.http.singleRequest(_))

  def start(id: Int): Future[HttpResponse] =
    Marshal(SendMessage(id, greeting))
      .to[RequestEntity]
      .map(k => HttpRequest(HttpMethods.POST, Uri(Config.apiBaseUrl + "sendMessage"), entity = k))
      .flatMap(Application.http.singleRequest(_))

  def answer(choice: String, id: String, chatId: String, messageId: Int, userId: Int) = {

    Marshal(AnswerCallbackQuery(id, Some("success")))
      .to[RequestEntity]
      .map(k => HttpRequest(HttpMethods.POST, Uri(Config.apiBaseUrl + "answerCallbackQuery"), entity = k))
      .flatMap(Application.http.singleRequest(_))

    Marshal(EditMessageReplyMarkup(Some(chatId), Some(messageId), Some(InlineKeyboardMarkup(Array()))))
      .to[RequestEntity]
      .map(k => HttpRequest(HttpMethods.POST, Uri(Config.apiBaseUrl + "editMessageReplyMarkup"), entity = k))
      .flatMap(Application.http.singleRequest(_))

    Marshal(SendMessage(userId, s"You've chosen $choice\nYou can start chat with /startChat command\nGood luck!"))
      .to[RequestEntity]
      .map(k => HttpRequest(HttpMethods.POST, Uri(Config.apiBaseUrl + "sendMessage"), entity = k))
      .flatMap(Application.http.singleRequest(_))
  }

}
