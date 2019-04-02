package club.malygin.server.models.main_types

case class Document(
    file_id: String,
    thumb: Option[PhotoSize] = None,
    file_name: Option[String] = None,
    mime_type: Option[String] = None,
    file_size: Option[Int] = None
)
