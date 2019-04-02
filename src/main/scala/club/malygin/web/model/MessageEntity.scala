package club.malygin.web.model

case class MessageEntity(
    `type`: String,
    offset: Int,
    length: Int,
    url: Option[String] = None,
    user: Option[User] = None
)
