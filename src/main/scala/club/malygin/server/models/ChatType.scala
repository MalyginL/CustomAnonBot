package club.malygin.server.models

object ChatType extends Enumeration {
  type ChatType = Value
  val Private, Group, Supergroup, Channel = Value
}