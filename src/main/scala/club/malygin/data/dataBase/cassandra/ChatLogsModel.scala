package club.malygin.data.dataBase.cassandra

import java.util.UUID

case class ChatLogsModel(
    id: UUID,
    user: BigInt,
    target: BigInt,
    message: String,
    time: org.joda.time.DateTime
)
