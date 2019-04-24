package club.malygin.web

import java.util.UUID

import club.malygin.data.appStat.AppStatModel
import club.malygin.data.cache.CacheStatModel
import club.malygin.data.dataBase.cassandra.ChatLogsModel
import club.malygin.data.dataBase.pg.model.CallbackMessage
import club.malygin.telegram.botMethods._
import club.malygin.web.model.{InlineKeyboardButton, InlineKeyboardMarkup, KeyboardButton, ReplyKeyboardMarkup}
import io.circe.{Encoder, Json, JsonNumber}
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveEncoder
import org.joda.time.DateTime

trait JsonEncoders {

  implicit val customConfig: Configuration = Configuration.default.withDefaults
  implicit val SendCacheStatistic: Encoder[CacheStatModel] =
    deriveEncoder[CacheStatModel].mapJsonObject(_.filter(!_._2.isNull))
  implicit val AppStatistic: Encoder[AppStatModel] =
    deriveEncoder[AppStatModel].mapJsonObject(_.filter(!_._2.isNull))
  implicit val SendMessageEncoder: Encoder[SendMessage] =
    deriveEncoder[SendMessage].mapJsonObject(_.filter(!_._2.isNull))
  implicit val InlineKeyboardMarkupEncoder: Encoder[InlineKeyboardMarkup] =
    deriveEncoder[InlineKeyboardMarkup].mapJsonObject(_.filter(!_._2.isNull))
  implicit val InlineKeyboardButtonEncoder: Encoder[InlineKeyboardButton] =
    deriveEncoder[InlineKeyboardButton].mapJsonObject(_.filter(!_._2.isNull))
  implicit val KeyboardButtonEncoder: Encoder[KeyboardButton] =
    deriveEncoder[KeyboardButton].mapJsonObject(_.filter(!_._2.isNull))
  implicit val ReplyKeyboardMarkupEncoder: Encoder[ReplyKeyboardMarkup] =
    deriveEncoder[ReplyKeyboardMarkup].mapJsonObject(_.filter(!_._2.isNull))
  implicit val AnswerCallbackQueryEncoder: Encoder[AnswerCallbackQuery] =
    deriveEncoder[AnswerCallbackQuery].mapJsonObject(_.filter(!_._2.isNull))
  implicit val EditMessageReplyMarkupEncoder: Encoder[EditMessageReplyMarkup] =
    deriveEncoder[EditMessageReplyMarkup].mapJsonObject(_.filter(!_._2.isNull))
  implicit val SendStickerEncoder: Encoder[SendSticker] =
    deriveEncoder[SendSticker]
  implicit val SendAudioEncoder: Encoder[SendAudio] =
    deriveEncoder[SendAudio]
  implicit val SendPhotoEncoder: Encoder[SendPhoto] =
    deriveEncoder[SendPhoto]
  implicit val SendVoiceEncoder: Encoder[SendVoice] =
    deriveEncoder[SendVoice]
  implicit val SendAnimationEncoder: Encoder[SendAnimation] =
    deriveEncoder[SendAnimation]


  implicit val ChatLogEncoder: Encoder[ChatLogsModel] =
    deriveEncoder[ChatLogsModel]
  implicit val TimestampFormat: Encoder[DateTime] = (a: DateTime) =>
    Encoder.encodeString.apply(a.toDateTime.toDateTime.toString)
  implicit val bigIntEncoder: Encoder[BigInt] = Encoder.encodeJsonNumber
    .contramap(x => JsonNumber.fromDecimalStringUnsafe(x.toString))

  implicit final val encodeUUID: Encoder[UUID] = (a: UUID) => Json.fromString(a.toString)

  implicit val cbMessageEncoder: Encoder[CallbackMessage] =
    deriveEncoder[CallbackMessage].mapJsonObject(_.filter(!_._2.isNull))
}

object JsonEncoders extends JsonEncoders
