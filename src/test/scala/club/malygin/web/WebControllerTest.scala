package club.malygin.web

import java.util.concurrent.Executors

import akka.http.scaladsl.model.StatusCodes.BadRequest
import akka.http.scaladsl.model.{HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import club.malygin.data.appStat.AppStatModel
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import club.malygin.data.cache.CacheStatModel
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.ExecutionContext

class WebControllerTest extends FlatSpec with Matchers with ScalatestRouteTest with MockFactory with ScalaFutures {
  implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))
  val mockedService = stub[WebService]
  val routes: Route = new WebController(mockedService).routes
  (mockedService.statistic _).when().returns(CacheStatModel(1.0, 1.0, 1L, 1L))

  (mockedService.getAppInfo _)
    .when()
    .returns(
      AppStatModel(
        javaVendor = "test",
        javaVersion = "test",
        pid = 111,
        uptime = 1L,
        heapInit = 1L,
        nonHeapCommited = 1L,
        heapCommited = 1L,
        heapMax = 1L,
        heapUsed = 1L,
        nonHeapInit = 1L,
        nonHeapMax = 1L,
        nonHeapUsed = 1L,
        threadCount = 1,
        daemons = 1
      )
    )

  val testMap: collection.concurrent.Map[Long, Long] =
    scala.collection.concurrent.TrieMap(2233L -> 3322L)
  (mockedService.currentPairs _).when().returns(testMap)

  "routes" should "work" in {
    Get("/statistic/cache") ~> routes ~> check {
      responseAs[String] shouldEqual """{"averageLoadPenalty":1.0,"hitRate":1.0,"evictionCount":1,"estimatedSize":1}"""
    }

    Get("/statistic/app") ~> routes ~> check {
      responseAs[String] shouldEqual
        """{"javaVendor":"test","javaVersion":"test","pid":111,"uptime":1,"heapInit":1,"heapUsed":1,"heapMax":1,"heapCommited":1,"nonHeapUsed":1,"nonHeapInit":1,"nonHeapMax":1,"nonHeapCommited":1,"threadCount":1,"daemons":1}"""
    }
    Get("/api/cache/current") ~> routes ~> check {
      responseAs[String] shouldEqual """{"2233":3322}"""
    }
    Get("/api/cache/current") ~> routes ~> check {
      responseAs[String] shouldEqual """{"2233":3322}"""
    }

    import akka.http.scaladsl.model.ContentTypes._
    val entity = HttpEntity(`application/json`, """{"update_id":1}""")

    Post("/telegram", entity) ~> routes ~> check {
      responseAs[HttpResponse].status shouldBe StatusCodes.OK
    }

    val failentity = HttpEntity(`application/json`, """{"update_iddd":1}""")
    Post("/telegram", failentity) ~> routes ~> check {
      status shouldBe BadRequest
    }

  }

}
