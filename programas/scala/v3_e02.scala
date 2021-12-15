
def multRap(tpl:(Int,Int)) = tpl match {
  case (0,_) => 0
  case (_,0) => 0
  case (a,b) => a * b
}
