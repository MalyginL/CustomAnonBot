package club.malygin.server.json


import club.malygin.server.models.CountryCode.CountryCode
import club.malygin.server.models.Currency.Currency

import club.malygin.server.models.PositionType.PositionType
import club.malygin.server.models._
import club.malygin.server.models.main_types._
import club.malygin.server.models.webhook.Update
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

trait JsonDecoders  {
    implicit val AddressReader: Decoder[Address] =deriveDecoder[Address]
    implicit val AnimationReader: Decoder[Animation] =deriveDecoder[Animation]
    implicit val AudioReader: Decoder[Audio] =deriveDecoder[Audio]
    implicit val CallbackQueryReader: Decoder[CallbackQuery] =deriveDecoder[CallbackQuery]
    implicit val ChatReader: Decoder[Chat] =deriveDecoder[Chat]
    implicit val ChatPhotoReader: Decoder[ChatPhoto] =deriveDecoder[ChatPhoto]
   // implicit val ChatTypeReader: Decoder[ChatType] =Decoder[String].map(a=>ChatType.withName(a))
    implicit val ChosenInlineResultReader: Decoder[ChosenInlineResult] =deriveDecoder[ChosenInlineResult]
    implicit val ContactReader: Decoder[Contact] =deriveDecoder[Contact]
    implicit val CountryCodeReader: Decoder[CountryCode] =Decoder[String].map(a=>CountryCode.withName(a))
    implicit val CurrencyReader: Decoder[Currency] =Decoder[String].map(a=>Currency.withName(a))
    implicit val DocumentReader: Decoder[Document] =deriveDecoder[Document]
    implicit val GameReader: Decoder[Game] =deriveDecoder[Game]
    implicit val InlineQueryReader: Decoder[InlineQuery] =deriveDecoder[InlineQuery]
    implicit val InvoiceReader: Decoder[Invoice] =deriveDecoder[Invoice]
    implicit val LocationReader: Decoder[Location] =deriveDecoder[Location]
    implicit val MessageReader: Decoder[Message] =deriveDecoder[Message]
    implicit val MessageEntityReader: Decoder[MessageEntity] =deriveDecoder[MessageEntity]
  //  implicit val MessageEntityTypeReader: Decoder[MessageEntityType] =Decoder[String].map(a=>MessageEntityType.withName(a))
    implicit val OrderInfoReader: Decoder[OrderInfo] =deriveDecoder[OrderInfo]
    implicit val PaymentReader: Decoder[Payment] =deriveDecoder[Payment]
    implicit val PhotoReader: Decoder[PhotoSize] =deriveDecoder[PhotoSize]
    implicit val PositionReader: Decoder[Position] =deriveDecoder[Position]
    implicit val PositionTypeReader: Decoder[PositionType] =Decoder[String].map(a=>PositionType.withName(a))
    implicit val PreCheckoutQueryReader: Decoder[PreCheckoutQuery] =deriveDecoder[PreCheckoutQuery]
    implicit val ShippingQueryReader: Decoder[ShippingQuery] =deriveDecoder[ShippingQuery]
    implicit val StickerReader: Decoder[Sticker] =deriveDecoder[Sticker]
    implicit val UserReader: Decoder[User] =deriveDecoder[User]
    implicit val VenueReader: Decoder[Venue] =deriveDecoder[Venue]
    implicit val VideoReader: Decoder[Video] =deriveDecoder[Video]
    implicit val VoiceReader: Decoder[Voice] =deriveDecoder[Voice]
    implicit val WebhookMessageReader: Decoder[Update] =deriveDecoder[Update]
    implicit val VideoMsgReader: Decoder[VideoNote] =deriveDecoder[VideoNote]
}
object JsonDecoders extends JsonDecoders
