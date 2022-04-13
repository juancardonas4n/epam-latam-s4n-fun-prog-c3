import scala.util.Either._

case class InfoUsuario(...)
type ES[A] = Either[String,A]

def consultaUsuario(usuarioID:String):ES[InfoUsuario]        = ???
def actualizarSaldo(retiro:Double,
                    infoUsuario:InfoUsuario):ES[InfoUsuario] = ???


def computarRetiro(retiro:Double,usuarioID:String)
:ES[InfoUsuario] = for {
  info  <- consultaUsuario(usuarioID)
  info2 <- actualizarSaldo(retiro,info)
} yield info2



