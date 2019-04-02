package club.malygin.server.models.input_media

trait InputFile

object InputFile {
  final case class FileId(fileId: String)                            extends InputFile
  final case class Path(path: java.nio.file.Path)                    extends InputFile
  final case class Contents(filename: String, contents: Array[Byte]) extends InputFile

  def apply(fileId: String): InputFile           = FileId(fileId)
  def apply(path: java.nio.file.Path): InputFile = Path(path)

  def apply(filename: String, contents: Array[Byte]): InputFile =
    Contents(filename, contents)
}
