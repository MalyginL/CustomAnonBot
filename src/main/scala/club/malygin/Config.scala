package club.malygin

import com.typesafe.config.ConfigFactory

object Config {

  private val cf = ConfigFactory.load("telegram")

  final val token: String      = cf.getString("bot.token")
  final val apiBaseUrl: String = cf.getString("server.apiUrl")
  final val host: String       = cf.getString("server.host")
  final val port: Int          = cf.getInt("server.port")

  import slick.jdbc.PostgresProfile.api._
  val sqldb = Database.forConfig("db.postgres")
}
