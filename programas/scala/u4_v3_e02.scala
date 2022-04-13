
val lstVacia:List[Int] = List()
val lstUnitaria:List[Int] = List(1)
val lstVarios:List[Int] = List(1,2,3,4)
val opEntero:Option[Int] = Some(1)
val opNoEntero:Option[Int] = Option.empty // None
type ES[A] = Either[String,A]
val eiEntero:ES[Int] = Right(1)
val eiNoEntero:ES[Int] = Left("Error")
