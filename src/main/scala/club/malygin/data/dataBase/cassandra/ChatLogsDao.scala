package club.malygin.data.dataBase.cassandra

import com.outworkers.phantom.ResultSet
import com.outworkers.phantom.connectors.CassandraConnection
import com.outworkers.phantom.database.Database
import com.outworkers.phantom.dsl._

import scala.concurrent.Future
import scala.concurrent.duration._

class ChatLogsDao(override val connector: CassandraConnection) extends Database[ChatLogsDao](connector) {

  object ChatLongsTable extends ChatLongsTable with connector.Connector

  def truncateAll = Future {
    CassandraDatabase.truncate(1.second)
  }

  def save(chat: ChatLogsModel): Future[ResultSet] = ChatLongsTable.customStore(chat)

  def getUserMessages(user: BigInt): Future[Seq[ChatLogsModel]] = ChatLongsTable.getByUserId(user)

  def delete(chat: ChatLogsModel): Future[ResultSet] =  ChatLongsTable.customDelete(chat)
}

object CassandraDatabase extends ChatLogsDao(Connector.connector)
