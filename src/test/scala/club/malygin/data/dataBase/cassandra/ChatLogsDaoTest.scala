package club.malygin.data.dataBase.cassandra

import java.util.UUID
import java.util.concurrent.Executors

import scala.concurrent.{ExecutionContext, Future}
import com.outworkers.phantom.dsl._
import org.joda.time.DateTime
import org.scalatest.concurrent.ScalaFutures
import org.scalatest._
object TestDatabase extends ChatLogsDao(Connector.testconnector)

trait Provider extends DatabaseProvider[ChatLogsDao] {
  override def database: ChatLogsDao = TestDatabase
}

trait Testing
    extends FlatSpec
    with Matchers
    with Inspectors
    with ScalaFutures
    with OptionValues
    with BeforeAndAfterAll
    with Provider

class ChatLogsDaoTest extends Testing {
  implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))
  override def beforeAll(): Unit = {
    super.beforeAll()
    database.create()
  }

  "ChatLogsModel" should "be inserted and deleted into cassandra" in {
    val log = ChatLogsModel(
      UUID.randomUUID,
      1,
      1,
      "test",
      DateTime.now()
    )
    whenReady(this.store(log)) { res =>
      res.isExhausted() shouldBe true
      res.wasApplied() shouldBe true

      whenReady(this.drop(log)) { res =>
        res.isExhausted() shouldBe true
        res.wasApplied() shouldBe true
      }
    }
  }

  "List of ChatLogModel" should "received" in {
    val log = ChatLogsModel(
      UUID.randomUUID,
      2,
      1,
      "test",
      DateTime.now()
    )

    whenReady(this.store(log)) { _ =>
      whenReady(TestDatabase.getUserMessages(2)) { res =>
        res.size == 1 shouldBe true
        whenReady(this.drop(log)) { r =>
          r.isExhausted() shouldBe true
          r.wasApplied() shouldBe true
        }
      }
    }
  }

  private def store(model: ChatLogsModel): Future[ResultSet] = TestDatabase.save(model)

  private def drop(model: ChatLogsModel) = TestDatabase.delete(model)

}
