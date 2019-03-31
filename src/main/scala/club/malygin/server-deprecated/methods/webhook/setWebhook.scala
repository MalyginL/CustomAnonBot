package club.malygin.server.methods.webhook

import club.malygin.server.models.input_media.InputFile

class setWebhook (
  url            : String,
  certificate    : Option[InputFile] = None,
  maxConnections : Option[Int] = None,
  allowedUpdates : Option[String] = None
                 )
