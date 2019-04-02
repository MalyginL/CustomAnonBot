package club.malygin.data.cache

case class CacheStatModel(averageLoadPenalty: Double, hitRate: Double, evictionCount: Long, estimatedSize: Long)
