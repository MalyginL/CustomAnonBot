package club.malygin.data.cache

import java.util.concurrent.Executors

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

import scala.concurrent.{ExecutionContext, Future}

class UserPairCacheImplTest extends FlatSpec with Matchers with BeforeAndAfter with ScalaFutures {
  implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))
  private val cacheLoader = new CacheLoader {
    override def load(key: Long): Future[Long] = Future(key + 333)
  }

  "cache" should "return stored value" in {
    val cache = new UserPairCacheImpl(cacheLoader)
    cache.addToCache(1, 2)
    whenReady(cache.loadFromCache(1)){_ shouldBe 2L}
  }

  "cache" should "should ask cacheLoader if key/value pair is not contained" in {
    val cache = new UserPairCacheImpl(cacheLoader)
    whenReady(cache.loadFromCache(1)){_ shouldBe 334L}
  }

  "cache" should "should return statistic" in {
    val cache = new UserPairCacheImpl(cacheLoader)

    def stat = cache.loadStatistic

    stat shouldBe a[CacheStatModel]
    stat.estimatedSize shouldBe 0
    cache.addToCache(1, 2)
    stat.estimatedSize shouldBe 1
    stat.averageLoadPenalty >= 0 shouldBe true
    stat.hitRate >= 0 shouldBe true
    stat.evictionCount >= 0 shouldBe true
  }

  "cache" should "return Map with values" in {
    val cache = new UserPairCacheImpl(cacheLoader)
    cache.addToCache(1, 1)
    cache.addToCache(2, 2)
    val map = cache.getCurrentCache
    map.contains(1) && map.apply(1) == 1L shouldBe true
  }

  "cache" should "delete values properly" in {
    val cache = new UserPairCacheImpl(cacheLoader)
    cache.addToCache(1, 2)
    cache.addToCache(2, 1)
    cache.deletePair(1, 2)
    cache.loadFromCache(1).map(_ shouldBe 334L)
    cache.loadFromCache(2).map(_ shouldBe 335L)
  }

}
