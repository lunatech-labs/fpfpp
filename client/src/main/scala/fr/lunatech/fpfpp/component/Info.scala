package fr.lunatech.fpfpp.component

import fr.lunatech.fpfpp.component.Swipeable.Direction
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^._

object Info {

  case class Props(
    history: Seq[Direction],
    nbInitialImages: Int,
    imagesCount: Int,
  )

  val component = ScalaComponent
    .builder[Props]("Info")
    .render { $ =>
      <.div(
        ^.visibility := "hidden",
        <.span(
          <.span("# FaitPeur"),
          <.span(^.id := "FaitPeur", $.props.history.count(_ == Direction.Left))
        ),
        <.span(
          <.span("# FaitPasPeur"),
          <.span(^.id := "FaitPasPeur", $.props.history.count(_ == Direction.Right))
        ),
        <.span(
          <.span("# remaining images"),
          <.span(^.id := "remainingImages", $.props.imagesCount)
        ),
        <.span(
          <.span("# total images"),
          <.span(^.id := "totalImages", $.props.nbInitialImages)
        )
      )
    }
    .build

  def apply(props: Props) = component(props)

}
