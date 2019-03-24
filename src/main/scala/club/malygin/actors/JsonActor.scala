package club.malygin.actors

import akka.actor.Actor
import club.malygin.web.model.Update

class JsonActor extends Actor {
  override def receive: Receive ={
    case e:Update=> println("Gotcha!")
    case _ => println("fail")
  }
}
