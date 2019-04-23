package club.malygin.telegram

import akka.actor.{ActorSystem, Props}
import club.malygin.Application
import club.malygin.data.appStat.{AppStatistic, AppStatisticImpl}
import club.malygin.data.cache.{CacheLoader, DatabaseCacheLoader, UserPairCache, UserPairCacheImpl}
import club.malygin.data.dataBase.pg.dao.{QuizQuestionDao, QuizQuestionService}
import com.google.inject.{AbstractModule, Provides, TypeLiteral}
import javax.inject.Named
import net.codingwell.scalaguice.ScalaModule

import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService}

class ActorModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[ActorSystem].toInstance(ActorSystem("telegram"))
    bind(classOf[CacheLoader]).to(classOf[DatabaseCacheLoader])
    bind(new TypeLiteral[UserPairCache[Long, Long]]() {})
      .to(classOf[UserPairCacheImpl])
    bind(classOf[AppStatistic]).to(classOf[AppStatisticImpl])
    bind(classOf[ ExecutionContextExecutorService]).toInstance(Application.ec)

  }

@Provides
  @Named("routerActor")
  def getRouterActor(
      actorSystem: ActorSystem,
      cache: UserPairCache[Long, Long]
  ) = actorSystem.actorOf(Props(new RouterActor(cache)), "TelegramRouterActor")

}
