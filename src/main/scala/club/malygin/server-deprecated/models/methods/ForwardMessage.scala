package club.malygin.server.models.methods

case class ForwardMessage(
    chat_id: Int,
    from_chat_id: Int,
    disable_notification: Option[Boolean] = None,
    message_id: Int
)
