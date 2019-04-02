package club.malygin.web.model

case class Message(
    message_id: Long,
    from: Option[User] = None,
    date: Int,
    chat: Chat,
    forward_from: Option[User] = None,
    forward_from_chat: Option[Chat] = None,
    forward_from_message_id: Option[Int] = None,
    forward_signature: Option[String] = None,
    forward_date: Option[Int] = None,
    reply_to_message: Option[Message] = None,
    edit_date: Option[Int] = None,
    media_group_id: Option[String] = None,
    author_signature: Option[String] = None,
    text: Option[String] = None,
    entities: Option[Seq[MessageEntity]] = None,
    caption_entities: Option[Array[MessageEntity]] = None,
    audio: Option[Audio] = None,
    document: Option[Document] = None,
    animation: Option[Animation] = None,
    photo: Option[Seq[PhotoSize]] = None,
    sticker: Option[Sticker] = None,
    video: Option[Video] = None,
    voice: Option[Voice] = None,
    video_note: Option[VideoNote] = None,
    caption: Option[String] = None,
    contact: Option[Contact] = None,
    location: Option[Location] = None,
    venue: Option[Venue] = None,
    new_chat_members: Option[Array[User]] = None,
    left_chat_member: Option[User] = None,
    new_chat_title: Option[String] = None,
    new_chat_photo: Option[Seq[PhotoSize]] = None,
    delete_chat_photo: Option[Boolean] = None,
    group_chat_created: Option[Boolean] = None,
    supergroup_chat_created: Option[Boolean] = None,
    channel_chat_created: Option[Boolean] = None,
    migrate_to_chat_id: Option[Long] = None,
    migrate_from_chat_id: Option[Long] = None,
    pinned_message: Option[Message] = None
    /**
  * Not suppported
  *
  * connected_website: Option[String] = None,
  * game: Option[Game] = None,
  * invoice: Option[Invoice] = None,
  * successful_payment: Option[Payment] = None,
  * passport_data: Option[String] = None */
) {

  def source: Long = chat.id
}
