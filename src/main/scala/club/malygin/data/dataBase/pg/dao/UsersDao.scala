package club.malygin.data.dataBase.pg.dao

import club.malygin.data.dataBase.pg.model.Users

import scala.concurrent.Future

trait UsersDao {

  def saveOrUpdate(user: Users):Future[Unit]

}
