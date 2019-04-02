package club.malygin.server.models.main_types

case class PhotoSize(file_id: String, width: Int, height: Int, file_size: Option[Int] = None)
