package fr.lunatech.fpfpp.component

import fr.lunatech.fpfpp.Image
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.DevDefaults._
import scalacss.ScalaCssReact._
import scalacss.internal.mutable.StyleSheet

object Card {

  object Style extends StyleSheet.Inline {

    import dsl._

    val card = style(
      display.inlineBlock,
      borderRadius(8.px),
      overflow.hidden,
      height(100.%%),
      width(100.%%),
      backgroundPosition := "center",
      backgroundSize := "cover",
      boxShadow := "0 3px 1px -2px rgba(0,0,0,.2), 0 2px 2px 0 rgba(0,0,0,.14), 0 1px 5px 0 rgba(0,0,0,.12)"
    )
  }

  Style.addToDocument()

  case class Props(
    image: Image
  )

  val component = ScalaComponent
    .builder[Props]("Card")
    .render_P { props =>
      <.div(
        Style.card,
        ^.backgroundImage := s"url(${props.image.url})"
      )
    }
    .build

  def apply(props: Props): VdomNode = component(props)

}
