package club.malygin.data.appStat

import java.lang.management.{ManagementFactory, MemoryMXBean, RuntimeMXBean, ThreadMXBean}

import javax.inject.Named

@Named
class AppStatisticImpl extends AppStatistic {

  private val rb: RuntimeMXBean = ManagementFactory.getRuntimeMXBean
  private val mb: MemoryMXBean = ManagementFactory.getMemoryMXBean
  private val tb: ThreadMXBean = ManagementFactory.getThreadMXBean
  private val pid = rb.getName.split("@")(0).toLong

  def getAppStatistic: AppStatModel = {
    AppStatModel(
      javaVendor = rb.getVmVendor,
      javaVersion = rb.getSpecVersion,
      pid = pid,
      uptime = rb.getUptime,

      heapInit = mb.getHeapMemoryUsage.getInit,
      heapUsed = mb.getHeapMemoryUsage.getUsed,
      heapCommited = mb.getHeapMemoryUsage.getCommitted,
      heapMax = mb.getHeapMemoryUsage.getMax,

      nonHeapUsed = mb.getNonHeapMemoryUsage.getUsed,
      nonHeapInit = mb.getNonHeapMemoryUsage.getInit,
      nonHeapMax = mb.getNonHeapMemoryUsage.getMax,
      nonHeapCommited = mb.getNonHeapMemoryUsage.getCommitted,

      threadCount = tb.getThreadCount,
      daemons = tb.getDaemonThreadCount
    )
  }
}
