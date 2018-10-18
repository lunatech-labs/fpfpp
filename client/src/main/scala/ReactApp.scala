import fr.lunatech.fpfpp.AppRouter

import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom

object ReactApp {

  @JSExport
  def main(args: Array[String]): Unit = {
    val appContent = dom.document.getElementById("app")
    AppRouter.router.mapUnmounted(_.renderIntoDOM(appContent))
  }
}
