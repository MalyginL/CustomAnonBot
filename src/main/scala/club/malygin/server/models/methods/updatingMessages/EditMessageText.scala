package club.malygin.server.models.methods.updatingMessages

import club.malygin.server.models.main_types.InlineKeyboardMarkup

case class EditMessageText (
                             chat_id:Option[Int]=None,
                             message_id:Option[Integer]=None,
                             inline_message_id:Option[String]=None,
                             text:String,
                             parse_mode:Option[String]=None,
                             disable_web_page_preview:Option[Boolean]=None,
                             reply_markup:Option[InlineKeyboardMarkup]=None,

                           )


