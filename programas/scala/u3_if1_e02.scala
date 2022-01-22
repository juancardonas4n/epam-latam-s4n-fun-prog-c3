
trait A {
  def d:String = "Un A"
}

class B extends A {
  override def d = "Un B"
}

class C extends A {
  override def d = "Un C"
}

