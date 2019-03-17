package club.malygin.web

import club.malygin.data.cache.UserPairCache
import javax.inject.{Inject, Named}

@Named
class WebService @Inject()(cache: UserPairCache[Long, Long]){

  def statistic = cache.loadStatistic

  def currentPairs = cache.getCurrentCache


}
