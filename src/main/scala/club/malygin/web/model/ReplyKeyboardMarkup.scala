package club.malygin.web.model

case class ReplyKeyboardMarkup(
    keyboard: Array[Array[KeyboardButton]],
    resize_keyboard: Option[Boolean] = Some(true),
    one_time_keyboard: Option[Boolean] = Some(true)
    //  selective:Option[Boolean] = None
)
