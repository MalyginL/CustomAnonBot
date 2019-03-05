package club.malygin.server.models.methods

case class EditMessageLiveLocation(
                                    chat_id: Option[Int] = None,
                                    message_id: Option[Int] = None,
                                    inline_message_id: Option[String] = None,
                                    latitude: Float,
                                    longitude: Float,
                                    reply_markup: Option[String] = None //FIX

                                  )


