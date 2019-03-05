package club.malygin.server.models.methods


case class SetChatDescription(
                               chat_id:Int,
                               description:Option[String]=None
                             )

