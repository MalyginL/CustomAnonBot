package club.malygin.telegram

import akka.actor.{Actor, ActorRef, Props}
import club.malygin.data.cache.UserPairCache
import club.malygin.web.model.Update
import com.typesafe.scalalogging.LazyLogging
import javax.inject.{Inject, Named}

import scala.concurrent.ExecutionContext

//class RouterActor @Inject()(@Named("commandActor") commandActor: ActorRef, cache: UserPairCache[Long, Long])
class RouterActor @Inject()( cache: UserPairCache[Long, Long])
    extends Actor
    with LazyLogging {

  override def receive(): Receive = {
    case update: Update => {
      update.callback_query match{
        case Some(callback) => getChild(callback.from.id.toString) ! callback
        case None =>
          update.message match {
            case Some(message) => message.from match {
              case Some(user) => getChild(user.id.toString) ! message
              case None => logger.warn("no user available")
            }
            case None => logger.warn("no message available")
          }
      }


      /*

      update.callback_query match {
        case Some(callback) => commandActor ! callback
        case None =>
          update.message match {
            case Some(m) if m.entities.isDefined =>
              commandActor ! update.message.get

            /** if entites is defined, 100% we will have text with those entities */

            case Some(m) =>
              m.from match {
                case Some(user) =>
                  cache
                    .loadFromCache(user.id)
                    .map(companion => getChild(companion.toString) ! update.message)(ExecutionContext.global)
                case None => logger.debug("no user available")
              }
            case None => logger.debug("no message available")
          }
      }

      */


    }
  }

  private var children = Map.empty[String, ActorRef]

  private def getChild(id: String): ActorRef =
    context.child(id).getOrElse {
      logger.info(s"creating new actor with id $id")
      val child = context.actorOf(Props(new UserActor(cache)), id)
      children += (id -> child)
      child
    }
}
