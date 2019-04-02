package club.malygin.server.json

import club.malygin.server.models.PositionType.PositionType
import club.malygin.server.models.{Game, Invoice, Position, Sticker}
import club.malygin.server.models.main_types.{Animation, Document, _}
import club.malygin.server.models.methods.SendMessage
import io.circe.Encoder
import io.circe.generic.extras._
import io.circe.generic.extras.auto._
import io.circe.generic.extras.semiauto._
import io.circe.syntax._

trait JsonEncoders {
  implicit val customConfig: Configuration = Configuration.default.withDefaults
  implicit val SendMessageEncoder: Encoder[SendMessage] =
    deriveEncoder[SendMessage]
  implicit val UserEncoder: Encoder[User]           = deriveEncoder[User]
  implicit val ChatEncoder: Encoder[Chat]           = deriveEncoder[Chat]
  implicit val ChatPhotoEncoder: Encoder[ChatPhoto] = deriveEncoder[ChatPhoto]
  implicit val MessageEntityEncoder: Encoder[MessageEntity] =
    deriveEncoder[MessageEntity]
  implicit val AudioEncoder: Encoder[Audio]         = deriveEncoder[Audio]
  implicit val PhotoSizeEncoder: Encoder[PhotoSize] = deriveEncoder[PhotoSize]
  implicit val DocumentEncoder: Encoder[Document]   = deriveEncoder[Document]
  implicit val AnimationEncoder: Encoder[Animation] = deriveEncoder[Animation]
  implicit val GameEncoder: Encoder[Game]           = deriveEncoder[Game]
  implicit val StickerEncoder: Encoder[Sticker]     = deriveEncoder[Sticker]
  implicit val PositionEncoder: Encoder[Position]   = deriveEncoder[Position]
  implicit val PositionTypeEncoder: Encoder[PositionType] =
    Encoder[String].contramap[PositionType](e ⇒ e.toString)
  implicit val VideoEncoder: Encoder[Video]         = deriveEncoder[Video]
  implicit val VoiceEncoder: Encoder[Voice]         = deriveEncoder[Voice]
  implicit val VideoNoteEncoder: Encoder[VideoNote] = deriveEncoder[VideoNote]
  implicit val ContactEncoder: Encoder[Contact]     = deriveEncoder[Contact]
  implicit val LocationEncoder: Encoder[Location]   = deriveEncoder[Location]
  implicit val VenueEncoder: Encoder[Venue]         = deriveEncoder[Venue]
  // implicit val InvoiceEncoder: Encoder[Invoice] =   deriveEncoder[Invoice]
}
object JsonEncoders extends JsonEncoders
