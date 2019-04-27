package club.malygin.web.model

case class Update(
    update_id: Int,
    message: Option[Message] = None,
    edited_message: Option[Message] = None,
    channel_post: Option[Message] = None,
    edited_channel_post: Option[Message] = None,
    inline_query: Option[InlineQuery] = None,
    chosen_inline_result: Option[ChosenInlineResult] = None,
    callback_query: Option[CallbackQuery] = None

)
