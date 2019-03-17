package club.malygin.web

import club.malygin.data.appStat.{AppStatModel, AppStatistic}
import club.malygin.data.cache.UserPairCache
import club.malygin.web.model.Update
import com.typesafe.scalalogging.LazyLogging
import javax.inject.{Inject, Named}



@Named
class WebService @Inject()(cache: UserPairCache[Long, Long], appStatistic: AppStatistic) extends LazyLogging{
  def process(json: Update) : Unit = {
    logger.debug(json.toString)
  }

  def statistic = cache.loadStatistic

  def currentPairs = cache.getCurrentCache

  def getAppInfo: AppStatModel = appStatistic.getAppStatistic



}
