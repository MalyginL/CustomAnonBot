package club.malygin.server.models.methods

case class PromoteChatMember (
                               chat_id:Int,
                               user_id:Int,
                               can_change_info:Option[Boolean]=None,
                               can_post_messages:Option[Boolean]=None,
                               can_edit_messages:Option[Boolean]=None,
                               can_delete_messages:Option[Boolean]=None,
                               can_invite_users:Option[Boolean]=None,
                               can_restrict_members:Option[Boolean]=None,
                               can_pin_messages:Option[Boolean]=None,
                               can_promote_members:Option[Boolean]=None,
                             )


