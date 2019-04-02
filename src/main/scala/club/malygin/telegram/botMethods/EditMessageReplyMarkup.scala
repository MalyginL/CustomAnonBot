package club.malygin.telegram.botMethods

import club.malygin.web.model.InlineKeyboardMarkup

case class EditMessageReplyMarkup(
    chat_id: Option[String] = None,
    message_id: Option[Int] = None,
    reply_markup: Option[InlineKeyboardMarkup] = None,
    inline_message_id: Option[String] = None
)
