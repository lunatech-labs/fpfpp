package fr.lunatech.fpfpp

import fr.lunatech.fpfpp.component.{Button, Card}
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}
import org.scalajs.dom
import org.scalajs.dom.KeyboardEvent
import org.scalajs.dom.ext.KeyCode

object HomePage {

  def apply(props: Props) = component(props)

  val component =
    ScalaComponent.builder[Props]("HomePage")
      .renderBackend[Backend]
      .componentDidMount(_.backend.start)
      .componentWillUnmount(_.backend.dispose)
      .build

  case class Props()

  class Backend($ : BackendScope[Props, Unit]) {

    private def iHaveFear: Callback = Callback { println("I have fear dude !!") }
    private def iHaveNotFear: Callback = Callback { println("I have not fear dude !!") }
    private def refresh: Callback = Callback { println("Refresh !!") }

    private def onKeyDown(event: KeyboardEvent): Unit = $.props.flatMap {
      props => event.keyCode match {
        case KeyCode.Left => iHaveNotFear
        case KeyCode.Right => iHaveFear
        case _ => Callback.empty
      }
    }.runNow()

    def start: Callback = Callback { dom.window.addEventListener("keydown", onKeyDown) }
    def dispose: Callback = Callback { dom.window.removeEventListener("keydown", onKeyDown) }

    def render(props: Props) =
      <.div(
        ^.className := "fpfpp",
        <.div(
          ^.className := "fpfpp-cards",
          Card()
        ),
        <.div(
          ^.className := "fpfpp-buttons",
          Button(Button.Props("nop", "fa-ban", iHaveNotFear)),
          Button(Button.Props("refresh", "fa-sync", refresh)),
          Button(Button.Props("love", "fa-heart", iHaveFear))
        )
      )
  }

}
