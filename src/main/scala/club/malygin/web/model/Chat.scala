package club.malygin.web.model

case class Chat(
                 id            : Long,
                 `type`        : String,
                 title         : Option[String] = None,
                 username      : Option[String] = None,
                 first_name     : Option[String] = None,
                 last_name      : Option[String] = None,
                 all_members_are_administrators : Option[Boolean] = None,
                 photo         : Option[ChatPhoto] = None,
                 description   : Option[String] = None,
                 invite_link    : Option[String] = None,
                 pinned_message : Option[Message] = None,
                 sticker_set_name: Option[String] = None,
                 can_set_sticker_set : Option[Boolean] = None
               )

