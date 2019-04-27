package club.malygin.data.dataBase.pg.dao

import club.malygin.data.dataBase.pg.model.QuizQuestions

import scala.concurrent.Future

trait QuizQuestionDao {

  /** returns seq of questions with `active` mark answered by the user with id. */
  def getActiveWithAnswer(id: Long): Future[Seq[QuizQuestions]]

  /**returns seq of questions marked as active*/
  def getActive: Future[Seq[QuizQuestions]]

/**returns seq of all questions, active and deprecated*/
  def getAll: Future[Seq[QuizQuestions]]

  /**adds new question to db*/
  def add(quizQuestion: QuizQuestions): Future[Unit]
}
