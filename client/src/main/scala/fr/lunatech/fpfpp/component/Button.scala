package fr.lunatech.fpfpp.component

import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}
import scalacss.DevDefaults._
import scalacss.ScalaCssReact._
import scalacss.internal.mutable.StyleSheet

object Button {

  val component = ScalaComponent
    .builder[Props]("Button")
    .renderBackend[Backend]
    .build

  def apply(props: Props) = component(props)

  case class Props(icon: String, action: Callback)

  class Backend($: BackendScope[Props, Unit]) {
    def render(props: Props) =
      <.button(
        Style.button,
        ^.className := s"fa ${props.icon}",
        ^.onClick --> props.action
      )
  }

  object Style extends StyleSheet.Inline {
    import dsl._
    val button = style(
      fontSize(3.em),
      verticalAlign.middle,
      padding(0.px),
      margin(0.px)
    )
  }

  Style.addToDocument()
}
