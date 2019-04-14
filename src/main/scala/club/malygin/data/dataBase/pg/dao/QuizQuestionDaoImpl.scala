package club.malygin.data.dataBase.pg.dao

import club.malygin.data.dataBase.pg.model.QuizQuestions
import club.malygin.data.dataBase.pg.Schema
import scala.concurrent.Future

class QuizQuestionDaoImpl extends QuizQuestionDao {

  import Schema.questions
  import Schema.sqldb
  import Schema.profile.api._

  override def getActive: Future[Seq[QuizQuestions]] = {
    sqldb.run(questions.filter(_.status === true).result)
  }

  override def getAll: Future[Seq[QuizQuestions]] = sqldb.run(questions.result)
}
