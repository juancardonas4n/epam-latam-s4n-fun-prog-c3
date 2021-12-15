type T2ID = (Int,Double)
type T2DI = (Double,Int)

// tpl:(Any,Any) tpl.swap
def swapImpl(tpl:T2ID):T2DI = tpl match {
  case (a, b) => (b, a)
}
