package club.malygin.web

import akka.actor.ActorRef
import club.malygin.data.appStat.{AppStatModel, AppStatistic}
import club.malygin.data.cache.{CacheStatModel, UserPairCache}
import club.malygin.data.dataBase.cassandra.{CassandraDatabase, ChatLogsModel}
import club.malygin.data.dataBase.pg.model.QuizQuestions
import club.malygin.web.model.Update
import com.typesafe.scalalogging.LazyLogging
import javax.inject.{Inject, Named}

import scala.concurrent.Future

@Named
class WebService @Inject()(
                            cache: UserPairCache[Long, Long],
                            appStatistic: AppStatistic,
                            @Named("routerActor") routerActor: ActorRef
                          ) extends LazyLogging {

  def process(json: Update): Unit = {
    routerActor ! json
    //  logger.info(json.toString)
  }

  def statistic: CacheStatModel = cache.loadStatistic

  def currentPairs: collection.concurrent.Map[Long, Long] =
    cache.getCurrentCache

  def getAppInfo: AppStatModel = appStatistic.getAppStatistic

  def getCurrentTasks: Future[Seq[QuizQuestions]] = ???

  def addTask: Future[Unit] = ???

  def loadUserMessageHistory(user:BigInt): Future[Seq[ChatLogsModel]]  = CassandraDatabase.getUserMessages(user)

  def truncateCassandra : Future[Unit] = ???
}
