def tuplax2(a:Any, b:Any) = new Tuple2(a,b)
def tuplax3(a:Any, b:Any, c:Any) = new Tuple3(a,b,c)
def tuplax4(a:Any, b:Any, c:Any, d:Any) = new Tuple4(a,b,c,d)

val tpx2 = tuplax2(12, "Hola")
val tpx3 = tuplax3(2.23, true, 'a')
val tpx4 = tuplax4(tpx2,tpx3,(tpx2,tpx3),(1,(tpx2,tpx3)))

println(tp2 == tpx2)
println(tp3 == tpx3)
println(tp4 == tpx4)
