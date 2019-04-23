package club.malygin.data.dataBase.pg.dao

import java.util.UUID
import java.util.concurrent.Executors

import club.malygin.TestConfig
import club.malygin.data.dataBase.pg.model.{QuizResults, Users}
import org.joda.time.DateTime
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.ExecutionContext

class UsersServiceTest extends FlatSpec with Matchers with MockFactory with ScalaFutures with BeforeAndAfterAll {

  implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))

  private val testdb = TestConfig.testdb
  val userService = new UsersService(testdb)
  val resultsService = new QuizResultsService(testdb)


    "find" should "return id of companion" in {

      val quizUUID = UUID.randomUUID()
      val firstUserID = System.currentTimeMillis()
      val secondUserID = System.currentTimeMillis() / 10

      val firstUser = userService.add(Users(firstUserID, "testerfdg", status = Some(0), searching_for = Some(quizUUID)))
      val secondUser = userService.add(Users(secondUserID, "tester", status = Some(0), searching_for = Some(quizUUID)))
      val firstResult = resultsService.save(QuizResults(UUID.randomUUID(), firstUserID, quizUUID, true))
      val secomdResult = resultsService.save(QuizResults(UUID.randomUUID(), secondUserID, quizUUID, false))

      Thread.sleep(1000)

      whenReady(userService.find(firstUserID, quizUUID)) {
        res => res == secondUserID shouldBe true
      }
    }

    "pair logic" should "work properly" in {
      val quizUUID = UUID.randomUUID()
      val firstUserID = System.currentTimeMillis()
      val secondUserID = System.currentTimeMillis() / 10

      val firstUser = userService.add(Users(firstUserID, "testerfdg", status = Some(0), searching_for = Some(quizUUID)))
      val secondUser = userService.add(Users(secondUserID, "tester", status = Some(0), searching_for = Some(quizUUID)))

      userService.setPair(firstUserID, secondUserID, quizUUID)

      userService.getCompanion(firstUserID).map { res =>
        res shouldEqual Some(secondUserID)
      }
      userService.getCompanion(secondUserID).map { res =>
        res shouldEqual Some(firstUserID)
      }

      userService.clearPair(firstUserID, secondUserID)

      userService.getCompanion(firstUserID).map { res =>
        res shouldEqual None
      }
      userService.getCompanion(secondUserID).map { res =>
        res shouldEqual None
      }

    }

  override def afterAll(): Unit ={
    testdb.close()
  }


}
