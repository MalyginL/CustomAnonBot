package club.malygin.telegram

import akka.actor.{ActorRef, ActorSystem, Props}
import club.malygin.data.appStat.{AppStatistic, AppStatisticImpl}
import club.malygin.data.cache.{DatabaseCacheLoader, UserPairCache, UserPairCacheImpl}
import com.github.benmanes.caffeine.cache.CacheLoader
import com.google.inject.{AbstractModule, Provides, TypeLiteral}
import javax.inject.{Inject, Named}
import net.codingwell.scalaguice.ScalaModule

class ActorModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[ActorSystem].toInstance(ActorSystem())
    bind(new TypeLiteral[CacheLoader[Long, Long]]() {})
      .to(classOf[DatabaseCacheLoader])
    bind(new TypeLiteral[UserPairCache[Long, Long]]() {})
      .to(classOf[UserPairCacheImpl])
    bind(classOf[AppStatistic]).to(classOf[AppStatisticImpl])

  }

  @Provides
  @Named("routerActor")
  def getRouterActor(
      actorSystem: ActorSystem,
      cache: UserPairCache[Long, Long],
      @Named("commandActor") commandActor: ActorRef
  ) =
    actorSystem.actorOf(Props(new RouterActor(commandActor, cache)), "routerActor")

  @Provides
  @Named("commandActor")
  def getCommandActor(actorSystem: ActorSystem, cache: UserPairCache[Long, Long]) =
    actorSystem.actorOf(Props(new CommandActor(cache)), "commandActor")
}
