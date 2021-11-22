def tupla2(a:Any, b:Any) = (a,b)
def tupla3(a:Any, b:Any, c:Any) = (a,b,c)
def tupla4(a:Any, b:Any, c:Any, d:Any) = (a,b,c,d)

val tp2 = tupla2(12,"Hola")
val tp3 = tupla3(2.23, true, 'a')
val tp4 = tupla4(tp2,tp3,(tp2,tp3),(1,(tp2,tp3)))
