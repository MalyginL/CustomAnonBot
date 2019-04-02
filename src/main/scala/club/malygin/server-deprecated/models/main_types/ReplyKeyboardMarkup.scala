package club.malygin.server.models.main_types

case class ReplyKeyboardMarkup(
    keyboard: Array[Array[KeyboardButton]],
    resize_keyboard: Option[Boolean] = None,
    one_time_keyboard: Option[Boolean] = None,
    selective: Option[Boolean] = None
)
