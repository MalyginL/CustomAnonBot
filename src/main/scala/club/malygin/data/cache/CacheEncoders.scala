package club.malygin.data.cache


import club.malygin.data.cache.model.CacheStatistic
import io.circe.Encoder
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveEncoder

trait CacheEncoders {
  implicit val customConfig: Configuration = Configuration.default.withDefaults
  implicit val SendCacheStatistic: Encoder[CacheStatistic] = deriveEncoder[CacheStatistic]
}
object CacheEncoders extends CacheEncoders
