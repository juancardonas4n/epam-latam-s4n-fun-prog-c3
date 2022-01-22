
trait F { // ...
}

trait A {
  def f:F
}

final case class B() extends A {
  def f:F = ???
}

final case class C() extends A {
  def f:F = ???
}

