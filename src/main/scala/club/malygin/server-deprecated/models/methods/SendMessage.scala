package club.malygin.server.models.methods

import club.malygin.server.json.Request
import club.malygin.server.models.main_types.Message

case class SendMessage(
    chat_id: Int,
    text: String,
    disable_web_page_preview: Option[Boolean] = None,
    disable_notification: Option[Boolean] = None
)
