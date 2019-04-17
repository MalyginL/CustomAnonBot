package club.malygin.data.dataBase.pg.dao

import club.malygin.data.dataBase.pg.model.UserPairs

import scala.concurrent.Future

trait UserPairsDao {

  def find(id: Long) : Future[UserPairs]

  def insert(pairs: UserPairs) : Future[Unit]

}
