package fr.lunatech.fpfpp

import fr.lunatech.fpfpp.component.{Button, Card}
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}

object HomePage {

  def apply(props: Props) = component(props)

  val component =
    ScalaComponent.builder[Props]("HomePage")
      .renderBackend[Backend]
      .build

  case class Props()

  class Backend($ : BackendScope[Props, Unit]) {

    def render(props: Props) =
      <.div(
        ^.className := "fpfpp",
        <.div(
          ^.className := "fpfpp-cards",
          Card()
        ),
        <.div(
          ^.className := "fpfpp-buttons",
          Button(Button.Props("nop", "fa-remove", Callback.empty)),
          Button(Button.Props("refresh", "fa-refresh", Callback.empty)),
          Button(Button.Props("love", "fa-heart", Callback.empty))
        )
      )
  }

}
