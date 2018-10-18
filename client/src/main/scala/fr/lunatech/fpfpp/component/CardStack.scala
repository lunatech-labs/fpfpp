package fr.lunatech.fpfpp.component

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.DevDefaults._
import scalacss.ScalaCssReact._
import scalacss.internal.mutable.StyleSheet

object CardStack {

  object Style extends StyleSheet.Inline {

    import dsl._

    val stack = style(
      position.relative,
      height(100.%%),
      maxHeight :=! "calc(100% - 70px)",
      marginBottom(15.px)
    )

    val stackCard = style(
      position.absolute,
      top.`0`,
      height(100.%%),
      width(100.%%),
      boxSizing.borderBox,
      transition := "all 250ms"
    )
  }

  Style.addToDocument()


  case class Props(
    cards: Seq[(String, VdomNode)],
    leftTag: VdomNode,
    swipeLeft: Callback,
    rightTag: VdomNode,
    swipeRight: Callback
  )

  class Backend($: BackendScope[Props, Unit]) {

    val swipeableRef = Ref.toScalaComponent(Swipeable.component)

    val swipeLeft = swipeableRef.get.flatMap(_.backend.swipeLeft)
    val swipeRight = swipeableRef.get.flatMap(_.backend.swipeRight)

    val stackSize = 5

    def renderCard: (((String, VdomNode), Int)) => VdomNode = {
      case ((id, child), i) => <.div(
        Style.stackCard,
        ^.key := id,
        ^.zIndex := s"${stackSize - i}",
        ^.top := ((stackSize - i - 1) * 4).px,
        ^.padding := s"0 ${i * 2}px",
        child
      )
    }

    def render(p: Props) = {
      val stackPart = p.cards.take(stackSize)
      <.div(Style.stack,
        stackPart.zipWithIndex match {
          case ((id, head), 0) :: tail =>
            val swipe = swipeableRef.component(Swipeable.Props(p.leftTag, p.swipeLeft, p.rightTag, p.swipeRight))(head)
            val swipeable = renderCard(id -> swipe, 0)
            val res = swipeable +: tail.map(renderCard)
            TagMod(res: _*)
          case _ => <.div("empty")
        }
      )
    }
  }

  val component = ScalaComponent
    .builder[Props]("CardStack")
    .renderBackend[Backend]
    .build

  def apply(props: Props) = component(props)
}
