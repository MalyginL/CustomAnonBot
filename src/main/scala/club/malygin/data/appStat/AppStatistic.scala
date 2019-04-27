package club.malygin.data.appStat

trait AppStatistic {

  /**Provides jvm application statistic*/
  def getAppStatistic: AppStatModel

}
