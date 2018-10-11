package fr.lunatech.fpfpp

import fr.lunatech.fpfpp.component.Card
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object HomePage {

  def apply() = ScalaComponent
    .static("Hello")(
      <.div(
        Card()
      )
    )
}
