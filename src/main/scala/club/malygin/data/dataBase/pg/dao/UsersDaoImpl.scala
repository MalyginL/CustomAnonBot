package club.malygin.data.dataBase.pg.dao
import club.malygin.data.dataBase.pg.Schema
import club.malygin.data.dataBase.pg.model.Users

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class UsersDaoImpl extends UsersDao{

  import Schema.users
  import Schema.sqldb
  import Schema.profile.api._

  override def saveOrUpdate(user: Users): Future[Unit] =  sqldb.run(users.insertOrUpdate(user)).map(_ => ())
}
