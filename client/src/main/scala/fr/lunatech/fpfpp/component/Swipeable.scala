package fr.lunatech.fpfpp.component

import japgolly.scalajs.react._
import japgolly.scalajs.react.component.builder.Lifecycle.ComponentWillReceivePropsFn
import japgolly.scalajs.react.extra.{EventListener, OnUnmount}
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom
import org.scalajs.dom.KeyboardEvent
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.html.Image
import scalacss.DevDefaults._
import scalacss.ScalaCssReact._
import scalacss.internal.mutable.StyleSheet

import scala.concurrent.Future
import scala.concurrent.duration._

object Swipeable {

  object Style extends StyleSheet.Inline {

    import dsl._

    val swipeable = style(
      height(100.%%),
      width(100.%%)
    )

    val moveBack = style(
      transition := "transform 200ms"
    )

    val tag = style(
      position.absolute,
      top(10.%%)
    )

    val leftTag = style(
      left(10.%%),
      transform := "rotate(-20deg)"
    )

    val rightTag = style(
      right(10.%%),
      transform := "rotate(20deg)"
    )
  }

  Style.addToDocument()

  sealed trait Direction

  object Direction {
    case object Left extends Direction
    case object Right extends Direction
  }

  case class Props(
    leftTag: VdomNode,
    swipeLeft: Callback,
    rightTag: VdomNode,
    swipeRight: Callback
  )

  case class State(
    posX: Double = 0,
    posY: Double = 0,
    initalMousePosX: Double = 0,
    initalMousePosY: Double = 0,
    delta: Double = 0,
    direction: Option[Direction] = None,
    dragged: Boolean = false
  )

  val emptyImg: Image = {
    val img = dom.document.createElement("img").asInstanceOf[Image]
    img.setAttribute("src", "data: image / gif;base64, R0lGODlhAQABAIAAAAAAAP ///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7")
    img
  }

  class Backend($: BackendScope[Props, State]) extends OnUnmount {

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

    def direction(state: State, props: Props): Option[Direction] = {
      if (state.posX < -delta) {
        Some(Direction.Left)
      } else if (state.posX > delta) {
        Some(Direction.Right)
      } else {
        None
      }
    }

    val dragEndState = $.modState { (state, props) =>
      state.copy(
        posX = 0,
        posY = 0,
        initalMousePosX = 0,
        initalMousePosY = 0,
        direction = direction(state, props)
      )
    }

    def onKeyDown(event: KeyboardEvent): Callback =
      event.keyCode match {
        case KeyCode.Left => swipeLeft
        case KeyCode.Right => swipeRight
        case _ => Callback.empty
      }

    val swipeLeft = $.modState(_.copy(direction = Some(Direction.Left)), $.props.map(_.swipeLeft.delay(200.millis)).flatten.void)
    val swipeRight = $.modState(_.copy(direction = Some(Direction.Right)), $.props.map(_.swipeRight.delay(200.millis)).flatten.void)

    val delta = 200

    def handleDragEnd: Callback = {
      val swipe = for {
        state <- $.state
        props <- $.props
      } yield direction(state, props) match {
        case Some(Direction.Left) => props.swipeLeft.delay(200.millis)
        case Some(Direction.Right) => props.swipeRight.delay(200.millis)
        case None => CallbackTo(Future.successful(()))
      }
      swipe.flatten >> dragEndState
    }

    def handleDragStart(event: ReactDragEvent): Callback = {
      persistEvent(event)
      $.modState { state =>
        state.copy(
          initalMousePosX = event.clientX,
          initalMousePosY = event.clientY,
          direction = None,
          dragged = true
        )
      }
    }

    def render(props: Props, state: State, c: PropsChildren) = {

      def translateToString(x: Double, y: Double) = {
        val angle = (x / 9).min(25).max(-25)
        s"translate(${x}px, ${y}px) rotate(${angle}deg) perspective(800px)"
      }

      val translate = state.direction match {
        case Some(Direction.Left) => translateToString(-dom.window.innerWidth, state.posY)
        case Some(Direction.Right) => translateToString(dom.window.innerWidth, state.posY)
        case None => translateToString(state.posX, state.posY)
      }

      <.div(
        ^.transform := translate,
        Style.swipeable,
        Style.moveBack.when((state.posX == 0 && state.posY == 0 && state.dragged) || state.direction.nonEmpty),
        ^.onDragEnd --> handleDragEnd,
        ^.onDragStart ==> handleDragStart,
        ^.onDrag ==> drag,
        ^.draggable := true,
        <.div(Style.tag, Style.leftTag, props.leftTag, ^.opacity := ((-state.posX).max(0) / delta).min(1).toString),
        <.div(Style.tag, Style.rightTag, props.rightTag , ^.opacity := (state.posX.max(0) / delta).min(1).toString),
        c
      )
    }
  }

  def resetState: ComponentWillReceivePropsFn[Props, State, Backend] = _.setState(State())

  val component = ScalaComponent
    .builder[Props]("Swipeable")
    .initialState(State())
    .renderBackendWithChildren[Backend]
    .componentWillReceiveProps(resetState)
    .configure(EventListener[KeyboardEvent].install("keydown", _.backend.onKeyDown, _ => dom.window))
    .build

  def apply(props: Props)(node: VdomNode) = component(props)(node)

}
