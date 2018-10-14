package fr.lunatech.fpfpp.component

import fr.lunatech.fpfpp.Image
import japgolly.scalajs.react.{BackendScope, ScalaComponent}
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.DevDefaults._
import scalacss.ScalaCssReact._
import scalacss.internal.mutable.StyleSheet
object Card {

  case class Props(images: Seq[Image])
  case class State(
      image: Option[Image] = None
  )
  class Backend($ : BackendScope[Props, State]) {

    def render(props: Props, state: State) = {
      <.div(
        state.image.map { im =>
          <.img(Style.img, ^.key := im.name, ^.src := im.url)()
        }
      )
    }
  }

  val component = ScalaComponent
    .builder[Props]("Card")
    .initialStateFromProps(props => State(image = props.images.lift(2)))
    .renderBackend[Backend]
    .componentWillReceiveProps(o =>
      o.modState(_.copy(image = o.nextProps.images.lift(2))))
    .build

  def apply(props: Props) = component(props)

  object Style extends StyleSheet.Inline {
    import dsl._
    val img = style(
      width(100.%%)
    )
  }

  Style.addToDocument()
}
