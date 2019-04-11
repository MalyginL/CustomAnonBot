package club.malygin.data.pg
import java.util.UUID

import club.malygin.data.pg.model.QuizQuestions

import scala.concurrent.Future
import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape

object Schema {
  implicit val uuidMapper: BaseColumnType[String] = MappedColumnType.base[String, UUID](UUID.fromString, _.toString)

  class Questions(tag: Tag) extends Table[QuizQuestions](tag, "quiz_questions") {
    def id: Rep[UUID] = column("quiz_id", O.PrimaryKey)
    def text: Rep[String] = column("text")
    def firstOption: Rep[String] = column("first_option")
    def secondOption: Rep[String] = column("second_option")

    override def * : ProvenShape[QuizQuestions] = (id, text, firstOption,secondOption) <> (QuizQuestions.tupled, QuizQuestions.unapply)
  }
}
