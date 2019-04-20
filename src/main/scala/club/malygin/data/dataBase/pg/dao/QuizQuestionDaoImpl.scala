package club.malygin.data.dataBase.pg.dao

import club.malygin.data.dataBase.pg.model.QuizQuestions
import club.malygin.data.dataBase.pg.Schema
import scala.concurrent.Future

class QuizQuestionDaoImpl extends QuizQuestionDao {

  import Schema.questions
  import Schema.results
  import Schema.sqldb
  import Schema.profile.api._


 def getCurrentwithAnswer(id: Long): Future[Seq[QuizQuestions]] = {
    val q = for {
      q <- questions
      r <- results
      if q.id === r.quizId &&
        r.userId === id &&
        q.status === true
    } yield
      q
    sqldb.run(q.result)
  }

  override def getActive: Future[Seq[QuizQuestions]] =
    sqldb.run(questions.filter(_.status === true).result)


  override def getAll: Future[Seq[QuizQuestions]] = sqldb.run(questions.result)
}
