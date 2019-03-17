package club.malygin.web.model

case class Video(
                  file_id   : String,
                  width    : Int,
                  height   : Int,
                  duration : Int,
                  thumb    : Option[PhotoSize] = None,
                  mime_type : Option[String] = None,
                  file_size : Option[Int] = None
                )
