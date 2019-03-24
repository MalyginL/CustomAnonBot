package club.malygin.web

import akka.actor.ActorRef
import club.malygin.data.appStat.{AppStatModel, AppStatistic}
import club.malygin.data.cache.{CacheStatModel, UserPairCache}
import club.malygin.web.model.Update
import com.typesafe.scalalogging.LazyLogging
import javax.inject.{Inject, Named}



@Named
class WebService @Inject()(cache: UserPairCache[Long, Long], appStatistic: AppStatistic,@Named("jsonParser") jsonParser: ActorRef) extends LazyLogging{
  def process(json: Update) : Unit = {
    jsonParser ! json
 //   logger.debug(json.toString)
  }

  def statistic = cache.loadStatistic

  def currentPairs = cache.getCurrentCache

  def getAppInfo: AppStatModel = appStatistic.getAppStatistic



}
