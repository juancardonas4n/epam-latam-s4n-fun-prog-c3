
// tupla.productArity
def nElem(x:Any):Int = x match {
  case _: (_,_)     => 2
  case _: (_,_,_)   => 3
  case _: (_,_,_,_) => 4
  case _            => 0
}
