def distRango(a:Int, b:Int):Tuple2[Int,Tuple2[Int,Int]] = new Tuple2(scala.math.abs(a - b), if (a <= b) new Tuple2(a,b) else new Tuple2(b,a)) 
