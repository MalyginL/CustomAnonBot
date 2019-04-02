package club.malygin.server.models.methods

import club.malygin.server.models.input_media.InputFile

case class SendAudio(
    chat_id: Int,
    audio: InputFile,
    caption: Option[String] = None,
    parse_mode: Option[String] = None,
    duration: Option[Int] = None,
    performer: Option[String] = None,
    title: Option[String] = None,
    thumb: Option[InputFile] = None,
    disable_notification: Option[Boolean] = None,
    reply_to_message_id: Option[Int] = None,
    reply_markup: Option[String] = None //FIX
)
