package club.malygin.server.models.main_types

case class File (
                  file_id: String,
                  file_size: Option[Int],
                  file_path:String
                )


