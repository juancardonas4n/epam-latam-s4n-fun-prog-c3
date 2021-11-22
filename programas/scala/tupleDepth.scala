def funcion(a:Any):Int = a match {
    case (l @ (a,b), r @ (c,d)) => scala.math.max(funcion(l), funcion(r))
    case (l @ (a,b), _) => 1 + funcion(l)
    case (_, r @(c,d)) => 1 + funcion(r)
    case (_, _) => 1
    case (_) => 0
}
