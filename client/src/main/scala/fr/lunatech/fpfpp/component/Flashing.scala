package fr.lunatech.fpfpp.component

import fr.lunatech.fpfpp.component.Swipeable.Direction
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.DevDefaults._
import scalacss.ScalaCssReact._
import scalacss.internal.mutable.StyleSheet

import scala.concurrent.duration._

object Flashing {

  object Style extends StyleSheet.Inline {

    import dsl._

    val flash = keyframes(
      0.%% -> keyframe(opacity(0)),
      50.%% -> keyframe(opacity(1)),
      100.%% -> keyframe(opacity(0)),
    )

    val swiped = style(
      position.absolute,
      animationName(flash),
      animationDuration(150.millis),
      width(25.%%),
      height(100.%%),
      opacity(0)
    )

    val swipedLeft = style(
      left.`0`,
      background := "linear-gradient(to right, rgba(255,0,0,0.5) 0%,rgba(255, 0, 0,0) 100%)"
    )

    val swipedRight = style(
      right.`0`,
      background := "linear-gradient(to left, rgba(0,255,0,0.5) 0%,rgba(0, 255, 0,0) 100%)"
    )
  }

  Style.addToDocument()


  val component = ScalaComponent.builder[Direction]("Flashing")
    .render($ =>
      <.div(
        Style.swiped,
        Style.swipedLeft.when($.props == Direction.Left),
        Style.swipedRight.when($.props == Direction.Right)
      )
    )
    .build

  def apply(props: Direction) = component(props)

}
