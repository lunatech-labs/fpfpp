package fr.lunatech.fpfpp.component

import scalacss.internal.mutable.StyleSheet
import scalacss.DevDefaults._
import scalacss.ScalaCssReact._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

import scala.concurrent.duration._

object BluredBackgound {

  object Style extends StyleSheet.Inline {

    import dsl._

    val bluredBackground = style(
      backgroundSize := "cover",
      backgroundPosition := "center",
      position.absolute,
      width(100.%%),
      height(100.%%),
      filter := "blur(15px) opacity(50%)",
      transitionProperty := "background-image",
      transitionDuration(150.millis)
    )
  }

  Style.addToDocument()


  val component = ScalaComponent.builder[String]("Flashing")
    .render($ => <.div(
      Style.bluredBackground,
      ^.backgroundImage := s"url('${$.props}')"
    ))
    .build

  def apply(imgSrc: String) = component(imgSrc)

}
