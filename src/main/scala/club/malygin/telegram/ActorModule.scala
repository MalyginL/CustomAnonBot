package club.malygin.telegram

import akka.actor.{ActorSystem, Props}
import club.malygin.data.appStat.{AppStatistic, AppStatisticImpl}
import club.malygin.data.cache.{CacheLoader, DatabaseCacheLoader, UserPairCache, UserPairCacheImpl}
import club.malygin.data.dataBase.pg.dao.{QuizQuestionDao, QuizQuestionService}
import com.google.inject.{AbstractModule, Provides, TypeLiteral}
import javax.inject.Named
import net.codingwell.scalaguice.ScalaModule

class ActorModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[ActorSystem].toInstance(ActorSystem())
    bind(classOf[CacheLoader]).to(classOf[DatabaseCacheLoader])
    bind(new TypeLiteral[UserPairCache[Long, Long]]() {})
      .to(classOf[UserPairCacheImpl])
    bind(classOf[AppStatistic]).to(classOf[AppStatisticImpl])
    bind(classOf[QuizQuestionDao]).to(classOf[QuizQuestionService])

  }

  @Provides
  @Named("routerActor")
  def getRouterActor(
      actorSystem: ActorSystem,
      cache: UserPairCache[Long, Long]
  ) = actorSystem.actorOf(Props(new RouterActor(cache)), "routerActor")

}
