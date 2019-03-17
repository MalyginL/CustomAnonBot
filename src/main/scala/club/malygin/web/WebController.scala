package club.malygin.web

import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{RequestEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import club.malygin.data.cache.{CacheEncoders, UserPairCache}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import javax.inject.{Inject, Named}

import scala.concurrent.ExecutionContext.Implicits.global

@Named
class WebController @Inject()(webService: WebService) extends FailFastCirceSupport with CacheEncoders {

  private val statRoutes = {
    (path("statistic" / "cache") & get) {
      complete(Marshal(webService.statistic).to[RequestEntity])
    }
  }

  private val apiRoutes = {
    (path("api" / "cache" / "current") & get) {
      complete(webService.currentPairs)
    }
  }

  private val telegramRoutes = {
    (path("telegram") & post){
      complete(StatusCodes.OK)
    }
  }
  val routes =telegramRoutes ~ statRoutes ~ apiRoutes


}
