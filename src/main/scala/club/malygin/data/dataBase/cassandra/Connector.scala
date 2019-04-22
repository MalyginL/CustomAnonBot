package club.malygin.data.dataBase.cassandra

import com.outworkers.phantom.connectors.{CassandraConnection, ContactPoints}
import com.typesafe.config.ConfigFactory

object Connector {
  private val config   = ConfigFactory.load()
  private val hosts    = config.getString("db.cassandra.host")
  private val keyspace = config.getString("db.cassandra.keyspace")

  lazy val connector: CassandraConnection = ContactPoints(Seq(hosts)).keySpace(keyspace)
  lazy val testconnector: CassandraConnection = ContactPoints(Seq(hosts)).keySpace("test")

}
