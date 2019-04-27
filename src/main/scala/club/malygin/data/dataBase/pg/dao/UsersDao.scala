package club.malygin.data.dataBase.pg.dao

import java.util.UUID

import club.malygin.data.dataBase.pg.model.Users

import scala.concurrent.Future

trait UsersDao {

  /** find user with opposite answer */
  def find(user: Long, quizId: UUID): Future[Long]

  /**setting 2 people into chat (updating fields)*/
  def setPair(init: Long, companion: Long, quiz: UUID): Future[Unit]

  /**combining `find` and `setPair` in one transaction*/
  def findAndPairUsersTransactionally(init: Long, quiz: UUID): Future[Long]

  /**saving or updating user*/
  def saveOrUpdate(user: Users): Future[Unit]

  /**finds current opponent in chat*/
  def getCompanion(userId: Long): Future[Option[Long]]

  /**clears chat pairs. Used to update status to `NOT SEARCHING` on chat close*/
  def clearPair(firstId: Long, secondId: Long): Future[Unit]

  /**Update status to `SEARCHING`. This status is used to activate the search feature. */
  def updateStatusToActive(userId: Long, quiz: UUID): Future[Unit]

  /**Adds user*/
  def add(user: Users): Future[Unit]

}
