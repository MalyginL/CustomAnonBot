package club.malygin.data.appStat

import org.scalatest.{FlatSpec, Matchers}

class AppStatisticImplTest extends FlatSpec with Matchers {

  val appStatisticImpl = new AppStatisticImpl
  behavior of "AppStatisticImpl"

  it should "return valid model" in {
    val stat = appStatisticImpl.getAppStatistic
    stat.threadCount > 0 shouldBe true
    stat.daemons > 0 shouldBe true
    stat.uptime > 0 shouldBe true
    stat.pid > 0 shouldBe true
    stat.uptime > 0 shouldBe true
    (stat.heapMax > 0) && stat.heapMax > stat.heapUsed shouldBe true
    (stat.heapMax > 0) && stat.heapMax > stat.heapUsed shouldBe true
  }
}
