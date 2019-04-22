package club.malygin.data.cache

import club.malygin.data.dataBase.pg.dao.UsersService
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CacheLoaderTest extends FlatSpec with Matchers  with MockFactory {

  val loader = new DatabaseCacheLoader
  val mockedService = stub[UsersService]

  (mockedService.getCompanion _).when(0).returns(Future{Some(1)})
  (mockedService.getCompanion _).when(1).returns(Future{None})

  "cacheLoader" should "return value if it contains in db" in{
    loader.load(0).map(res => res shouldBe Some(1))
  }
  "cacheLoader" should "return -1 if value doest" in{
    loader.load(0).map(res => res shouldBe Some(-1))
  }





}