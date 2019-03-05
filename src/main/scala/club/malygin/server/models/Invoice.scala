package club.malygin.server.models

import club.malygin.server.models.Currency.Currency

case class Invoice(
                    title          : String,
                    description    : String,
                    startParameter : String,
                    currency       : Currency,
                    totalAmount    : Long
                  )

