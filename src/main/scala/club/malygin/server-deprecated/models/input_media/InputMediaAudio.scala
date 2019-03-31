package club.malygin.server.models.input_media

case class InputMediaAudio (
                             `type`: String,
                             media: String,
                             thumb: Option[InputFile] = None,
                             caption: Option[String] = None,
                             parse_mode: Option[String] = None,
                             duration: Option[Int] = None,
                             performer:Option[String] = None,
                             title:Option[String] = None
                           )


