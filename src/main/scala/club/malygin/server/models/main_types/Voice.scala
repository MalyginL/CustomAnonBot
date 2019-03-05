package club.malygin.server.models.main_types

case class Voice(
                  file_id   : String,
                  duration : Int,
                  mime_type : Option[String] = None,
                  file_size : Option[Int] = None
                )
