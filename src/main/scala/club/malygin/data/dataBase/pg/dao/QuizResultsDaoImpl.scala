package club.malygin.data.dataBase.pg.dao

import java.util.UUID

import club.malygin.data.dataBase.pg.model.QuizResults
import club.malygin.data.dataBase.pg.Schema

import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

class QuizResultsDaoImpl extends QuizResultsDao {

  import Schema.results
  import Schema.sqldb
  import Schema.profile.api._


  override def saveOrUpdate(quizResults: QuizResults): Future[Unit] = {
    find(quizResults.quizId, quizResults.userId).andThen {
      case Success(res) => update(res,quizResults.result)
      case Failure(_) => save(quizResults)
    }.map(_=>())
  }

  override def save(quizResults: QuizResults): Future[Unit] = {
    sqldb.run(results += quizResults).map(_ => ())
  }

  override def update(quizResults: QuizResults, answer: Boolean): Future[Unit] = {
    sqldb.run(results.filter(_.id === quizResults.id).map(_.result).update(answer)).map(_ => ())
  }

  override def find(quizId: UUID, userId: Long): Future[QuizResults] = {
    sqldb.run(results.filter(_.quizId === quizId).filter(_.userId === userId).take(1).result.head)
  }
}
