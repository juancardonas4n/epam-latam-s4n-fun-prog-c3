type T4Mangle = (((Int,Int),Int),Int)
type T4       = Tuple4[Int,Int,Int,Int]

def unMangle(tpl:T4Mangle):T4 = (tpl._1._1._1, tpl._1._1._2, tpl._1._2, tpl._2) 
