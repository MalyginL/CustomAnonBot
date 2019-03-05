package club.malygin.server.models.methods

case class KickChatMember (
                            chat_id:Int,
                            user_id:Int,
                            until_date:Option[Int]=None
                          )


