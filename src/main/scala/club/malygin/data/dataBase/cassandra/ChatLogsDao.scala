package club.malygin.data.dataBase.cassandra

import com.outworkers.phantom.ResultSet
import com.outworkers.phantom.connectors.CassandraConnection
import com.outworkers.phantom.database.Database
import com.outworkers.phantom.dsl._

import scala.concurrent.Future

class ChatLogsDao(override val connector: CassandraConnection) extends Database[ChatLogsDao](connector) {

  object ChatLongsTable extends ChatLongsTable with connector.Connector

  def truncateAll = Future {
    CassandraDatabase.truncate()
  }

  def save(chat: ChatLogsModel): Future[ResultSet] = ChatLongsTable.customStore(chat)

  def getUserMessages(user: BigInt): Future[Seq[ChatLogsModel]] = ChatLongsTable.getByUserId(user)

}

object CassandraDatabase extends ChatLogsDao(Connector.connector)
