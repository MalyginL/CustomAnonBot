package club.malygin.data.dataBase.pg.dao

import club.malygin.Config
import club.malygin.data.dataBase.pg.model.QuizQuestions
import club.malygin.data.dataBase.pg.Schema
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

class QuizQuestionService(sqldb: Database) extends QuizQuestionDao {

  import Schema.questions
  import Schema.results

  import Schema.profile.api._

  def getCurrentwithAnswer(id: Long): Future[Seq[QuizQuestions]] = {
    val q = for {
      q <- questions
      r <- results
      if q.id === r.quizId &&
        r.userId === id &&
        q.status === true
    } yield q
    sqldb.run(q.result)
  }

  override def getActive: Future[Seq[QuizQuestions]] =
    sqldb.run(questions.filter(_.status === true).result)

  override def getAll: Future[Seq[QuizQuestions]] = sqldb.run(questions.result)

  override def add(quizQuestions: QuizQuestions): Future[Unit] =
    sqldb.run(questions += quizQuestions).map(_ => ())
}

object QuizQuestionService extends QuizQuestionService(Config.sqldb)
