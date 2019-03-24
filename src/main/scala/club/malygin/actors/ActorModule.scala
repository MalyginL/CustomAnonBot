package club.malygin.actors
import akka.actor.{ActorSystem, Props}
import com.google.inject.Provides
import javax.inject.Named
import net.codingwell.scalaguice.ScalaModule
import club.malygin.data.appStat.{AppStatistic, AppStatisticImpl}
import club.malygin.data.cache.{DatabaseCacheLoader, UserPairCache, UserPairCacheImpl}
import com.github.benmanes.caffeine.cache.CacheLoader
import com.google.inject.{AbstractModule, TypeLiteral}

class ActorModule extends AbstractModule with ScalaModule{
  override def configure() : Unit = {
    bind[ActorSystem].toInstance(ActorSystem())
    bind(new TypeLiteral[CacheLoader[Long,Long]](){}).to(classOf[DatabaseCacheLoader])
    bind(new TypeLiteral[UserPairCache[Long,Long]](){}).to(classOf[UserPairCacheImpl])
    bind(classOf[AppStatistic]).to(classOf[AppStatisticImpl])

  }


  @Provides
  @Named("jsonParser")
  def getMyActor(actorSystem: ActorSystem) = {
    actorSystem.actorOf(Props(new JsonActor),"jsonParser")
  }
}




