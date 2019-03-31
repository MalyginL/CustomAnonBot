package club.malygin.telegram

import akka.actor.Actor
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, RequestEntity, Uri}
import club.malygin.{Application, Config}
import club.malygin.telegram.botMethods.SendMessage
import club.malygin.web.{JsonDecoders, JsonEncoders}
import club.malygin.web.model._
import com.typesafe.scalalogging.LazyLogging
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport

import scala.concurrent.ExecutionContext.Implicits.global

class UserActor extends Actor with JsonEncoders with JsonDecoders with FailFastCirceSupport with LazyLogging {
  override def receive: Receive = {

    case message: Option[Message] => {
      logger.info(message.toString)
      val actorName = self.path.name
      logger.info(s"got message in $actorName")

      message match {
        case Some(m) => m.text match {
          case Some(text) => Marshal(SendMessage(actorName.toInt, text.toString)).to[RequestEntity]
            .map(k => HttpRequest(HttpMethods.POST, Uri(Config.apiBaseUrl + "sendMessage"), entity = k))
            .flatMap(Application.http.singleRequest(_))
          case None => logger.debug("no text")
        }
        case None => logger.debug("no message")
      }
    }
  }
}
