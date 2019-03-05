package club.malygin.server.models.webhook

case class WebhookInfo(

                        url: String,
                        has_custom_certificate: Boolean,
                        pending_update_count: Int,
                        last_error_date: Option[Int] = None,
                        last_error_message: Option[String] = None,
                        max_connections: Option[Int] = None,
                        allowed_updates: Option[Array[String]] = None
                      )


