/*package club.malygin.data.dataBase.pg.dao
import club.malygin.data.dataBase.pg.Schema

import club.malygin.data.dataBase.pg.model.UserPairs

import scala.concurrent.Future

class UserPairsDaoImpl extends UserPairsDao {

  import Schema.pairs
  import Schema.sqldb
  import Schema.profile.api._

  override def find(userId: Long): Future[UserPairs] =
    sqldb.run(pairs.filter(e=> (e.first === userId) || (e.second === userId)).take(1).result.head)

  override def insert(pair: UserPairs): Future[Unit] =  sqldb.run(pairs += pair).map(_ => ())
}
*/