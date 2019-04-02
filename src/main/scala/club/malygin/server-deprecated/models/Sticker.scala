package club.malygin.server.models

import club.malygin.server.models.main_types.PhotoSize

case class Sticker(
    fileId: String,
    width: Int,
    height: Int,
    thumb: Option[PhotoSize] = None,
    emoji: Option[String] = None,
    setName: Option[String] = None,
    maskPosition: Option[Position] = None,
    fileSize: Option[Int] = None
)
