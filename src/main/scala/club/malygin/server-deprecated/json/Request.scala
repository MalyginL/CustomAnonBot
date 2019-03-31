package club.malygin.server.json

trait Request[R] {

  def methodName: String = getClass.getSimpleName.takeWhile('$' != _)
}
