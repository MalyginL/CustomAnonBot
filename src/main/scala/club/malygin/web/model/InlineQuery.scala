package club.malygin.web.model

case class InlineQuery(id: String, from: User, location: Option[Location] = None, query: String, offset: String)
