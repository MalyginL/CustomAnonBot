package club.malygin

object TestConfig {

  import slick.jdbc.PostgresProfile.api._
  def testdb = Database.forConfig("db.test")
}
