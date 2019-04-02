package club.malygin.server.models

import club.malygin.server.models.main_types.{Animation, MessageEntity, PhotoSize}

case class Game(
    title: String,
    description: String,
    photo: Array[PhotoSize],
    text: Option[String] = None,
    textEntities: Option[Array[MessageEntity]] = None,
    animation: Option[Animation] = None
)
