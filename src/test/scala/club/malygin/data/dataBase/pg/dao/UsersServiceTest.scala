package club.malygin.data.dataBase.pg.dao

import java.util.UUID
import java.util.concurrent.Executors

import club.malygin.TestConfig
import club.malygin.data.dataBase.pg.model.{QuizResults, Users}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, FlatSpec, Matchers}
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.ExecutionContext

class UsersServiceTest
    extends FlatSpec
    with Matchers
    with MockFactory
    with ScalaFutures
    with BeforeAndAfterAll
    with BeforeAndAfterEach {

  implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(11))

  private val testdb = TestConfig.testdb
  val userService    = new UsersService(testdb)
  val resultsService = new QuizResultsService(testdb)

  private val quizUUID     = UUID.randomUUID()
  private val firstUserID  = System.currentTimeMillis()
  private val secondUserID = System.currentTimeMillis() / 10

  override def beforeAll(): Unit = {
    userService.add(Users(firstUserID, "testerfdg", status = Some(0), searching_for = Some(quizUUID)))
    userService.add(Users(secondUserID, "tester", status = Some(0), searching_for = Some(quizUUID)))
    resultsService.save(QuizResults(UUID.randomUUID(), firstUserID, quizUUID, true))
    resultsService.save(QuizResults(UUID.randomUUID(), secondUserID, quizUUID, false))
  }

  override def beforeEach(): Unit =
    Thread.sleep(5000)

  "find" should "return id of companion" in {
    whenReady(userService.find(firstUserID, quizUUID)) { res =>
      res == secondUserID shouldBe true
    }
  }

  "pair set" should "work properly" in {
    userService.setPair(firstUserID, secondUserID, quizUUID).andThen {
      case _ =>
        whenReady(userService.getCompanion(firstUserID)) { res =>
          res shouldEqual Some(secondUserID)
        }
        whenReady(userService.getCompanion(secondUserID)) { res =>
          res shouldEqual Some(firstUserID)
        }
    }
  }

  "pair delete" should "work properly" in {
    userService.clearPair(firstUserID, secondUserID).andThen {
      case _ =>
        whenReady(userService.getCompanion(firstUserID)) { res =>
          res shouldEqual Some(-1)
        }
        whenReady(userService.getCompanion(secondUserID)) { res =>
          res shouldEqual Some(-1)
        }
    }
  }

  override def afterAll() {
    Thread.sleep(10000)
    testdb.close()
  }

}
