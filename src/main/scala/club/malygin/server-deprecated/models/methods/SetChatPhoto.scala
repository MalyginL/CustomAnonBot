package club.malygin.server.models.methods

import club.malygin.server.models.input_media.InputFile

case class SetChatPhoto(chat_id: Int, photo: InputFile)
