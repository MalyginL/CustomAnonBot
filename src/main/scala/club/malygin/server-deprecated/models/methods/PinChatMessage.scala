package club.malygin.server.models.methods

case class PinChatMessage(
                           chat_id: Int,
                           message_id: Int,
                           disable_notification: Option[Boolean] = None
                         )


