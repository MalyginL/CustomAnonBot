package club.malygin.server.models.input_media

case class InputMediaDocument(
    `type`: String,
    media: String,
    thumb: Option[InputFile] = None,
    caption: Option[String] = None,
    parse_mode: Option[String] = None
)
