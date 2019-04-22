package club.malygin.data.dataBase.pg.dao

import java.util.UUID

import club.malygin.data.dataBase.pg.Schema
import club.malygin.data.dataBase.pg.model.Users

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class UsersService extends UsersDao {

  import Schema.users
  import Schema.results
  import Schema.sqldb
  import Schema.profile.api._

  override def saveOrUpdate(user: Users): Future[Unit] = sqldb.run(users.insertOrUpdate(user)).map(_ => ())

  def find(user: Long, quizId: UUID): Future[Long] = {

    /**
      * status 0 - searching
      * status -1 - not searching
      *
      **/
    val q = for {
      u <- users
      r <- results
      if u.userId === r.userId &&
        r.userId =!= user &&
        u.searching_for === quizId &&
        r.quizId === quizId &&
        u.status === 0L &&
        !(r.result in results.filter(_.quizId === quizId).filter(_.userId === user).take(1).map(_.result))
    } yield u.userId
    sqldb.run(q.take(1).result.head)
  }

  def setPair(init: Long, companion: Long, quiz: UUID): Future[Unit] = {

    val first = users
      .filter(_.userId === init)
      .map(c => (c.status, c.searching_for))
      .update((Some(companion), Some(quiz)))

    val second = users
      .filter(_.userId === companion)
      .map(_.status)
      .update(Some(init))
    sqldb
      .run(
        DBIO
          .seq(
            first,
            second
          )
          .transactionally
      )
      .map(_ => ())
  }

  def clearPair(firstId: Long, secondId: Long): Future[Unit] = {

    val first = users
      .filter(_.userId === firstId)
      .map(_.status)
      .update(Some(-1L))

    val second = users
      .filter(_.userId === secondId)
      .map(_.status)
      .update(Some(-1L))

    sqldb
      .run(
        DBIO
          .seq(
            first,
            second
          )
          .transactionally
      )
      .map(_ => ())

  }

  def updateStatusToActive(userId: Long, quiz: UUID): Future[Unit] = {
    val q = users
      .filter(_.userId === userId)
      .map(c => (c.status, c.searching_for))
      .update((Some(0), Some(quiz)))
    sqldb.run(q).map(_ => ())
  }

  def getCompanion(userId: Long): Future[Option[Long]] = {
    val q = users.filter(_.userId === userId).map(_.status)
    sqldb.run(q.take(1).result.head)
  }

}

object UsersService extends UsersService
