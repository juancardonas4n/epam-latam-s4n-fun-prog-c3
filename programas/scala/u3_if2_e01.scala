
trait C { // ...
}
trait D { // ...
}
trait F { // ...
}

trait A {
  def e:F
}

case class B(c:C, d:D) extends A {
  def e:F = ???
}

