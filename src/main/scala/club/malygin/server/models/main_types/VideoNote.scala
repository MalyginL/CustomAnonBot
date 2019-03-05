package club.malygin.server.models.main_types

case class VideoNote(
                      file_id   : String,
                      length   : Int,
                      duration : Int,
                      thumb    : Option[PhotoSize] = None,
                      file_size : Option[Int] = None
               )