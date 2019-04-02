package club.malygin.server.models.main_types

case class ForceReply(force_reply: Boolean = true, selective: Option[Boolean] = None)
