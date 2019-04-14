package club.malygin.data.dataBase.pg

import club.malygin.data.dataBase.pg.dao.QuizQuestionDao
import club.malygin.data.dataBase.pg.model.QuizQuestions
import javax.inject.Inject

import scala.concurrent.Future

class QuestionService @Inject()(quizQuestionDao: QuizQuestionDao) {

  def getCurrentQuestions: Future[Seq[QuizQuestions]] = quizQuestionDao.getActive

}
