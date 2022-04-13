
def doblar_lista(lst:List[Int]):List[Int] = lst match {
  case Nil     => Nil
  case x :: xs => (x * 2) :: doblar_lista(xs)
}

def doblar_opcion(opt:Option[Int]):Option[Int] = opt match {
  case Some(x) => Some(x * 2)
  case None    => None
}

def doblar_cualquiera(ei:ES[Int]):ES[Int] = ei match {
  case Right(x) => Right(x * 2)
  case Left(y)  => Left(y)
}
