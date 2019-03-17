package club.malygin.web

import club.malygin.data.appStat.{AppStatModel, AppStatistic}
import club.malygin.data.cache.UserPairCache
import javax.inject.{Inject, Named}

@Named
class WebService @Inject()(cache: UserPairCache[Long, Long], appStatistic: AppStatistic) {

  def statistic = cache.loadStatistic

  def currentPairs = cache.getCurrentCache

  def getAppInfo: AppStatModel = appStatistic.getAppStatistic



}
