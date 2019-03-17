package club.malygin.data.cache.model

case class CacheStatistic(averageLoadPenalty: Double, hitRate: Double, evictionCount: Long, estimatedSize: Long)
