package club.malygin.server.models.methods

case class SendLocation(
                         chat_id: Int,
                         latitude: Float,
                         longitude: Float,
                         live_period: Option[Int] = None,
                         disable_notification: Option[Boolean] = None,
                         reply_to_message_id: Option[Int] = None,
                         reply_markup: Option[String] = None //FIX
                       )


