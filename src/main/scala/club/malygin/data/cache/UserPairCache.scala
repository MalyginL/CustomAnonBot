package club.malygin.data.cache

import club.malygin.data.cache.model.CacheStatistic

import scala.concurrent.Future

trait UserPairCache[A, B] {

  /** Returns the future associated with `key` in this cache, obtaining that value from
    * `loader` if necessary. If the asynchronous computation fails, the entry
    * will be automatically removed.
    * */
  def loadFromCache(key: A): Future[B]

  /** Returns the class with params
    * averageLoadPenalty : Double - the average time spent loading new values
    * hitRate : Double - returns the ratio of hits to requests
    * evictionCount : Long - the number of cache evictions
    * estimatedSize : Long - estimated size of cache
    * */
  def loadStatistic: CacheStatistic

  /** Associates `value` with `key` in this cache. If the cache previously contained a
    * value associated with `key`, the old value is replaced by `value`. If the
    * asynchronous computation fails, the entry will be automatically removed.
    */
  def addToCache(key: A, value: B): Unit

  def getCurrentCache
}
