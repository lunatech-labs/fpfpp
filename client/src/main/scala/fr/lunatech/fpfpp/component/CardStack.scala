package fr.lunatech.fpfpp.component

import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{ BackendScope, ScalaComponent }
import scalacss.DevDefaults._
import scalacss.ScalaCssReact._
import scalacss.internal.mutable.StyleSheet

object CardStack {

  case class Props(
    cards: Seq[Card.Props]
  )

  object Style extends StyleSheet.Inline {

    import dsl._

    val stack = style(
      position.relative
    )

    val stackCard = style(
      position.absolute,
      top.`0`,
      width(250.px),
      height(450.px)
    )
  }

  Style.addToDocument()

  class Backend($: BackendScope[Props, Unit]) {

    def renderCard: (Card.Props, Int) => TagMod = {
      case (cardProps, i) =>
        <.div(
          Style.stackCard,
          ^.zIndex := s"${5 - i}",
          ^.top := ((5 - i) * 10).px,
          Card(cardProps)
        )
    }

    def render(props: Props) = {
      <.div(Style.stack,
        TagMod {
         val res = props.cards.take(5).zipWithIndex match {
            case head :: tail => Swipeable(Swipeable.Props(head)) :+ tail.map(renderCard)
            case o => Seq(<.div("empty"))
          }
          res:_*
        }
      )
    }

    val component = ScalaComponent
      .builder[Props]("CardStack")
      .renderBackend[Backend]
      .build

    def apply(props: Props) = component(props)

  }

}
