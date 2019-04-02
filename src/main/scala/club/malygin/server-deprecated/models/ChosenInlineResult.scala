package club.malygin.server.models

import club.malygin.server.models.main_types.{Location, User}

case class ChosenInlineResult(
    resultId: String,
    from: User,
    location: Option[Location] = None,
    inlineMessageId: Option[String] = None,
    query: String
)
