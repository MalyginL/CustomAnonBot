package club.malygin

import java.util.concurrent.Executors

import com.typesafe.config.ConfigFactory

import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService}

object Config {

   val ec: ExecutionContextExecutorService = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(50))

  private val cf = ConfigFactory.load("telegram")

  final val token: String      = cf.getString("bot.token")
  final val apiBaseUrl: String = cf.getString("server.apiUrl")
  final val host: String       = cf.getString("server.host")
  final val port: Int          = cf.getInt("server.port")

  import slick.jdbc.PostgresProfile.api._
  val sqldb = Database.forConfig("db.postgres")
}
