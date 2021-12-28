
sealed trait Result

final case class RInt(int:Int) extends Result
final case object RNothing extends Result
