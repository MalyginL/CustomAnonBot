package club.malygin.data.cache

import java.util.concurrent.Executors
import club.malygin.data.dataBase.pg.dao.UsersService
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.{ExecutionContext, Future}

class CacheLoaderTest extends FlatSpec with Matchers with MockFactory with ScalaFutures {
  implicit val ec   = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))
  val mockedService = stub[UsersService]
  val loader        = new DatabaseCacheLoader(mockedService)

  (mockedService.getCompanion _).when(0).returns(Future { Some(1) })
  (mockedService.getCompanion _).when(1).returns(Future { None })

  "cacheLoader" should "return value if it is in db" in {

    whenReady(loader.load(0)) { res =>
      res shouldBe 1
    }
  }
  "cacheLoader" should "return -1 if value doest" in {
    whenReady(loader.load(0)) { res =>
      res shouldBe -1
    }
  }

}
