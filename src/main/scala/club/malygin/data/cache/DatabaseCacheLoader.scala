package club.malygin.data.cache

import club.malygin.data.dataBase.pg.dao.{QuizQuestionDao, UsersDao, UsersService}
import javax.inject.{Inject, Named}

import scala.concurrent.{ExecutionContextExecutorService, Future}

trait CacheLoader extends {

  def load(key: Long): Future[Long]

}

@Named
class DatabaseCacheLoader @Inject()(userDao: UsersDao)(implicit context: ExecutionContextExecutorService) extends CacheLoader {

  override def load(key: Long): Future[Long] =
    try {
      userDao.getCompanion(key).map {
        case Some(res) => res
        case None => -1L
      }
    }
    catch {
      case ex: java.lang.NullPointerException => Future.successful(-1L)
    }


}
