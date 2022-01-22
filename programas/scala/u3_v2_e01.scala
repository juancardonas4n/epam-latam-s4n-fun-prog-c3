
trait ListaInt

final case object VaciaInt extends ListaInt
final case class ConsInt(n:Int, lst:ListaInt) extends ListaInt

val lst1 = VaciaInt
val lst2 = ConsInt(3, ConsInt(2, VaciaInt))
val lst3 = ConsInt(9, lst2)

def longListaInt(lst:ListaInt):Int = lst match {
  case VaciaInt     => ???
  case ConsInt(i,l) => ???
}

def sumaListaInt(lst:ListaInt):Int = lst match {
  case VaciaInt     => ???
  case ConsInt(i,l) => ???
}
