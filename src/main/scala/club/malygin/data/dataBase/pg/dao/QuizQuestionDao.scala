package club.malygin.data.dataBase.pg.dao

import club.malygin.data.dataBase.pg.model.QuizQuestions

import scala.concurrent.Future

trait QuizQuestionDao {

  def getActive: Future[Seq[QuizQuestions]]

  def getAll: Future[Seq[QuizQuestions]]

}
