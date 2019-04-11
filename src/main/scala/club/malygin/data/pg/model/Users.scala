package club.malygin.data.pg.model
import akka.http.scaladsl.model.DateTime

case class Users(
    userId: Long,
    firstName: String,
    lastName: String,
    username: String,
    status: Boolean,
    lastOnline: DateTime
)
