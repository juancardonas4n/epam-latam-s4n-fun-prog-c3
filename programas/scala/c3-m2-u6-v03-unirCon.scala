import com.epam.inmutable.lista._

val lst1 = Lista(1,2,3,4)
val lst2 = Lista(5,6,7)

def unirCon[A,B,C](lstA:Lista[A])(lstB:Lista[B])(f:(A,B)=>C):
Lista[C] = ???
