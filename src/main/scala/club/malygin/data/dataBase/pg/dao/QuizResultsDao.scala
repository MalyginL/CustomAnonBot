package club.malygin.data.dataBase.pg.dao

import java.util.UUID

import club.malygin.data.dataBase.pg.model.QuizResults

import scala.concurrent.Future

trait QuizResultsDao {

  def find(quizId:UUID,userId:Long):Future[QuizResults]

  def saveOrUpdate(quizResults: QuizResults): Future[Unit]

  def save(quizResults: QuizResults):Future[Unit]

  def update(quizResults: QuizResults, answer: Boolean):Future[Unit]


}
