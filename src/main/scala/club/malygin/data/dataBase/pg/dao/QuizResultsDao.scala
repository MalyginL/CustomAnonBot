package club.malygin.data.dataBase.pg.dao

import java.util.UUID

import club.malygin.data.dataBase.pg.model.QuizResults

import scala.concurrent.Future

trait QuizResultsDao {

  /** finds answer */
  def find(quizId: UUID, userId: Long): Future[QuizResults]

  /** saving or updating  answer */
  def saveOrUpdate(quizResults: QuizResults): Future[Unit]

  /** saving  answer */
  def save(quizResults: QuizResults): Future[Unit]

  /** updating answer */
  def update(quizResults: QuizResults, answer: Boolean): Future[Unit]

}
