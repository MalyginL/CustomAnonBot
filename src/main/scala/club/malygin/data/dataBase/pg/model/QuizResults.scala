package club.malygin.data.dataBase.pg.model

import java.util.UUID

case class QuizResults(
                        id: UUID,
                        userId: Long,
                        quizId: UUID,
                        result: Boolean
)
