package club.malygin.data.cache
import club.malygin.data.dataBase.pg.dao.UsersService
import javax.inject.{Inject, Named}

import scala.concurrent.{ExecutionContextExecutorService, Future}

trait CacheLoader extends {

  def load(key: Long): Future[Long]

}

@Named
class DatabaseCacheLoader@Inject()(implicit context: ExecutionContextExecutorService) extends CacheLoader {

  override def load(key: Long): Future[Long] =
    UsersService.getCompanion(key).map {
      case Some(res) => res
      case None      => -1L
    }
}
