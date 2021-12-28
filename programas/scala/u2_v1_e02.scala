
sealed trait State

final case object Off extends State
final case object On  extends State

def com(s:State):State = s match {
  case Off => On
  case On  => Off
}
