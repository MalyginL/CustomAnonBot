package club.malygin.data.appStat

case class AppStatModel(
    javaVendor: String,
    javaVersion: String,
    pid: Long,
    uptime: Long,
    heapInit: Long,
    heapUsed: Long,
    heapMax: Long,
    heapCommited: Long,
    nonHeapUsed: Long,
    nonHeapInit: Long,
    nonHeapMax: Long,
    nonHeapCommited: Long,
    threadCount: Int,
    daemons: Int
)
