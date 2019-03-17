package club.malygin.data.appStat

case class AppStatModel(
                         javaVendor: String,
                         javaVersion: String,
                         pid: Long,
                         uptime: Long,
                         heapInit: Long,
                         heapMax: Long,
                         nonHeapInit: Long,
                         nonHeapMax: Long,
                         threadCount: Int
                       )
