package club.malygin

import club.malygin.server.Server

object Application extends App {
  override def main(args: Array[String]): Unit = {
    new Server()
  }
}
