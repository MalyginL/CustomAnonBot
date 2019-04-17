package club.malygin.data.dataBase.pg.dao

import java.util.UUID

import club.malygin.data.dataBase.pg.Schema
import club.malygin.data.dataBase.pg.model.Users

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class UsersDaoImpl extends UsersDao {

  import Schema.users
  import Schema.results
  import Schema.sqldb
  import Schema.profile.api._

  override def saveOrUpdate(user: Users): Future[Unit] = sqldb.run(users.insertOrUpdate(user)).map(_ => ())

  def find(user: Long, quizId: UUID): Future[Long] = {

    val q = for {
      u <- users
      r <- results
      if u.userId === r.userId &&
        r.userId =!= user &&
        u.searching_for === quizId &&
        r.quizId === quizId &&
        !(r.result in results.filter(_.quizId === quizId).filter(_.userId === user).take(1).map(_.result))
    } yield
      u.userId

    sqldb.run(q.take(1).result.head)
  }

}
