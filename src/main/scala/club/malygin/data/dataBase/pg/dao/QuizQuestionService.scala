package club.malygin.data.dataBase.pg.dao

import club.malygin.{Application, Config}
import club.malygin.data.dataBase.pg.model.QuizQuestions
import club.malygin.data.dataBase.pg.Schema
import slick.jdbc.PostgresProfile.api._
import com.google.inject.Singleton
import javax.inject.Inject

import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService, Future}
@Singleton
class QuizQuestionService @Inject()(sqldb: Database)(
    implicit executionContextExecutorService: ExecutionContextExecutorService
) extends QuizQuestionDao {

  import Schema.questions
  import Schema.results
  import Schema.profile.api._

  override def getActiveWithAnswer(id: Long): Future[Seq[QuizQuestions]] = {
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
