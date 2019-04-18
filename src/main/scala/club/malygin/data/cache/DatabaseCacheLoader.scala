package club.malygin.data.cache
import club.malygin.data.dataBase.pg.dao.UsersDaoImpl
import com.github.benmanes.caffeine.cache.CacheLoader
import javax.inject.Named
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Named
class DatabaseCacheLoader extends CacheLoader[Long, Long] {

  val u = new UsersDaoImpl

  override def load(key: Long): Long =

    u.getCompanion(key).value.get.get.get

}
