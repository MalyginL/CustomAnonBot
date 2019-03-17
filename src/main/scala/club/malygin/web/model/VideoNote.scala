package club.malygin.web.model

case class VideoNote(
                      file_id   : String,
                      length   : Int,
                      duration : Int,
                      thumb    : Option[PhotoSize] = None,
                      file_size : Option[Int] = None
               )