
def obtTuplaBin(tpl:Tuple2[Any,Any]):(Any,Any) = tpl match {
  case (t @ (_,_),_)  => t
  case (_,t @(_,_))   => t
}
