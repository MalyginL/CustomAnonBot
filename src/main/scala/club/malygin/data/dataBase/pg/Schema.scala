package club.malygin.data.dataBase.pg

import java.sql.Timestamp
import java.util.UUID

import org.joda.time.DateTime
import club.malygin.data.dataBase.pg.model._
import slick.jdbc.JdbcProfile

import scala.concurrent.Future
import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape

object Schema {
  implicit val uuidMapper: BaseColumnType[String] = MappedColumnType.base[String, UUID](UUID.fromString, _.toString)

  implicit def dateTime =
    MappedColumnType.base[DateTime, Timestamp](
      dt => new Timestamp(dt.getMillis),
      ts => new DateTime(ts.getTime)
    )

  import slick.jdbc.PostgresProfile.api._
  import org.postgresql.Driver._

  class QuestionsTable(tag: Tag) extends Table[QuizQuestions](tag, "quiz_q") {

    def id: Rep[UUID] = column("quiz_id", O.PrimaryKey)

    def text: Rep[String] = column("text")

    def firstOption: Rep[String] = column("first_option")

    def secondOption: Rep[String] = column("second_option")

    def status: Rep[Boolean] = column("status")

    override def * : ProvenShape[QuizQuestions] =
      (id, text, firstOption, secondOption, status) <> (QuizQuestions.tupled, QuizQuestions.unapply)
  }

  val questions = TableQuery[QuestionsTable]

  class ResultsTable(tag: Tag) extends Table[QuizResults](tag, "quiz_r") {

    def id: Rep[UUID] = column("id", O.PrimaryKey)

    def userId: Rep[Long] = column("user_id")

    def quizId: Rep[UUID] = column("quiz_id")

    def result: Rep[Boolean] = column("result")

    override def * : ProvenShape[QuizResults] =
      (id, userId, quizId, result) <> (QuizResults.tupled, QuizResults.unapply)
  }

  val results = TableQuery[ResultsTable]

  class UsersTable(tag: Tag) extends Table[Users](tag, "users") {

    def userId: Rep[Long] = column("user_id", O.PrimaryKey)

    def firstName: Rep[String] = column("first_name")

    def lastName: Rep[Option[String]] = column("last_name")

    def username: Rep[Option[String]] = column("username")

    def status: Rep[Option[Long]] = column("status")

    def last_online: Rep[Option[DateTime]] = column("last_online")

    def searching_for: Rep[Option[UUID]] = column("searching_for")

    override def * : ProvenShape[Users] =
      (userId, firstName, lastName, username, status, last_online, searching_for) <> (Users.tupled, Users.unapply)

  }

  val users = TableQuery[UsersTable]

  val profile: JdbcProfile = slick.jdbc.PostgresProfile

}

