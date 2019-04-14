package club.malygin.telegram


import club.malygin.web.{JsonDecoders, JsonEncoders}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, RequestEntity, Uri}
import club.malygin.data.dataBase.pg.model.QuizResults
import club.malygin.{Application, Config}
import club.malygin.telegram.botMethods.{AnswerCallbackQuery, EditMessageReplyMarkup, SendMessage}
import club.malygin.web.model.{InlineKeyboardButton, InlineKeyboardMarkup}
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}


trait Commands extends JsonEncoders with JsonDecoders with FailFastCirceSupport with LazyLogging {



  def sendMessage(text: String, userId: Int) = {
    Marshal(SendMessage(userId, text))
      .to[RequestEntity]
      .map(k => HttpRequest(HttpMethods.POST, Uri(Config.apiBaseUrl + "sendMessage"), entity = k))
      .flatMap(Application.http.singleRequest(_))
  }

  def answerCallbackQueryAndRemove(id: String, chatId: String, messageId: Int, userId: Int) = {
    val answ = Marshal(AnswerCallbackQuery(id, Some("Answer accepted")))
      .to[RequestEntity]
      .map(k => HttpRequest(HttpMethods.POST, Uri(Config.apiBaseUrl + "answerCallbackQuery"), entity = k))
      .flatMap(Application.http.singleRequest(_))

    answ.onComplete {
      case Success(_) => {
        Marshal(EditMessageReplyMarkup(Some(chatId), Some(messageId), Some(InlineKeyboardMarkup(Array()))))
          .to[RequestEntity]
          .map(k => HttpRequest(HttpMethods.POST, Uri(Config.apiBaseUrl + "editMessageReplyMarkup"), entity = k))
          .flatMap(Application.http.singleRequest(_))
      }
      case Failure(_) => logger.error(s"error in removing inlinequery for user $userId")
    }
  }

  def sendKeyboard(id: String, title: String, buttons: Array[InlineKeyboardButton]) = {


    Marshal(SendMessage(id.toInt, title, reply_markup = Option(InlineKeyboardMarkup(Array(buttons)))))
      .to[RequestEntity]
      .map(k => HttpRequest(HttpMethods.POST, Uri(Config.apiBaseUrl + "sendMessage"), entity = k))
      .flatMap(Application.http.singleRequest(_))
  }

}