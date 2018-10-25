package external

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

@JSGlobal("Konami")
@js.native
object KonamiCode extends js.Function1[js.Function1[Unit, Unit], Konami] {

  override def apply(f: js.Function1[Unit, Unit]): Konami = js.native

}

@js.native
trait Konami extends js.Object {
  def load(): Unit = js.native
}
