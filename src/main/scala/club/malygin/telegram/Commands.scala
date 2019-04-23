package club.malygin.telegram

import akka.http.scaladsl.Http
import club.malygin.web.{JsonDecoders, JsonEncoders}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, RequestEntity, Uri}
import club.malygin.Config
import club.malygin.telegram.botMethods.{AnswerCallbackQuery, EditMessageReplyMarkup, SendMessage}
import club.malygin.web.model.{CallbackQuery, InlineKeyboardButton, InlineKeyboardMarkup}
import com.typesafe.scalalogging.LazyLogging
import club.malygin.Application.system

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

trait Commands extends JsonEncoders with JsonDecoders with FailFastCirceSupport with LazyLogging {

  val http = Http()

  def sendMessage(text: String, userId: Int)(implicit executor: ExecutionContext): Unit =
    Marshal(SendMessage(userId, text))
      .to[RequestEntity]
      .map(k => HttpRequest(HttpMethods.POST, Uri(Config.apiBaseUrl + "sendMessage"), entity = k))
      .flatMap(http.singleRequest(_))

  def answerCallbackQueryAndRemove(id: String, chatId: String, messageId: Int, userId: Int)(
      implicit executor: ExecutionContext
  ): Unit = {
    val answ = Marshal(AnswerCallbackQuery(id, Some("Answer accepted")))
      .to[RequestEntity]
      .map(k => HttpRequest(HttpMethods.POST, Uri(Config.apiBaseUrl + "answerCallbackQuery"), entity = k))
      .flatMap(http.singleRequest(_))

    answ.onComplete {
      case Success(_) =>
        Marshal(EditMessageReplyMarkup(Some(chatId), Some(messageId), Some(InlineKeyboardMarkup(Array()))))
          .to[RequestEntity]
          .map(k => HttpRequest(HttpMethods.POST, Uri(Config.apiBaseUrl + "editMessageReplyMarkup"), entity = k))
          .flatMap(http.singleRequest(_))
      case Failure(_) => logger.error(s"error in removing inlinequery for user $userId")
    }
  }

  def sendKeyboard(id: String, title: String, buttons: Array[InlineKeyboardButton])(
      implicit executor: ExecutionContext
  ): Unit =
    Marshal(SendMessage(id.toInt, title, reply_markup = Option(InlineKeyboardMarkup(Array(buttons)))))
      .to[RequestEntity]
      .map(k => HttpRequest(HttpMethods.POST, Uri(Config.apiBaseUrl + "sendMessage"), entity = k))
      .flatMap(http.singleRequest(_))

  def invalidateCallback(callback: CallbackQuery)(implicit executor: ExecutionContext): Unit =
    callback.message match {
      case Some(message) =>
        val answ = Marshal(AnswerCallbackQuery(callback.id, Some("Answer ignored, try again")))
          .to[RequestEntity]
          .map(k => HttpRequest(HttpMethods.POST, Uri(Config.apiBaseUrl + "answerCallbackQuery"), entity = k))
          .flatMap(http.singleRequest(_))
        answ.onComplete {
          case Success(_) =>
            Marshal(
              EditMessageReplyMarkup(
                Some(message.chat.id.toString),
                Some(message.message_id.toInt),
                Some(InlineKeyboardMarkup(Array()))
              )
            ).to[RequestEntity]
              .map(k => HttpRequest(HttpMethods.POST, Uri(Config.apiBaseUrl + "editMessageReplyMarkup"), entity = k))
              .flatMap(http.singleRequest(_))
          case Failure(_) => logger.error(s"error")
        }
      case None => logger.warn("callback issue")
    }
}
