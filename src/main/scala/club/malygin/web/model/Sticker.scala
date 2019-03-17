package club.malygin.web.model

case class Sticker(
                    file_id       : String,
                    width        : Int,
                    height       : Int,
                    thumb        : Option[PhotoSize] = None,
                    emoji        : Option[String] = None,
                    set_name      : Option[String] = None,
                    mask_position : Option[Position] = None,
                    file_size     : Option[Int] = None
                  )
