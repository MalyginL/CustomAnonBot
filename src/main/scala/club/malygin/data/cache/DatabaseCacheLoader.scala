package club.malygin.data.cache
import club.malygin.data.dataBase.pg.dao.UsersService

import javax.inject.Named
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait CacheLoader {

  def load(key: Long): Future[Long]

}

@Named
class DatabaseCacheLoader extends CacheLoader {

  override def load(key: Long): Future[Long] =
    UsersService.getCompanion(key).map {
      case Some(res) => res
      case None      => -1L
    }
}
