package club.malygin.server.models

case class ResponseParameters(migrateToChatId: Option[Long] = None, retryAfter: Option[Int] = None)
