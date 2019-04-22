package club.malygin.data.dataBase.pg.model

case class CallbackMessage(
    id: String,
    ans: Option[Boolean] = None
)
