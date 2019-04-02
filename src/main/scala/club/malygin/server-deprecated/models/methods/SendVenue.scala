package club.malygin.server.models.methods

case class SendVenue(
    chat_id: Int,
    latitude: Float,
    longitude: Float,
    title: String,
    address: String,
    foursquare_id: Option[String] = None,
    foursquare_type: Option[String] = None,
    disable_notification: Option[Boolean] = None,
    reply_to_message_id: Option[Int] = None,
    reply_markup: Option[String] = None //FIX
)
