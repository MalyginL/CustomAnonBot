package club.malygin

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import club.malygin.actors.ActorModule

import club.malygin.web.WebController
import com.google.inject.Guice
import com.typesafe.scalalogging.LazyLogging

object Application extends App with LazyLogging{

  val injector = Guice.createInjector(new ActorModule)

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val dispatcher = system.dispatcher

  private val contoller = injector.getInstance(classOf[WebController])

  logger.info(s"Starting server on ${Config.host}:${Config.port}")

  Http().bindAndHandle(contoller.routes, Config.host, Config.port)



}
