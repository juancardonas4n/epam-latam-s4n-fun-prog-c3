type T2I = (Int,Int)

// -1: t1 menor que t2
//  0: t1 igual a t2
//  1: t1 mayor que t2
def compare(t1:T2I, t2:T2I) = (t1,t2) match {
  case ((a,b),(c,d)) if a == c => b.compare(d)
  case ((a,_),(c,_))           => a.compare(c)
} 
