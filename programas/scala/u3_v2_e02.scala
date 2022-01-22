sealed trait ArbolBinInt

final case class Hoja(n:Int) extends ArbolBinInt
final case class BinInt(i:ArbolBinInt, d:ArbolBinInt) extends ArbolBinInt

val ab1 = Hoja(1)
val ab2 = Hoja(2)
val ab3 = BinInt(ab1,ab2)
val ab4 = BinInt(BinInt(ab1,ab2),Hoja(3))
val ab5 = BinInt(BinInt(Hoja(5),BinInt(Hoja(4),Hoja(6))),BinInt(ab2,ab3))

def profArbolBin(ab:ArbolBinInt):Int = ab match {
  case Hoja(_) => 0
  case BinInt(i,d) => scala.math.max(profArbolBin(i),profArbolBin(d)) + 1
}

def sumaArbolBin(ab:ArbolBinInt):Int = ab match {
  case Hoja(v) => v
  case BinInt(i,d) => sumaArbolBin(i) + sumaArbolBin(d)
}
