type T3IID = (Int,Int,Double)
type T3IDI = (Int,Double,Int)

def aplicar(tpl1:T3IID,tpl2:T3IDI,f:(Int,Double) => Int) = f(tpl1._1,tpl2._2) + f(tpl2._3,tpl1._3)

