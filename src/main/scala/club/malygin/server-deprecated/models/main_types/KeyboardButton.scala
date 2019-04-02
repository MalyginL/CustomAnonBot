package club.malygin.server.models.main_types

case class KeyboardButton(text: String, request_contact: Option[Boolean], request_location: Option[Boolean])
