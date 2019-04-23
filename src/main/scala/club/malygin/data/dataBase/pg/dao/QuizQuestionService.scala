package club.malygin.data.dataBase.pg.dao

import java.util.concurrent.Executors

import club.malygin.{Application, Config}
import club.malygin.data.dataBase.pg.model.QuizQuestions
import club.malygin.data.dataBase.pg.Schema
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}

class QuizQuestionService(sqldb: Database)(implicit context: ExecutionContext) extends QuizQuestionDao {

  import Schema.questions
  import Schema.results
  implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))
  import Schema.profile.api._

  def getActiveWithAnswer(id: Long): Future[Seq[QuizQuestions]] = {
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

object QuizQuestionService extends QuizQuestionService(Config.sqldb)(Application.ec)
