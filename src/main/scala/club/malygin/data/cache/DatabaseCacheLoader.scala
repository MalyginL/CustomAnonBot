package club.malygin.data.cache
import com.github.benmanes.caffeine.cache.CacheLoader
import javax.inject.Named

@Named
class DatabaseCacheLoader extends CacheLoader[Long, Long] {

  override def load(key: Long): Long = ???
}
