package club.malygin.telegram.botMethods

import club.malygin.web.model.{InlineKeyboardMarkup, ReplyKeyboardMarkup}


case class SendMessage(
                        chat_id: Int,
                        text: String,
                        disable_web_page_preview: Option[Boolean] = None,
                        disable_notification: Option[Boolean] = None,
                        reply_markup: Option[InlineKeyboardMarkup] =None,
                      )

