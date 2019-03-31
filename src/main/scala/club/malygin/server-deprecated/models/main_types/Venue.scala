package club.malygin.server.models.main_types

case class Venue(
                  location       : Location,
                  title          : String,
                  address        : String,
                  foursquare_id   : Option[String] = None,
                  foursquare_type : Option[String] = None
                )
