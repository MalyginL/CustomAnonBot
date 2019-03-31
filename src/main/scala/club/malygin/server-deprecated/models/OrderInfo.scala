package club.malygin.server.models

case class OrderInfo(
                      name            : Option[String] = None,
                      phoneNumber     : Option[String] = None,
                      email           : Option[String] = None,
                      shippingAddress : Option[Address] = None
                    )
