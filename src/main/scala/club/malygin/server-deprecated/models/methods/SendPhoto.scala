package club.malygin.server.models.methods

import club.malygin.server.models.input_media.InputFile

case class SendPhoto(
                      chat_id: Int,
                      photo: InputFile,
                      caption: Option[String] = None,
                      parse_mode: Option[String] = None,
                      disable_notification: Option[Boolean] = None,
                      reply_to_message_id: Option[Int] = None,
                      reply_markup: Option[String] = None //FIX
                    )


