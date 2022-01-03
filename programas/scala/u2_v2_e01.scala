sealed trait Resultado

final case class RInt(int:Int) extends Resultado
final case object RNada extends Resultado

def division(a:Int,b:Int):Resultado =
  if (b == 0) RNada else RInt(a / b)

def esCero(r:Resultado):Boolean = r match {
  case RInt(0) => true
  case _       => false
}

val uno:Resultado = RInt(1)

def incr(r:Resultado):Resultado = r match {
  case rp @ RInt(i) => rp.copy(int = i + 1)
  case j            => j
}
