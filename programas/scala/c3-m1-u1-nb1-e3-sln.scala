val aplicarTuplas = (a:(Int,String),b:(Int,String),(c:((Int,Int) => Int, (String, String) => String))) => (c._1(a._1, b._1),c._2(a._2, b._2)) 
