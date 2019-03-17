package club.malygin.web.model

case class Document(
                     file_id   : String,
                     thumb    : Option[PhotoSize] = None,
                     file_name : Option[String] = None,
                     mime_type : Option[String] = None,
                     file_size : Option[Int] = None
                   )
