package club.malygin.server.models
import club.malygin.server.models.PositionType.PositionType

case class Position(
                         point  : PositionType,
                         xShift : Double,
                         yShift : Double,
                         zoom   : Double)

