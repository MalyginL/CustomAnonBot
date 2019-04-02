package club.malygin.web.model

case class ChosenInlineResult(
    resultId: String,
    from: User,
    location: Option[Location] = None,
    inline_message_id: Option[String] = None,
    query: String
)
