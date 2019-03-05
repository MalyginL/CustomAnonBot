package club.malygin.server.models.methods

import club.malygin.server.models.input_media.InputFile

case class SendAnimation(
                          chat_id: Int,
                          animation: InputFile,
                          duration: Option[Int] = None,
                          width: Option[Int] = None,
                          height: Option[Int] = None,
                          thumb: Option[InputFile] = None,
                          caption: Option[String] = None,
                          parse_mode: Option[String] = None,
                          disable_notification: Option[Boolean] = None,
                          reply_to_message_id: Option[Int] = None,
                          reply_markup: Option[String] = None //FIX
                        )

