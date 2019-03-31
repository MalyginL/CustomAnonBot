package club.malygin.telegram.botMethods

case class AnswerCallbackQuery(
                                callback_query_id: String,
                                text: Option[String] = None,
                                show_alert: Option[Boolean] = None,
                                url: Option[String] = None,
                                cache_time: Option[Int] = None
                              )
