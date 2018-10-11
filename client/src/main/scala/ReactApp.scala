import scala.scalajs.js.annotation.JSExport

import fr.lunatech.fpfpp.HomePage
import org.scalajs.dom

object ReactApp {

  @JSExport
  def main(args: Array[String]): Unit = {
    HomePage(HomePage.Props()).renderIntoDOM(dom.document.getElementById("app"))
  }
}
