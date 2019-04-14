package club.malygin.data.dataBase.pg.model

import java.util.UUID

case class QuizQuestions(
                          quizIdd: UUID,
                          text: String,
                          firstOption: String,
                          secondOption: String,
                          status: Boolean
                        )
