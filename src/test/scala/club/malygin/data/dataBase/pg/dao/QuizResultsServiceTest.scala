package club.malygin.data.dataBase.pg.dao

import java.util.UUID
import java.util.concurrent.Executors

import club.malygin.TestConfig
import club.malygin.data.dataBase.pg.model.QuizResults
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.ExecutionContext


class QuizResultsServiceTest extends FlatSpec with Matchers with MockFactory with ScalaFutures with BeforeAndAfterAll {
  implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))

  import slick.jdbc.PostgresProfile.api._

  private val testdb = TestConfig.testdb
  private val resultService = new QuizResultsService(testdb)


  "save & find" should "work properly" in {
    val sampleUUID = UUID.randomUUID()
    val sample = QuizResults(UUID.randomUUID(), 1L, sampleUUID, result = true)
    resultService.save(sample)
    whenReady(resultService.find(sampleUUID, 1L)) {
      _.id shouldEqual sample.id
    }
  }

  "saveOrUpdate" should "change entity" in {
    val sampleUUID = UUID.randomUUID()
    val sample = QuizResults(sampleUUID, 1L, sampleUUID, result = true)
    val sampleChanged = QuizResults(sampleUUID, 1L, sampleUUID, result = false)
    resultService.save(sample)
    resultService.saveOrUpdate(sampleChanged)
    whenReady(resultService.find(sampleUUID, 1L)) { res =>
      res.id == sample.id && !res.result shouldBe false
    }
  }

  override def afterAll(): Unit ={
    testdb.close()
  }
}
