package club.malygin.data.pg.model

case class QuizResults(
    id: String,
    userId: Long,
    quizId: String,
    result: Boolean
)
