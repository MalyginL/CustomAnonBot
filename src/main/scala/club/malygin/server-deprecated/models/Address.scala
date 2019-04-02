package club.malygin.server.models

import club.malygin.server.models.CountryCode.CountryCode

case class Address(
    countryCode: CountryCode,
    state: String,
    city: String,
    streetLine1: String,
    streetLine2: String,
    postCode: String
)
