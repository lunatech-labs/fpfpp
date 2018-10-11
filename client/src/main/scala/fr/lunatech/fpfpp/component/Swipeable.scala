package fr.lunatech.fpfpp.component

import fr.lunatech.fpfpp.component.Card.Props
import fr.lunatech.fpfpp.model.FPFPP
import fr.lunatech.fpfpp.model.FPFPP.{ FaitPasPeur, FaitPeur }
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react._
import org.scalajs.dom
import org.scalajs.dom.html.Image
import scalacss.DevDefaults._
import scalacss.ScalaCssReact._
import scalacss.internal.mutable.StyleSheet

import scala.concurrent.Future
import scala.concurrent.duration._

object Swipeable {

  object Style extends StyleSheet.Inline {

    import dsl._

    val moveBack = style(
      transition := "transform 200ms"
    )

  }

  Style.addToDocument()

  case class Props(
    node: VdomNode,
    fpp: Callback,
    fp: Callback
  )

  case class State(
    posX: Double,
    posY: Double,
    initalMousePosX: Double,
    initalMousePosY: Double,
    delta: Double,
    fpfpp: Option[FPFPP]
  )

  val emptyImg: Image = {
    val img = dom.document.createElement("img").asInstanceOf[Image]
    img.setAttribute("src", "data: image / gif;base64, R0lGODlhAQABAIAAAAAAAP ///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7")
    img
  }

  class Backend($: BackendScope[Props, State]) {

    def persistEvent(event: ReactDragEvent): Unit = {
      event.persist()
      event.dataTransfer.setDragImage(emptyImg, 0, 0)
    }

    def drag(event: ReactDragEvent): Callback = {
      persistEvent(event)
      $.modStateOption { state =>
        val mouseCurrPosX = event.clientX
        val mouseCurrPosY = event.clientY

        Some(mouseCurrPosX).filter(_ != 0).map(_ =>
          state.copy(
            posX = mouseCurrPosX - state.initalMousePosX,
            posY = mouseCurrPosY - state.initalMousePosY
          )
        )
      }
    }

    def fpfpp(state: State, props: Props): Option[FPFPP] = {
      if (state.posX < -delta) {
        Some(FaitPasPeur)
      } else if (state.posX > delta) {
        Some(FaitPeur)
      } else {
        None
      }
    }

    val delta = 200

    def handleDragEnd(event: ReactDragEvent): Callback = {
      persistEvent(event)
      val o = for {
        state <- $.state
        props <- $.props
      } yield fpfpp(state, props) match {
        case Some(FaitPasPeur) => props.fpp.delay(200.millis)
        case Some(FaitPeur) => props.fp.delay(200.millis)
        case None => CallbackTo(Future.successful(()))
      }

      o.flatten >>
        $.modState { (state, props) =>
          state.copy(
            posX = 0,
            posY = 0,
            initalMousePosX = 0,
            initalMousePosY = 0,
            fpfpp = fpfpp(state, props)
          )
        }
    }

    def handleDragStart(event: ReactDragEvent): Callback = {
      persistEvent(event)
      $.modState { state =>
        state.copy(
          initalMousePosX = event.clientX,
          initalMousePosY = event.clientY,
          fpfpp = None
        )
      }
    }

    def render(props: Props, state: State) = {

      def translateToString(x: Double, y: Double) = {
        val angle = (x / 9).min(25).max(-25)
        s"translate(${x}px, ${y}px) rotate(${angle}deg) perspective(800px)"
      }

      val translate = state.fpfpp match {
        case Some(FaitPeur) => translateToString(dom.window.innerWidth, state.posY)
        case Some(FaitPasPeur) => translateToString(-dom.window.innerWidth, state.posY)
        case None => translateToString(state.posX, state.posY)
      }

      <.div(
        ^.transform := translate,
        Style.moveBack.when(state.posX == 0 && state.posY == 0),
        ^.onDragEnd ==> handleDragEnd,
        ^.onDragStart ==> handleDragStart,
        ^.onDrag ==> drag,
        ^.draggable := true
      )
    }
  }

  val component = ScalaComponent
    .builder[Props]("Swipeable")
    .initialState(State(0,0,0,0,0, None))
    .renderBackend[Backend]
    .build

  def apply(props: Props) = component(props)

}
