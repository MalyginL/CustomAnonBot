package club.malygin.web.model

case class Audio(
    file_id: String,
    duration: Int,
    performer: Option[String] = None,
    title: Option[String] = None,
    mime_type: Option[String] = None,
    file_size: Option[Int] = None,
    thumb: Option[PhotoSize] = None
)
