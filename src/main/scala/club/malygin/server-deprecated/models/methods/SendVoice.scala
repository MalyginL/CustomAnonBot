package club.malygin.server.models.methods

import club.malygin.server.models.input_media.InputFile

case class SendVoice(
    chat_id: Int,
    voice: InputFile,
    caption: Option[String] = None,
    parse_mode: Option[String] = None,
    duration: Option[Int] = None,
    disable_notification: Option[Boolean] = None,
    reply_to_message_id: Option[Int] = None,
    reply_markup: Option[String] = None //FIX
)
