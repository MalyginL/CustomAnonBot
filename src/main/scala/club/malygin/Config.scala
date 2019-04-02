package club.malygin

import com.typesafe.config.ConfigFactory

object Config {

  private val cf = ConfigFactory.load("debug")

  final val token: String      = cf.getString("bot.token")
  final val apiBaseUrl: String = cf.getString("server.apiUrl")
  final val host: String       = cf.getString("server.host")
  final val port: Int          = cf.getInt("server.port")

}
