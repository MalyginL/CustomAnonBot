package club.malygin.web

import club.malygin.data.appStat.AppStatModel
import club.malygin.data.cache.CacheStatModel
import club.malygin.telegram.botMethods.{AnswerCallbackQuery, EditMessageReplyMarkup, SendMessage}
import club.malygin.web.model.{InlineKeyboardButton, InlineKeyboardMarkup, KeyboardButton, ReplyKeyboardMarkup}
import io.circe.Encoder
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveEncoder

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
}
object JsonEncoders extends JsonEncoders
