package club.malygin.server.models

import club.malygin.server.models.Currency.Currency

case class Payment(
    currency: Currency,
    totalAmount: Long,
    invoicePayload: String,
    shippingOptionId: Option[String] = None,
    orderInfo: Option[OrderInfo] = None,
    telegramPaymentChargeId: String,
    providerPaymentChargeId: String
)
