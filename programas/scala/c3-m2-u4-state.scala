import cats._
import cats.implicits._
import cats.data._

def contadorSinEstado(actual:Int):(Int,Int) = (actual + 1, actual)

def contarTresVecesSinEstado():Int = {
  val (s,_) = contadorSinEstado(1)
  val (s1,_) = contadorSinEstado(s)
  val (_,c)  = contadorSinEstado(s1)
  c
}

type Contador[A] = State[Int,A]

def contadorEstado:Contador[Int] = for {
  c <- State.get[Int]
  _ <- State.modify[Int](s => s + 1)
} yield c

def contarTresVecesConEstado:Contador[Int] = for {
  _ <- contadorEstado
  _ <- contadorEstado
  r <- contadorEstado
} yield r


