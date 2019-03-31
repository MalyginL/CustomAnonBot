package club.malygin.telegram

import akka.actor.Actor
import club.malygin.data.cache.UserPairCache
import club.malygin.web.model.{CallbackQuery, Message}
import com.typesafe.scalalogging.LazyLogging

class CommandActor(cache: UserPairCache[Long, Long]) extends Actor with LazyLogging with CommandService {

  val Pattern = "\\/(\\S+)".r

  override def receive: Receive = {
    case message: Message => {
      message.text.get match {
        case Pattern(command) => solve(command, message.from.get.id)
        case _ => logger.debug("more than one")
      }
    }
    case callback: CallbackQuery => answer(callback.data.get,callback.id,callback.message.get.chat.id.toString,callback.message.get.message_id.intValue,callback.from.id)
    case _ => println("fail")
  }
}
