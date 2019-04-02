package club.malygin.server.models.main_types

case class ChatMember(
    user: User,
    status: String,
    until_date: Option[Int] = None,
    can_be_edited: Option[Boolean] = None,
    can_change_info: Option[Boolean] = None,
    can_post_messages: Option[Boolean] = None,
    can_edit_messages: Option[Boolean] = None,
    can_delete_messages: Option[Boolean] = None,
    can_invite_users: Option[Boolean] = None,
    can_restrict_members: Option[Boolean] = None,
    can_pin_messages: Option[Boolean] = None,
    can_promote_members: Option[Boolean] = None,
    can_send_messages: Option[Boolean] = None,
    can_send_media_messages: Option[Boolean] = None,
    can_send_other_messages: Option[Boolean] = None,
    can_add_web_page_previews: Option[Boolean] = None
)
