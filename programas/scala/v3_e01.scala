
def suma3_(tpl:(Double,Double,Double)) =
  tpl._1 + tpl._2 + tpl._3

def suma3(tpl:(Double,Double,Double)) = tpl match {
  case (a,b,c) => a + b + c
}
