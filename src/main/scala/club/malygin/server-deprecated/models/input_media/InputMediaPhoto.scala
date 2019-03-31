package club.malygin.server.models.input_media

class InputMediaPhoto(
                       `type`: String,
                       media: String,
                       caption: Option[String] = None,
                       parse_mode: Option[String] = None
                     )