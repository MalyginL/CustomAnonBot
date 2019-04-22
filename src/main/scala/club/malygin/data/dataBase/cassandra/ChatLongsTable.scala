package club.malygin.data.dataBase.cassandra

import com.outworkers.phantom.{ResultSet, Table}
import com.outworkers.phantom.dsl._

import scala.concurrent.Future

abstract class ChatLongsTable extends Table[ChatLongsTable, ChatLogsModel] {
  override def tableName: String = "messagelog"

  object id extends UUIDColumn with PrimaryKey

  object message extends StringColumn

  object target extends BigIntColumn

  object user extends BigIntColumn with PartitionKey

  object time extends DateTimeColumn with PartitionKey

  def getByLogId(id: UUID): Future[Option[ChatLogsModel]] =
    select
      .where(_.id eqs id)
      .consistencyLevel_=(ConsistencyLevel.ONE)
      .one()

  def getByUserId(user: BigInt): Future[Seq[ChatLogsModel]] =
    select
      .where(_.user eqs user)
      .consistencyLevel_=(ConsistencyLevel.ONE)
      .allowFiltering
      .fetch

  def customStore(chatLogsModel: ChatLogsModel): Future[ResultSet] =
    insert
      .value(_.id, chatLogsModel.id)
      .value(_.message, chatLogsModel.message)
      .value(_.user, chatLogsModel.user)
      .value(_.target, chatLogsModel.target)
      .value(_.time, chatLogsModel.time)
      .future()


  def customDelete(chat: ChatLogsModel): Future[ResultSet] = {
    delete
      .where(_.id eqs chat.id)
      .and(_.user eqs chat.user)
      .and(_.time eqs chat.time)
      .consistencyLevel_=(ConsistencyLevel.ONE)
      .future()
  }


}

