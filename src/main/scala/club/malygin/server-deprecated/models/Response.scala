package club.malygin.server.models

case class Response[R](
                        ok          : Boolean,
                        result      : Option[R] = None,
                        description : Option[String] = None,
                        errorCode   : Option[Int] = None,
                        parameters  : Option[ResponseParameters] = None
                      )
