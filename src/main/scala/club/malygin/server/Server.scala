package club.malygin.server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import club.malygin.server.json.{JsonDecoders, JsonEncoders}
import club.malygin.server.models.methods.SendMessage
import club.malygin.server.models.webhook.Update
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import com.github.blemale.scaffeine.{Cache, Scaffeine}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.{LazyLogging, Logger}

class Server extends FailFastCirceSupport with JsonEncoders with JsonDecoders with LazyLogging{

  val cf = ConfigFactory.load("debug")

  val token: String = cf.getString("bot.token")
  val apiBaseUrl: String = cf.getString("server.apiUrl")
  val host: String = cf.getString("server.host")
  val port: Int = cf.getInt("server.port")

  logger.debug(s"Starting on $host:$port")

  implicit val system: ActorSystem = ActorSystem("main")
  implicit val executor: ExecutionContext = system.dispatcher

  implicit val materializer: ActorMaterializer = ActorMaterializer()


  val cache: Cache[String, String] =
    Scaffeine()
      .recordStats()
      .expireAfterWrite(1.hour)
      .maximumSize(500)
      .build[String, String]()


  val http = Http()

  val route = pathSingleSlash {
    post {
      entity(as[Update])(implicit json => {
        cache.put(json.message.get.from.get.id.toString, json.message.get.text.get)
        logger.debug(json.toString)

        Marshal(SendMessage(json.message.get.from.get.id, json.message.get.text.get)).to[RequestEntity]
          .map(
            k =>
              HttpRequest(HttpMethods.POST, Uri(apiBaseUrl+"sendMessage"), entity = k))
          .flatMap(http.singleRequest(_))
        complete(StatusCodes.OK)
      })
    }
  } ~
    path("info") {
      get {
        complete(cache.asMap())

      }
    }


  http.bindAndHandle(route, host, port)


}
