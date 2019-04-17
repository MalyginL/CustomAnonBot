package club.malygin.web

import club.malygin.data.dataBase.pg.model.CallbackMessage
import club.malygin.web.model.PositionType.PositionType
import club.malygin.web.model._
import io.circe.generic.semiauto.deriveDecoder
import io.circe.Decoder

trait JsonDecoders {
  implicit val AnimationReader: Decoder[Animation] = deriveDecoder[Animation]
  implicit val AudioReader: Decoder[Audio]         = deriveDecoder[Audio]
  implicit val CallbackQueryReader: Decoder[CallbackQuery] =
    deriveDecoder[CallbackQuery]
  implicit val ChatReader: Decoder[Chat]           = deriveDecoder[Chat]
  implicit val ChatPhotoReader: Decoder[ChatPhoto] = deriveDecoder[ChatPhoto]
  implicit val ChosenInlineResultReader: Decoder[ChosenInlineResult] =
    deriveDecoder[ChosenInlineResult]
  implicit val ContactReader: Decoder[Contact]   = deriveDecoder[Contact]
  implicit val DocumentReader: Decoder[Document] = deriveDecoder[Document]
  implicit val InlineQueryReader: Decoder[InlineQuery] =
    deriveDecoder[InlineQuery]
  implicit val LocationReader: Decoder[Location] = deriveDecoder[Location]
  implicit val MessageReader: Decoder[Message]   = deriveDecoder[Message]
  implicit val MessageEntityReader: Decoder[MessageEntity] =
    deriveDecoder[MessageEntity]
  implicit val PhotoSizeReader: Decoder[PhotoSize] = deriveDecoder[PhotoSize]
  implicit val PositionReader: Decoder[Position]   = deriveDecoder[Position]
  implicit val PositionTypeReader: Decoder[PositionType] =
    Decoder[String].map(a => PositionType.withName(a))
  implicit val StickerReader: Decoder[Sticker]       = deriveDecoder[Sticker]
  implicit val WebhookMessageReader: Decoder[Update] = deriveDecoder[Update]
  implicit val UserReader: Decoder[User]             = deriveDecoder[User]
  implicit val VenueReader: Decoder[Venue]           = deriveDecoder[Venue]
  implicit val VideoReader: Decoder[Video]           = deriveDecoder[Video]
  implicit val VideoNoteReader: Decoder[VideoNote]   = deriveDecoder[VideoNote]
  implicit val VoiceReader: Decoder[Voice]           = deriveDecoder[Voice]
  implicit val cbMessageReader: Decoder[CallbackMessage]           = deriveDecoder[CallbackMessage]

}
object JsonDecoders extends JsonDecoders
