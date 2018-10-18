package fr.lunatech.fpfpp.component

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.internal.mutable.StyleSheet
import scalacss.DevDefaults._
import scalacss.ScalaCssReact._

object Controls {

  object Style extends StyleSheet.Inline {

    import dsl._

    val button = style(
      position.absolute,
      zIndex(100)
    )

    val stickLeftCenter = style(
      left.`0`,
      top(50.%%),
      transform := "translate(-50%, -50%)"
    )

    val stickBottomCenter = style(
      left(50.%%),
      bottom.`0`,
      transform := "translate(-50%, -50%)"
    )

    val stickRightCenter = style(
      right.`0`,
      top(50.%%),
      transform := "translate(50%, -50%)"
    )
  }
  Style.addToDocument()

  case class Props(
    swipeLeft: Callback,
    refresh: Callback,
    swipeRight: Callback
  )

  val component = ScalaComponent.builder[Props]("Controls")
    .render($ =>
      <.div(
        <.div(Style.button, Style.stickLeftCenter, Button(Button.Props("fa-flask-poison", $.props.swipeLeft))),
        <.div(Style.button,
          Style.stickBottomCenter,
          Button(Button.Props("fa-hat-witch", $.props.refresh, Button.Style.secondary))),
        <.div(Style.button,
          Style.stickRightCenter,
          Button(Button.Props("fa-flask-potion", $.props.swipeRight))),
      )
    )
    .build

  def apply(props: Props) = component(props)

}
