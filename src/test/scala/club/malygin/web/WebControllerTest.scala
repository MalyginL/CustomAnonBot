package club.malygin.web

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import club.malygin.data.appStat.AppStatModel
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import club.malygin.data.cache.CacheStatModel


class WebControllerTest extends FlatSpec
  with Matchers
  with ScalatestRouteTest
  with MockFactory {

  val mockedService = stub[WebService]
  val routes: Route = new WebController(mockedService).routes
  (mockedService.statistic _).when().returns(CacheStatModel(1.0, 1.0, 1L, 1L))
  (mockedService.getAppInfo _).when().returns(AppStatModel(
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
  ))


  "get request /statistic/cache" should "return CacheStatModel" in {
    Get("/statistic/cache") ~> Route.seal(routes) ~> check {
      responseAs[String] shouldEqual """{"averageLoadPenalty":1.0,"hitRate":1.0,"evictionCount":1,"estimatedSize":1}"""
    }
  }

  "get request /statistic/app" should "return AppStatModel" in {
    Get("/statistic/app") ~> Route.seal(routes) ~> check {
      responseAs[String] shouldEqual
"""{"javaVendor":"test","javaVersion":"test","pid":111,"uptime":1,"heapInit":1,"heapUsed":1,"heapMax":1,"heapCommited":1,"nonHeapUsed":1,"nonHeapInit":1,"nonHeapMax":1,"nonHeapCommited":1,"threadCount":1,"daemons":1}"""
    }



  }


}
