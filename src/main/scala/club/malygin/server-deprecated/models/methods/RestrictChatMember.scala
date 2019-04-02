package club.malygin.server.models.methods

case class RestrictChatMember(
    chat_id: Int,
    user_id: Int,
    until_date: Option[Int] = None,
    can_send_messages: Option[Boolean] = None,
    can_send_media_messages: Option[Boolean] = None,
    can_send_other_messages: Option[Boolean] = None,
    can_add_web_page_previews: Option[Boolean] = None
)
