package club.malygin.server.models

import club.malygin.server.models.main_types.{Location, User}

case class InlineQuery(
                        id       : String,
                        from     : User,
                        location : Option[Location] = None,
                        query    : String,
                        offset   : String
                      )