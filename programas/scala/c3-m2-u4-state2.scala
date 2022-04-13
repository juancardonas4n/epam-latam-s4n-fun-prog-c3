
case class State[S,A](f:S => (S,A))
