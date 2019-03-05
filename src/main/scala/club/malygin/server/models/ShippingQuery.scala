package club.malygin.server.models

import club.malygin.server.models.main_types.User

case class ShippingQuery(
                          id              : String,
                          from            : User,
                          invoicePayload  : String,
                          shippingAddress : Address
                        )