package club.malygin.web

import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{RequestEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._

import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import javax.inject.{Inject, Named}

import scala.concurrent.ExecutionContext.Implicits.global

@Named
class WebController @Inject()(webService: WebService) extends FailFastCirceSupport with JsonEncoders {

  private val statRoutes = {
    (path("statistic" / "cache") & get) {
      complete(Marshal(webService.statistic).to[RequestEntity])
    } ~
      (path("statistic" / "app") & get) {
        complete(Marshal(webService.getAppInfo).to[RequestEntity])
      }
  }

  private val apiRoutes = {
    (path("api" / "cache" / "current") & get) {
      complete(webService.currentPairs)
    }
  }

  private val telegramRoutes = {
    (path("telegram") & post) {
      complete(StatusCodes.OK)
    }
  }
  val routes = telegramRoutes ~ statRoutes ~ apiRoutes


}
