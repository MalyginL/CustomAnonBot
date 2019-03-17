package club.malygin.web.model

case class Contact(
                    phone_number : String,
                    first_name   : String,
                    last_name    : Option[String] = None,
                    user_id      : Option[Int] = None,
                    vcard       : Option[String] = None
                  )

