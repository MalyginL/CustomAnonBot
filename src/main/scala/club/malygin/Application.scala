package club.malygin

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import club.malygin.data.appStat.{AppStatistic, AppStatisticImpl}
import club.malygin.data.cache.{DatabaseCacheLoader, UserPairCache, UserPairCacheImpl}
import club.malygin.web.WebController
import com.github.benmanes.caffeine.cache.CacheLoader
import com.google.inject.{AbstractModule, Guice, TypeLiteral}
import com.typesafe.scalalogging.LazyLogging

object Application extends App with LazyLogging{


  private val injector = Guice.createInjector(new AbstractModule() {
    override def configure() {
      bind(new TypeLiteral[CacheLoader[Long,Long]](){}).to(classOf[DatabaseCacheLoader])
      bind(new TypeLiteral[UserPairCache[Long,Long]](){}).to(classOf[UserPairCacheImpl])
      bind(classOf[AppStatistic]).to(classOf[AppStatisticImpl])
    }
  })

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val dispatcher = system.dispatcher

  private val contoller = injector.getInstance(classOf[WebController])


  logger.info(s"Starting server on ${Config.host}:${Config.port}")
  Http().bindAndHandle(contoller.routes, Config.host, Config.port)

}
