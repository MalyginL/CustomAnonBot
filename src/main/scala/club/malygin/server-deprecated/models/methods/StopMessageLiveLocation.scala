package club.malygin.server.models.methods

import club.malygin.server.models.main_types.InlineKeyboardMarkup

case class StopMessageLiveLocation(
    chat_id: Option[Int] = None,
    message_id: Option[Int] = None,
    inline_message_id: Option[String] = None,
    reply_markup: Option[InlineKeyboardMarkup] = None
)
