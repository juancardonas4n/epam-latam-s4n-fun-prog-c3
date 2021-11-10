def funcion(u:(Int,Int),v:(Int,Int)) = (u,v) match {
  case ((w,0),(0,z)) => w + z
  case ((w,1),(1,z)) => w - z
  case ((w,x),(y,z)) if x == y => w * z
  case _             => 0
}
