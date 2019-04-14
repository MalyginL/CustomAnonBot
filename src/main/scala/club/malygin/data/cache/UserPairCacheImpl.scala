package club.malygin.data.cache

import com.github.benmanes.caffeine.cache.CacheLoader
import com.github.blemale.scaffeine.{AsyncLoadingCache, Scaffeine}
import com.typesafe.scalalogging.LazyLogging
import javax.inject.{Inject, Named, Singleton}

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

@Named
@Singleton
class UserPairCacheImpl @Inject()(cacheLoader: CacheLoader[Long, Long])
    extends UserPairCache[Long, Long]
    with LazyLogging {

  /**
    * Can't use writer(cacheWriter) due these two features are incompatible
    **/
  private val maxSize = 500
  private val expire  = 3.hour

  final private val cache: AsyncLoadingCache[Long, Long] =
    Scaffeine()
      .recordStats()
      .expireAfterAccess(expire)
      .maximumSize(maxSize)
      .buildAsync(e => cacheLoader.load(e))

  logger.info(s"Cache initialize with $maxSize pool and $expire expire time")

  def loadFromCache(user: Long): Future[Long] = cache.get(user)

  def loadStatistic: CacheStatModel = {

    /** Modifications made to the  synchronous cache directly affect the asynchronous cache.
      * If a modification is made to a
      * mapping that is currently loading, the operation blocks until the computation completes.
      * Don't use synchronous to add elements!
      */
    val syncCache = cache.synchronous

    CacheStatModel(
      syncCache.stats.averageLoadPenalty,
      syncCache.stats.hitRate,
      syncCache.stats.evictionCount,
      syncCache.estimatedSize
    )
  }

  def addToCache(key: Long, value: Long): Unit =
    cache.put(key, Future {
      value
    }(ExecutionContext.global))

//  addToCache(229087075L, 829491453L)
 // addToCache(829491453L, 229087075L)

  def getCurrentCache: collection.concurrent.Map[Long, Long] =
    cache.synchronous.asMap
}
