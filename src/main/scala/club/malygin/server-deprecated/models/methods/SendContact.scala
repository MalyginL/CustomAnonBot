package club.malygin.server.models.methods

case class SendContact(
    chat_id: Int,
    phone_number: String,
    first_name: String,
    last_name: Option[String] = None,
    vcard: Option[String] = None,
    disable_notification: Option[Boolean] = None,
    reply_to_message_id: Option[Int] = None,
    reply_markup: Option[String] = None //FIX
)
