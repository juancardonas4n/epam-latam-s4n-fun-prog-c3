
sealed trait Tiempo

final case class  Reg(seg:Int) extends Tiempo
final case object Fallo extends Tiempo

object Tiempo {
  def apply(str:String):Tiempo =
    if (str.startsWith("*")) Fallo else Reg(str.toInt)

  def suma(a:Tiempo, b:Tiempo):Tiempo = (a,b) match {
    case (Fallo,_) => ???
    case (_,Fallo) => ???
    case (Reg(n),Reg(m)) => ???
  }
}
