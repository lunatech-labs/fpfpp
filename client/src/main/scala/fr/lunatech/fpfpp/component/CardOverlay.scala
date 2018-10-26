package fr.lunatech.fpfpp.component

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.DevDefaults._
import scalacss.ScalaCssReact._
import scalacss.internal.Macros.Color
import scalacss.internal.mutable.StyleSheet

object CardOverlay {

  object Style extends StyleSheet.Inline {
    import dsl._

    val overlay = style(
      width(100.%%),
      height(100.%%),
      display.flex,
      justifyContent.center,
      alignItems.center,
      flexDirection.column,
      borderRadius(8.px)
    )

    val icon = style(
      addClassName("fa"),
      fontSize(5.em)
    )

    val text = style(
      transform := "rotate(-10deg)",
      fontSize(3.em)
    )

  }
  Style.addToDocument()

  case class Props(
    icon: String,
    text: String,
    color: Color,
    backgroundColor: Color
  )

  class Backend($: BackendScope[Props, Unit]) {

    def render(props: Props) = {
      <.div(
        Style.overlay,
        ^.backgroundColor := props.backgroundColor.value,
        <.div(Style.icon, ^.color := props.color.value, ^.cls := props.icon),
        <.div(Style.text, ^.color := props.color.value, props.text)
      )
    }
  }

  val component = ScalaComponent
    .builder[Props]("CardOverlay")
    .renderBackend[Backend]
    .build

  def apply = Props.tupled.andThen(component(_))
}
