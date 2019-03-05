package club.malygin.server.models.methods

import club.malygin.server.models.input_media.InputFile

case class SendVideoNote(
                          chat_id: Int,
                          video_note: InputFile,
                          duration: Option[Int] = None,
                          length: Option[Int] = None,
                          thumb: Option[InputFile] = None,
                          disable_notification: Option[Boolean] = None,
                          reply_to_message_id: Option[Int] = None,
                          reply_markup: Option[String] = None //FIX
                        )
