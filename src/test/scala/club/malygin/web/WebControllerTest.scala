package club.malygin.web

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import club.malygin.data.cache.CacheStatModel
import club.malygin.Application._


class WebControllerTest extends FlatSpec
  with Matchers
  with ScalatestRouteTest
  with MockFactory {

  val mockedService = stub[WebService]
  val routes: Route = new WebController(mockedService).routes
  (mockedService.statistic _).when().returns(CacheStatModel(1L, 1L, 1L, 1L))

  "statRoutes" should "work" in {

    Get("statistic/cache") ~> routes ~> check {
      responseAs[String] shouldEqual """{"averageLoadPenalty":1,"hitRate":1,"evictionCount":1,"estimatedSize":1}"""
    }
  }


}
