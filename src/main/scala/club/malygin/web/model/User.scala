package club.malygin.web.model

case class User(
    id: Int,
    is_bot: Boolean,
    first_name: String,
    last_name: Option[String] = None,
    username: Option[String] = None,
    language_code: Option[String] = None
)
