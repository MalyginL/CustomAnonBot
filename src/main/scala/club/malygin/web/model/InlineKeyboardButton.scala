package club.malygin.web.model

case class InlineKeyboardButton(
    text: String,
    callback_data: Option[String] = None
)
