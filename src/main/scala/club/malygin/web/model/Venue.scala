package club.malygin.web.model

case class Venue(
    location: Location,
    title: String,
    address: String,
    foursquare_id: Option[String] = None,
    foursquare_type: Option[String] = None
)
