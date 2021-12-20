type Notas = (Double,Double,Double)
type Estudiante = (String,Notas,Double)

def actualizarNotas(est:Estudiante,nroNota:Int,nuevaNota:Double):Estudiante = {
  def cambiarNota(notas:Notas):Notas = nroNota match {
    case 1 => notas.copy(_1 = nuevaNota)
    case 2 => notas.copy(_2 = nuevaNota)
    case 3 => notas.copy(_3 = nuevaNota)
  }
  def promNotas(n:Notas):Double = {
    (n._1 + n._2 + n._3) / 3 
  }
  val nuevaNotas = cambiarNota(est._2)
  est.copy(_2 = nuevaNotas, _3 = promNotas(nuevaNotas))
}

