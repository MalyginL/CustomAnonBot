package club.malygin.server.models

import club.malygin.server.models.Currency.Currency
import club.malygin.server.models.main_types.User

case class PreCheckoutQuery(
                             id               : String,
                             from             : User,
                             currency         : Currency,
                             totalAmount      : Long,
                             invoicePayload   : String,
                             shippingOptionId : Option[String] = None,
                             orderInfo        : Option[OrderInfo] = None
                           )