package club.malygin.server.models.webhook

import club.malygin.server.models.input_media.InputFile
/*
Ports currently supported for Webhooks: 443, 80, 88, 8443
*/
class Webhook(
  url            : String,
  certificate    : Option[InputFile] = None,
  maxConnections : Option[Int] = None,
  allowedUpdates : Option[String] = None
                 )
