package club.malygin.telegram

import java.util.concurrent.Executors

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import club.malygin.TestConfig
import club.malygin.data.cache.{DatabaseCacheLoader, UserPairCache, UserPairCacheImpl}
import club.malygin.data.dataBase.pg.dao.{QuizQuestionService, QuizResultsService, UsersService}
import club.malygin.web.model.{Chat, Message, Update, User}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, Matchers, WordSpecLike}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class RouterActorTest
    extends TestKit(ActorSystem("test"))
    with ImplicitSender
    with Matchers
    with FlatSpecLike
    with BeforeAndAfterAll
    with MockFactory {
  implicit val ec     = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))
  val userService     = new UsersService(TestConfig.testdb)
  val questionService = new QuizQuestionService(TestConfig.testdb)
  val resultsService  = new QuizResultsService(TestConfig.testdb)

  override def afterAll: Unit =
    TestKit.shutdownActorSystem(system)

  "Router actor" should "return states on ?" in {
    val testProbe                    = TestProbe()
    val k: UserPairCache[Long, Long] = new UserPairCacheImpl(new DatabaseCacheLoader(userService))
    val testActor = TestActorRef(Props(new RouterActor(k, userService, resultsService, questionService) {
      override def getChild(id: String): ActorRef = testProbe.ref
    }))
    testActor ! ActorState(value = "?", actorName = "1")
    testProbe.expectMsg(500 millis, ActorState("init", "parent"))
  }

  "Router actor" should "return chatting on chatting" in {
    val testProbe                    = TestProbe()
    val k: UserPairCache[Long, Long] = new UserPairCacheImpl(new DatabaseCacheLoader(userService))
    val testActor = TestActorRef(Props(new RouterActor(k, userService, resultsService, questionService) {
      override def getChild(id: String): ActorRef = testProbe.ref
    }))
    testActor ! ActorState(value = "chatting", actorName = "1")
    testProbe.expectMsg(500 millis, ActorState("chatting", "parent"))
  }

  "Router actor" should "reroute message from telegram" in {
    val testProbe                    = TestProbe()
    val m                            = Message(13, Some(User(13, is_bot = false, "asd")), 1, Chat(13, "asd"))
    val k: UserPairCache[Long, Long] = new UserPairCacheImpl(new DatabaseCacheLoader(userService))
    val testActor = TestActorRef(Props(new RouterActor(k, userService, resultsService, questionService) {
      override def getChild(id: String): ActorRef = testProbe.ref
    }))
    testActor ! Update(13, Some(m))
    testProbe.expectMsg(500 millis, m)
  }

}
