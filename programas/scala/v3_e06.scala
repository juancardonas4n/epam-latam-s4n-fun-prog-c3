
type T2I   = (Int, Int)
type TBT2I = (T2I, T2I)

def sumarElem(tpl:TBT2I) =
  tpl._1._1 + tpl._1._2 + tpl._2._1 + tpl._2._2


def sumaElem2(tpl:TBT2I) = tpl match {
   case ((a,b),(c,d)) => a + b + c + d
}
