package club.malygin.web.model

import club.malygin.web.model.PositionType.PositionType

case class Position(point: PositionType, xShift: Double, yShift: Double, zoom: Double)
