package club.malygin.server.models.main_types

case class InlineKeyboardButton (
                                  text:String,
                                  url:Option[String] = None,
                                  callback_data: Option[String] = None,
                                  switch_inline_query: Option[String] = None,
                                  switch_inline_query_current_chat: Option[String] = None,
                                  callback_game:Option[String]=None, //FIX ME
                                  pay:Option[Boolean]=None
                                )


