package club.malygin.data.dataBase.pg.dao

import java.util.UUID
import java.util.concurrent.Executors

import club.malygin.{Application, TestConfig}
import club.malygin.data.dataBase.pg.model.{QuizQuestions, QuizResults}
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, FlatSpec, Matchers}

import scala.concurrent.ExecutionContext
class QuizQuestionServiceTest
    extends FlatSpec
    with Matchers
    with MockFactory
    with ScalaFutures
    with BeforeAndAfterAll
    with BeforeAndAfterEach {
  implicit val ec           = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))
  private val testdb        = TestConfig.testdb
  private val service       = new QuizQuestionService(testdb)
  private val resultService = new QuizResultsService(testdb)
  private val active        = UUID.randomUUID()
  private val notActive     = UUID.randomUUID()

  override def beforeAll(): Unit = {
    super.beforeAll()
    service.add(
      QuizQuestions(
        active,
        "testtrue",
        "1",
        "2",
        status = true
      )
    )
    service.add(
      QuizQuestions(
        notActive,
        "testfalse",
        "1",
        "2",
        status = false
      )
    )
    resultService.save(QuizResults(UUID.randomUUID(), 1L, active, result = true))
    resultService.save(QuizResults(UUID.randomUUID(), 2L, notActive, result = true))

  }

  override def beforeEach(): Unit =
    Thread.sleep(5000)

  "getActive" should "should return only active questions" in {
    whenReady(service.getActive) { res =>
      res.count(_.quizIdd == active) == 1 shouldBe true
    }
  }

  "getAll" should "should return all questions" in {
    whenReady(service.getAll) { res =>
      res.count(x => x.quizIdd == active || x.quizIdd == notActive) == 2 shouldBe true
    }
  }

  "getCurrentwithAnswer" should "return " in {
    whenReady(service.getActiveWithAnswer(1L)) { res =>
      res.count(_.quizIdd == active) shouldBe 1
    }
  }

  override def afterAll() {
    Thread.sleep(10000)
    testdb.close()
  }

}
