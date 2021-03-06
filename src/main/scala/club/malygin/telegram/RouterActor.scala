package club.malygin.telegram

import akka.actor.{Actor, ActorRef, Props}
import club.malygin.data.cache.UserPairCache
import club.malygin.data.dataBase.pg.dao.{QuizQuestionDao, QuizResultsDao, UsersDao}
import club.malygin.web.model.Update
import com.typesafe.scalalogging.LazyLogging
import javax.inject.Inject

case class ActorState(value: String, actorName: String)

class RouterActor @Inject()(
    cache: UserPairCache[Long, Long],
    usersDao: UsersDao,
    quizResultsDao: QuizResultsDao,
    quizQuestionDao: QuizQuestionDao
) extends Actor
    with LazyLogging {

  override def receive(): Receive = {
    case update: Update =>
      update.callback_query match {
        case Some(callback) => getChild(callback.from.id.toString) ! callback
        case None =>
          update.message match {
            case Some(message) =>
              message.from match {
                case Some(user) => getChild(user.id.toString) ! message
                case None       => logger.warn("no user available in UserActor receive")
              }
            case None => logger.warn("no message available in UserActor receive")
          }
      }

    case state: ActorState =>
      state.value match {
        case "?" =>
          getChild(state.actorName) ! idleChildren.getOrElse(state.actorName, ActorState("init", "parent"))
        case "chatting" =>
          logger.info(s"setting chatting to ${state.actorName}")
          getChild(state.actorName) ! ActorState("chatting", "parent")
          idleChildren += (state.actorName -> state)
        case "awaitingRegister" =>
          getChild(state.actorName) ! ActorState("awaitingRegister", "parent")
          idleChildren += (state.actorName -> state)
        case _ =>
          idleChildren += (state.actorName -> state)
      }
  }

  private var idleChildren = Map.empty[String, ActorState]

  def getChild(id: String): ActorRef =
    context.child(id).getOrElse {
      idleChildren.get(id) match {
        case Some(state: ActorState) =>
          logger.info(s"loading actor id $id with state $state")
          val child = context.actorOf(Props(new UserActor(cache, usersDao, quizResultsDao, quizQuestionDao)), id)
          child ! state
          child
        case None =>
          logger.info(s"creating new actor with id $id")
          val child = context.actorOf(Props(new UserActor(cache, usersDao, quizResultsDao, quizQuestionDao)), id)
          child ! ActorState("init", "parent")
          child
      }
    }
}
