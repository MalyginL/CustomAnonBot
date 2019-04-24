package club.malygin.web

import club.malygin.data.dataBase.cassandra.ChatLogsModel
import club.malygin.data.dataBase.pg.model.CallbackMessage
import club.malygin.telegram.botMethods.SendSticker
import club.malygin.web.model.PositionType.PositionType
import club.malygin.web.model._
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter

import scala.util.control.NonFatal

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
  implicit val SendStickerReader: Decoder[SendSticker] =
    deriveDecoder[SendSticker]
  implicit val LocationReader: Decoder[Location] = deriveDecoder[Location]
  implicit val MessageReader: Decoder[Message]   = deriveDecoder[Message]
  implicit val MessageEntityReader: Decoder[MessageEntity] =
    deriveDecoder[MessageEntity]
  implicit val PhotoSizeReader: Decoder[PhotoSize] = deriveDecoder[PhotoSize]
  implicit val PositionReader: Decoder[Position]   = deriveDecoder[Position]
  implicit val PositionTypeReader: Decoder[PositionType] =
    Decoder[String].map(a => PositionType.withName(a))
  implicit val StickerReader: Decoder[Sticker]           = deriveDecoder[Sticker]
  implicit val WebhookMessageReader: Decoder[Update]     = deriveDecoder[Update]
  implicit val UserReader: Decoder[User]                 = deriveDecoder[User]
  implicit val VenueReader: Decoder[Venue]               = deriveDecoder[Venue]
  implicit val VideoReader: Decoder[Video]               = deriveDecoder[Video]
  implicit val VideoNoteReader: Decoder[VideoNote]       = deriveDecoder[VideoNote]
  implicit val VoiceReader: Decoder[Voice]               = deriveDecoder[Voice]
  implicit val cbMessageReader: Decoder[CallbackMessage] = deriveDecoder[CallbackMessage]
  implicit val messageLogReader: Decoder[ChatLogsModel]  = deriveDecoder[ChatLogsModel]

  import org.joda.time.format.DateTimeFormat

  val dateFormatter: DateTimeFormatter = DateTimeFormat.forPattern("yyyyMMdd")
  implicit val decodeDateTime: Decoder[DateTime] = Decoder.decodeString.emap { s =>
    try {
      Right(DateTime.parse(s, dateFormatter))
    } catch {
      case NonFatal(e) => Left(e.getMessage)
    }
  }

}

object JsonDecoders extends JsonDecoders
