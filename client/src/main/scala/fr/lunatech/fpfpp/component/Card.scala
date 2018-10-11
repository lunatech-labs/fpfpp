package fr.lunatech.fpfpp.component

import fr.lunatech.fpfpp.model.Profile
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.OnUnmount
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
      backgroundSize := "cover"
    )
  }

  Style.addToDocument()

  case class Props(
    profile: Profile,
    fp: Callback,
    fpp: Callback
  )


  class Backend($: BackendScope[Props, Unit]) extends OnUnmount {

    def render(props: Props) = {

      <.div(
        Style.card,
        ^.backgroundImage := s"url(${props.profile.image})"
      )
    }
  }

  val component = ScalaComponent
    .builder[Props]("Card")
    .renderBackend[Backend]
    .build

  def apply(props: Props) = component(props)

}
