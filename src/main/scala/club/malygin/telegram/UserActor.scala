package club.malygin.telegram

import akka.actor.{Actor, ActorRef}
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, RequestEntity, Uri}
import club.malygin.{Application, Config}
import club.malygin.telegram.botMethods.SendMessage
import club.malygin.web.{JsonDecoders, JsonEncoders}
import club.malygin.web.model._
import com.google.inject.name.Named
import com.typesafe.scalalogging.LazyLogging
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import javax.inject.Inject

import scala.concurrent.ExecutionContext.Implicits.global

class UserActor extends Actor with JsonEncoders with JsonDecoders with FailFastCirceSupport with LazyLogging{
  override def receive: Receive = {

    case message:Option[Message] =>{
      logger.info(message.toString)
      val actorName = self.path.name
      logger.info(s"got message in $actorName")

      message match {
        case Some(m) =>{
          if (m.entities.isDefined){
            logger.info("sending")
//            import io.circe.syntax._
//            print((SendMessage(829491453,
//              "reg",
//              reply_markup = Option(ReplyKeyboardMarkup(
//                Array(Array(
//                  KeyboardButton("first")
//                  ,KeyboardButton("second"))
//              )))
//              ).asJson
//            ))




          }
        }


        case None => "no message"
      }
//      message.get.text match {
//        case Some(e) => {
//          Marshal(SendMessage(actorName.toInt, e.toString)).to[RequestEntity]
//            .map(
//              k =>
//                HttpRequest(HttpMethods.POST, Uri(Config.apiBaseUrl + "sendMessage"), entity = k))
//            .flatMap(Application.http.singleRequest(_))
//        }
//      }
    }

    case callback:CallbackQuery =>{Marshal(SendMessage(229087075, "otvet")).to[RequestEntity]
      .map(
        k =>
          HttpRequest(HttpMethods.POST, Uri(Config.apiBaseUrl + "sendMessage"), entity = k))
      .flatMap(Application.http.singleRequest(_))}
    case _ => logger.info("failed")
  }
}
