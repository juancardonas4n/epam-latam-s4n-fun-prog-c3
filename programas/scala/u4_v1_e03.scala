
class ClassA(a:Int, b:Int) {
  val c = a + b
  override def toString:String = s"ClassA($a,$b,$c)"
}
