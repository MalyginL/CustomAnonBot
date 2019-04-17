package club.malygin.data.dataBase.pg.model

import java.util.UUID

import org.joda.time.DateTime

case class Users(
                  userId: Long,
                  firstName: String,
                  lastName: Option[String] = None,
                  username: Option[String] = None,
                  status: Option[Long] = None,
                  lastOnline: Option[DateTime] = None,
                  searching_for :Option[UUID] = None
                )
