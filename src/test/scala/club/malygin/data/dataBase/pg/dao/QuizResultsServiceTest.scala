package club.malygin.data.dataBase.pg.dao

import java.util.UUID
import java.util.concurrent.Executors

import club.malygin.TestConfig
import club.malygin.data.dataBase.pg.model.QuizResults
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, FlatSpec, Matchers}
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.ExecutionContext

class QuizResultsServiceTest extends FlatSpec with Matchers with MockFactory with ScalaFutures with BeforeAndAfterAll  with BeforeAndAfterEach{
  implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))

  private val testdb = TestConfig.testdb
  private val resultService = new QuizResultsService(testdb)


  override def beforeEach(): Unit = {
    Thread.sleep(5000)
  }

  "save & find" should "work properly" in {
    val sampleUUID = UUID.randomUUID()
    val sample = QuizResults(UUID.randomUUID(), 1L, sampleUUID, result = true)
    resultService.save(sample).andThen {
      case _ =>

        whenReady(resultService.find(sampleUUID, 1L)) {
          _.id shouldEqual sample.id
        }
    }
  }

  "saveOrUpdate" should "change entity" in {
    val sampleUUID = UUID.randomUUID()
    val sample = QuizResults(sampleUUID, 1L, sampleUUID, result = true)
    val sampleChanged = QuizResults(sampleUUID, 1L, sampleUUID, result = false)
    resultService.save(sample).andThen { case _ => resultService.saveOrUpdate(sampleChanged) }.andThen { case _ =>
      whenReady(resultService.find(sampleUUID, 1L)) { res =>
        res.id == sample.id && !res.result shouldBe false
      }
    }
  }
  override def afterAll() {
    Thread.sleep(10000)
    testdb.close()
  }

}
