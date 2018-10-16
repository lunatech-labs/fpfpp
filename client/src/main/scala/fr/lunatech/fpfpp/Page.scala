package fr.lunatech.fpfpp

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object Page {

  def apply(props: Props) = component(props)

  val component =
    ScalaComponent
      .builder[Props]("Page2")
      .renderBackend[Backend]
      .build

  case class Props(
      number: Int
  )

  class Backend($ : BackendScope[Props, _]) {

    def render(props: Props) =
      <.div(s"Je suis la page ${props.number}")
  }
}
