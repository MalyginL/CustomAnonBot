package club.malygin.web.model

case class Voice(file_id: String, duration: Int, mime_type: Option[String] = None, file_size: Option[Int] = None)
