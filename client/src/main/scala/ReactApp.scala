import fr.lunatech.fpfpp.{ AppRouter, HomePage }
import org.scalajs.dom

import scala.scalajs.js.annotation.JSExport

object ReactApp {

  @JSExport
  def main(args: Array[String]): Unit = {
    val router = AppRouter.router
    //router().renderIntoDOM(dom.document.body)
    HomePage().mapUnmounted(_.renderIntoDOM(dom.document.getElementById("app")))
  }
}
