package club.malygin.web.model

case class InlineKeyboardButton (
                                  text:String,
                                  callback_data: Option[String] = None,
                                //  switch_inline_query: Option[String] = None,
                               //   switch_inline_query_current_chat: Option[String] = None,
                                //  pay:Option[Boolean]=None
                                )


