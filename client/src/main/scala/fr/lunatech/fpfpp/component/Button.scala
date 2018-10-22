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

  case class Props(icon: String, action: Callback, style: StyleA = Style.primary, id: String)

  class Backend($: BackendScope[Props, Unit]) {
    def render(props: Props) = {
      <.button(
        Style.button,
        props.style,
        ^.id := props.id,
        ^.className := s"fa ${props.icon}",
        ^.onClick --> props.action
      )
    }
  }

  object Style extends StyleSheet.Inline {
    import dsl._

    val button = style(
      verticalAlign.middle,
      padding(0.px),
      margin(0.px),
      borderRadius(50.%%),
      boxShadow := "0 3px 5px -1px rgba(0,0,0,.2), 0 6px 10px 0 rgba(0,0,0,.14), 0 1px 18px 0 rgba(0,0,0,.12)",
      cursor.pointer,
      &.focus(
        outline.none
      )
    )

    val primary = style(
      fontSize(3.em),
      height(70.px),
      width(70.px)
    )

    val secondary = style(
      fontSize(2.em),
      height(56.px),
      width(56.px)
    )
  }

  Style.addToDocument()
}
