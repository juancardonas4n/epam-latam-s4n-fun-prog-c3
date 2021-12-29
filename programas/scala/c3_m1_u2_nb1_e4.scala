sealed trait Coord
final case class P2D(x:Int,y:Int) extends Coord

sealed trait Direccion
final case object Norte     extends Direccion
final case object Sur       extends Direccion
final case object Oriente   extends Direccion
final case object Occidente extends Direccion

sealed trait Jugador
final case class JugadorInfo(coord:Coord,dir:Direccion) extends Jugador

def cambiarDir(ndir:Direccion,jugador:Jugador):Jugador = jugador match {
    case JugadorInfo(coord,_) => JugadorInfo(coord,ndir)
}

val cambiarDirNorte     = cambiarDir(Norte, _)
val cambiarDirSur       = cambiarDir(Sur, _)
val cambiarDirOriente   = cambiarDir(Oriente, _)
val cambiarDirOccidente = cambiarDir(Occidente,_)

val moverJugador = (jugador:Jugador) => jugador match {
  case JugadorInfo(P2D(x,y),d @ Norte)     => JugadorInfo(P2D(x, y + 1),d)
  case JugadorInfo(P2D(x,y),d @ Sur)       => JugadorInfo(P2D(x, y - 1),d)
  case JugadorInfo(P2D(x,y),d @ Occidente) => JugadorInfo(P2D(x - 1, y),d)
  case JugadorInfo(P2D(x,y),d @ Oriente)   => JugadorInfo(P2D(x + 1, y),d)
}

// cambiarDirSur(moverJugador(cambiarDirOriente(moverJugador(JugadorInfo(P2D(0,0),Norte)))))
