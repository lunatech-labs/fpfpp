package fr.lunatech.fpfpp

import fr.lunatech.fpfpp.component.{Button, Card}
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object HomePage {

  def apply() = ScalaComponent
    .static("Hello")(
      <.div(
        ^.className:="fpfpp",
        <.div(
          ^.className:="fpfpp-cards",
          Card()
        ),
        <.div(
          ^.className:="fpfpp-buttons",
          Button(Button.Props("nop", "fa-remove", Callback.empty)),
          Button(Button.Props("refresh", "fa-refresh", Callback.empty)),
          Button(Button.Props("love", "fa-heart", Callback.empty))
        )
      )
    )
}
