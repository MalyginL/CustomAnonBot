package club.malygin.web.model

case class CallbackQuery(
                          id              : String,
                          from            : User,
                          message         : Option[Message] = None,
                          inline_message_id : Option[String] = None,
                          chat_instance    : String,
                          data            : Option[String] = None,
                          game_short_name   : Option[String] = None
                        )