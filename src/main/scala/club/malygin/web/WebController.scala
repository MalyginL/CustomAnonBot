package club.malygin.web

import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{RequestEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import club.malygin.web.model.Update
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import javax.inject.{Inject, Named}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Named
class WebController @Inject()(webService: WebService) extends FailFastCirceSupport with JsonEncoders with JsonDecoders {

  import ch.megard.akka.http.cors.scaladsl.CorsDirectives._


  private val statRoutes = cors() {
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
      entity(as[Update])(implicit json => {
        webService.process(json)
        complete(StatusCodes.OK)
      })
    }
  }
  val routes = telegramRoutes ~ statRoutes ~ apiRoutes


}
