package club.malygin.web.model

case class PhotoSize(file_id: String, width: Int, height: Int, file_size: Option[Int] = None)
