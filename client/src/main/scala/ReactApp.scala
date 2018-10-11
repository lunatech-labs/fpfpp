import fr.lunatech.fpfpp.HomePage
import org.scalajs.dom

import scala.scalajs.js.annotation.JSExport

object ReactApp {

  @JSExport
  def main(args: Array[String]): Unit = {
    HomePage().mapUnmounted(_.renderIntoDOM(dom.document.getElementById("app")))
  }
}
