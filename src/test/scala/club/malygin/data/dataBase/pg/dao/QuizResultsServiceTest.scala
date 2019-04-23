package club.malygin.data.dataBase.pg.dao

import java.util.UUID
import java.util.concurrent.Executors

import club.malygin.data.dataBase.pg.model.QuizResults
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.ExecutionContext


class QuizResultsServiceTest extends FlatSpec with Matchers with MockFactory with ScalaFutures {
  implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(10))
  import slick.jdbc.PostgresProfile.api._

  private val testdb        = Database.forConfig("db.test")
  private val resultService = new QuizResultsService(testdb)


  "save & find" should "work properly" in {
    val sampleUUID = UUID.randomUUID()
    val sample     = QuizResults(UUID.randomUUID(), 1L, sampleUUID, result = true)
    for {
      _ <- resultService.save(sample)
      r <- resultService.find(sampleUUID, 1L)
    } yield r.id == sample.id shouldBe true
  }

  "saveOrUpdate" should "change entity" in {
    val sampleUUID    = UUID.randomUUID()
    val sample        = QuizResults(sampleUUID, 1L, sampleUUID, result = true)
    val sampleChanged = QuizResults(sampleUUID, 1L, sampleUUID, result = false)
    for {
      _ <- resultService.save(sample)
      _ <- resultService.saveOrUpdate(sampleChanged)
      r <- resultService.find(sampleUUID, 1L)
    } yield r.id == sample.id && !r.result shouldBe true
  }
}
