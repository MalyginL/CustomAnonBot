package club.malygin.telegram

import akka.actor.{ActorSystem, Props}
import club.malygin.{Application, Config}
import club.malygin.data.appStat.{AppStatistic, AppStatisticImpl}
import club.malygin.data.cache.{CacheLoader, DatabaseCacheLoader, UserPairCache, UserPairCacheImpl}
import club.malygin.data.dataBase.pg.dao._
import com.google.inject.{AbstractModule, Provides, Scopes, TypeLiteral}
import javax.inject.Named
import net.codingwell.scalaguice.ScalaModule
import slick.jdbc.PostgresProfile

import scala.concurrent.ExecutionContextExecutorService

class ActorModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    bind[ActorSystem].toInstance(ActorSystem("telegram"))
    bind(classOf[CacheLoader]).to(classOf[DatabaseCacheLoader]).in(Scopes.SINGLETON)
    bind(new TypeLiteral[UserPairCache[Long, Long]]() {})
      .to(classOf[UserPairCacheImpl])
      .in(Scopes.SINGLETON)
    bind(classOf[AppStatistic]).to(classOf[AppStatisticImpl]).in(Scopes.SINGLETON)
    bind(classOf[ExecutionContextExecutorService]).toInstance(Config.ec)
    bind(classOf[PostgresProfile.Backend#Database]).toInstance(Config.sqldb)
    bind(classOf[QuizQuestionDao]).to(classOf[QuizQuestionService]).in(Scopes.SINGLETON)
    bind(classOf[QuizResultsDao]).to(classOf[QuizResultsService]).in(Scopes.SINGLETON)
    bind(classOf[UsersDao]).to(classOf[UsersService]).in(Scopes.SINGLETON)

  }

  @Provides
  @Named("routerActor")
  def getRouterActor(
      usersDao: UsersDao,
      quizResultsDao: QuizResultsDao,
      quizQuestionDao: QuizQuestionDao,
      actorSystem: ActorSystem,
      cache: UserPairCache[Long, Long]
  ) =
    actorSystem.actorOf(Props(new RouterActor(cache, usersDao, quizResultsDao, quizQuestionDao)), "TelegramRouterActor")

}
