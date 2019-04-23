package club.malygin.data.dataBase.pg.dao

import java.util.UUID

import club.malygin.{Application, Config}
import club.malygin.data.dataBase.pg.Schema
import club.malygin.data.dataBase.pg.model.Users
import slick.jdbc.PostgresProfile.api._

import com.google.inject.Singleton
import javax.inject.Inject

import scala.concurrent.{ExecutionContextExecutorService, Future}

@Singleton
class UsersService @Inject()(sqldb: Database)(implicit executionContextExecutorService: ExecutionContextExecutorService)
    extends UsersDao {

  import Schema.users
  import Schema.results

  import Schema.profile.api._

  override def saveOrUpdate(user: Users): Future[Unit] = sqldb.run(users.insertOrUpdate(user)).map(_ => ())

  private final val NOTSEARCHING = -1L
  private final val SEARCHING    = 0L

  override def find(user: Long, quizId: UUID): Future[Long] = {
    val q = for {
      u <- users
      r <- results
      if u.userId === r.userId &&
        r.userId =!= user &&
        u.searchingFor === quizId &&
        r.quizId === quizId &&
        u.status === SEARCHING &&
        !(r.result in results.filter(_.quizId === quizId).filter(_.userId === user).take(1).map(_.result))
    } yield u.userId
    sqldb.run(q.take(1).result.head)
  }

  override def setPair(init: Long, companion: Long, quiz: UUID): Future[Unit] = {

    val first = users
      .filter(_.userId === init)
      .map(c => (c.status, c.searchingFor))
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

  override def clearPair(firstId: Long, secondId: Long): Future[Unit] = {

    val first = users
      .filter(_.userId === firstId)
      .map(_.status)
      .update(Some(NOTSEARCHING))

    val second = users
      .filter(_.userId === secondId)
      .map(_.status)
      .update(Some(NOTSEARCHING))

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

  override def updateStatusToActive(userId: Long, quiz: UUID): Future[Unit] = {
    val q = users
      .filter(_.userId === userId)
      .map(c => (c.status, c.searchingFor))
      .update((Some(SEARCHING), Some(quiz)))
    sqldb.run(q).map(_ => ())
  }

  override def getCompanion(userId: Long): Future[Option[Long]] = {
    val q = users.filter(_.userId === userId).map(_.status)
    sqldb.run(q.take(1).result.head)
  }

  override def add(user: Users): Future[Unit] = sqldb.run(users += user).map(_ => ())
}
