package fr.lunatech.fpfpp.component

import japgolly.scalajs.react.{ BackendScope, ScalaComponent }
import japgolly.scalajs.react.vdom.html_<^._

object Card {

  case class Props()
  class Backend($: BackendScope[Props, Unit]) {

    def render() = {
      <.div(
        "TOTO"
      )
    }
  }

  val component = ScalaComponent
    .builder[Props]("Card")
    .renderBackend[Backend]
    .build

  def apply() = component(Props())

}
