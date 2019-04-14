package club.malygin.data.dataBase.pg.model

import org.joda.time.DateTime

case class Users(
                  userId: Long,
                  firstName: String,
                  lastName: Option[String] = None,
                  username: Option[String] = None,
                  status: Boolean = false,
                  lastOnline: Option[DateTime] = None
                )
