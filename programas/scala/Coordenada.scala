
sealed trait Coordenada

final case class Punto(x:Double, y:Double) extends Coordenada

val coord:Coordenada = Punto(0.0, 0.0)


