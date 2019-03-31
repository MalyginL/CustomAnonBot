package club.malygin.server.models.methods

case class SendMediaGroup(
                           chat_id: Int,
                           media: Array[String], //FIX ME
                           disable_notification: Option[Boolean] = None,
                           reply_to_message_id: Option[Int] = None
                         )
