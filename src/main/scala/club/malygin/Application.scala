package club.malygin

import java.util.concurrent.Executors

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import club.malygin.telegram.ActorModule
import club.malygin.web.WebController
import com.google.inject.Guice
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService}

object Application extends App with LazyLogging {
  implicit val ec: ExecutionContextExecutorService = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(200))
  val injector = Guice.createInjector(new ActorModule)

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val dispatcher = system.dispatcher

  private val contoller = injector.getInstance(classOf[WebController])
  logger.info(s"Starting server on ${Config.host}:${Config.port}")
  val http = Http()
  http.bindAndHandle(contoller.routes, Config.host, Config.port)


}
