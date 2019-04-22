package club.malygin.web

import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{RequestEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{MalformedRequestContentRejection, RejectionHandler, Route}
import club.malygin.web.model.Update
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import javax.inject.{Inject, Named}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

@Named
class WebController @Inject()(webService: WebService) extends FailFastCirceSupport with JsonEncoders with JsonDecoders {

  import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

  implicit def TelegramRejectionHandler =
    RejectionHandler
      .newBuilder()
      .handleAll[MalformedRequestContentRejection] { _ =>
        complete(StatusCodes.BadRequest, "You are not Telegram!")
      }
      .result()

  private def statRoutes: Route = cors() {
    (path("statistic" / "cache") & get) {
      complete(Marshal(webService.statistic).to[RequestEntity])
    } ~
      (path("statistic" / "app") & get) {
        complete(Marshal(webService.getAppInfo).to[RequestEntity])
      }
  }

  private def apiRoutes: Route =
    (path("api" / "cache" / "current") & get) {
      complete(webService.currentPairs)
    } ~
      (path("api" / "logs" / LongNumber) & get) { number =>
        complete(Marshal(webService.loadUserMessageHistory(number)).to[RequestEntity])
      } ~
      (path("api" / "logs" / "clear") & get) {
        webService.truncateCassandra
        complete(StatusCodes.OK)
      }

  private def telegramRoutes: Route =
    handleRejections(TelegramRejectionHandler) {
      (path("telegram") & post) {
        entity(as[Update])(implicit json => {
          webService.process(json)
          complete(StatusCodes.OK)
        })
      }
    }

  def routes: Route = telegramRoutes ~ statRoutes ~ apiRoutes
}
