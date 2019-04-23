package club.malygin.data.dataBase.pg.dao

import java.util.UUID

import club.malygin.data.dataBase.pg.model.Users

import scala.concurrent.Future

trait UsersDao {

  def find(user: Long, quizId: UUID): Future[Long]

  def setPair(init: Long, companion: Long, quiz: UUID): Future[Unit]

  def saveOrUpdate(user: Users): Future[Unit]

  def getCompanion(userId: Long): Future[Option[Long]]

  def clearPair(firstId: Long, secondId: Long): Future[Unit]

  def updateStatusToActive(userId: Long, quiz: UUID): Future[Unit]

  def add(user: Users): Future[Unit]

}
