package club.malygin.web

import club.malygin.data.appStat.AppStatModel
import club.malygin.data.cache.CacheStatModel
import io.circe.Encoder
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveEncoder

trait JsonEncoders {

  implicit val customConfig: Configuration = Configuration.default.withDefaults
  implicit val SendCacheStatistic: Encoder[CacheStatModel] = deriveEncoder[CacheStatModel]
  implicit val AppStatistic: Encoder[AppStatModel] = deriveEncoder[AppStatModel]
}
  object JsonEncoders extends JsonEncoders

