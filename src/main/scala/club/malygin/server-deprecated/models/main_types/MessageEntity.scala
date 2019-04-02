package club.malygin.server.models.main_types

case class MessageEntity(
    `type`: String,
    offset: Int,
    length: Int,
    url: Option[String] = None,
    user: Option[User] = None
)
