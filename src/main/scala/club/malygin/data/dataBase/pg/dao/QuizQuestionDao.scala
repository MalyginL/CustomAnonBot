package club.malygin.data.dataBase.pg.dao

import club.malygin.data.dataBase.pg.model.QuizQuestions

import scala.concurrent.Future

trait QuizQuestionDao {

  def getActiveWithAnswer(id: Long): Future[Seq[QuizQuestions]]

  def getActive: Future[Seq[QuizQuestions]]

  def getAll: Future[Seq[QuizQuestions]]

  def add(quizQuestion: QuizQuestions): Future[Unit]
}
