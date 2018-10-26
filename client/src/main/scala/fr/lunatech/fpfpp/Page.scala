package fr.lunatech.fpfpp

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object Page {

  def apply(props: Props) = component(props)

  val component =
    ScalaComponent
      .builder[Props]("Page")
      .render_P(content)
      .build

  case class Props(
      year: String
  )

  def content(props: Props) =
    <.div(
      <.img(^.src := "pumpkin.png"),
      <.input.submit(
        ^.value := "🎃",
        ^.onClick --> Callback.alert(s"Happy Halloween ${props.year}!")
      )
    )

}
