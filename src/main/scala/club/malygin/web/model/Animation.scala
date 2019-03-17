package club.malygin.web.model

case class Animation(
                      file_id   : String,
                      width : Int,
                      height: Int,
                      duration: Int,
                      thumb    : Option[PhotoSize] = None,
                      file_name : Option[String] = None,
                      mime_type : Option[String] = None,
                      file_size : Option[Int] = None
                    )