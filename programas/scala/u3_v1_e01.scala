import scala.annotation.tailrec

sealed trait Nat

final case object Cero extends Nat
final case class  Suc(n:Nat) extends Nat

def fromIntToNat(i:Int):Nat = i match {
  case 0 => Cero
  case n => Suc(fromIntToNat(n-1))
}

// val uno  = fromIntToNat(1)
// val dos  = fromIntToNat(2)
// val diez = fromIntToNat(10)

def fromNatToInt(nat:Nat):Int = ???

def sumaNat(m:Nat, p:Nat):Nat = ???
