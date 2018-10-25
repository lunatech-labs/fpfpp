import fr.lunatech.fpfpp.utils.ApiClient
import fr.lunatech.fpfpp.{AppRouter, HomePage}

import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom

object ReactApp {

  @JSExport
  def main(args: Array[String]): Unit = {
    val appContent = dom.document.getElementById("app")

//    HomePage(HomePage.Props(ApiClient)).renderIntoDOM(appContent)
    AppRouter.router.mapUnmounted(_.renderIntoDOM(appContent))
  }

}
