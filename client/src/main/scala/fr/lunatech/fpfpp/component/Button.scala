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

  case class Props(name: String, icon: String, action: Callback)

  class Backend($: BackendScope[Props, Unit]) {
    def render(props: Props) =
      <.button(Style.button, ^.id := props.name, ^.className := s"fa ${props.icon}")
  }

  object Style extends StyleSheet.Inline {
    import dsl._
    val button = style(
      width(40.px),
      height(40.px),
      fontSize(32.px),
      verticalAlign.middle,
      padding(0.px),
      margin(0.px)
    )
  }

  Style.addToDocument()
}
