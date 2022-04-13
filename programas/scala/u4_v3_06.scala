
def leerEnt:IO[Int] = for {
  _ <- IO { print("Escriba un entero: ") }
  _ <- IO { Console.out.flush }
  v <- IO { Console.in.readLine }
} yield v.toInt
