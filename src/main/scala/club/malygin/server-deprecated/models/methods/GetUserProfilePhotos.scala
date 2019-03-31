package club.malygin.server.models.methods

case class GetUserProfilePhotos(
                                 user_id:Int,
                                 offset:Option[Int]=None,
                                 limit:Option[Int]=None,
                               )

