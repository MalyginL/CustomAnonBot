package club.malygin.data.cache

import com.github.blemale.scaffeine.{AsyncLoadingCache, LoadingCache, Scaffeine}
import com.typesafe.scalalogging.LazyLogging
import javax.inject.{Inject, Named, Singleton}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService, Future}
import scala.concurrent.duration._
import club.malygin.Config.ec

@Named
@Singleton
class UserPairCacheImpl @Inject()(cacheLoader: CacheLoader)(
    implicit executionContextExecutorService: ExecutionContextExecutorService
) extends UserPairCache[Long, Long]
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
      .buildAsyncFuture((i: Long) => cacheLoader.load(i))

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

  def getCurrentCache: collection.concurrent.Map[Long, Long] =
    cache.synchronous.asMap

  def deletePair(first: Long, second: Long): Unit = {
    cache.synchronous().invalidate(first)
    cache.synchronous().invalidate(second)
  }

}
