package fr.lunatech.fpfpp.component

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.{ EventListener, OnUnmount }
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
      transitionDelay(150.millis),
      transitionDuration(500.millis),
      transitionProperty := "transform"
    )

    val overlay = style(
      position.absolute,
      height(100.%%),
      width(100.%%)
    )

    val fadeIn = style(
      opacity(1).important,
      transitionProperty := "opacity",
      transitionDuration(500.millis)
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
    direction: Option[Direction] = None
  )

  val emptyImg: Image = {
    val img = dom.document.createElement("img").asInstanceOf[Image]
    img.setAttribute(
      "src",
      "data: image / gif;base64, R0lGODlhAQABAIAAAAAAAP ///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7")
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

        Some(mouseCurrPosX)
          .filter(_ != 0)
          .map { _ =>
            state.copy(
              posX = mouseCurrPosX - state.initalMousePosX,
              posY = mouseCurrPosY - state.initalMousePosY
            )
          }
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
      state.copy(0, 0, 0, 0, direction(state, props))
    }

    def onKeyDown(event: KeyboardEvent): Callback =
      event.keyCode match {
        case KeyCode.Left => swipeLeft
        case KeyCode.Right => swipeRight
        case _ => Callback.empty
      }

    val swipeLeft = $.modState(_.copy(direction = Some(Direction.Left)), $.props.map(_.swipeLeft.delay(300.millis)).flatten.void)
    val swipeRight = $.modState(_.copy(direction = Some(Direction.Right)), $.props.map(_.swipeRight.delay(300.millis)).flatten.void)

    val delta = 200

    def handleDragEnd: Callback = {
      val swipe = for {
        state <- $.state
        props <- $.props
      } yield
        direction(state, props) match {
          case Some(Direction.Left) => props.swipeLeft.delay(300.millis)
          case Some(Direction.Right) => props.swipeRight.delay(300.millis)
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
          direction = None
        )
      }
    }

    def render(props: Props, state: State, child: PropsChildren) = {

      val atCenter = state.posX == 0 && state.posY == 0

      def translateToString(x: Double, y: Double) = {
        val angle = (x / 9).min(25).max(-25)
        s"translate(${x}px, ${y}px) rotate(${angle}deg) perspective(800px)"
      }

      val translate = state.direction match {
        case Some(Direction.Left) => translateToString(-dom.window.innerWidth, state.posY)
        case Some(Direction.Right) => translateToString(dom.window.innerWidth, state.posY)
        case None => translateToString(state.posX, state.posY)
      }

      def opacity(n: Int) =
        ^.opacity := ((n * state.posX).max(0) / delta).min(1).toString

      <.div(
        ^.transform := translate,
        Style.swipeable,
        Style.moveBack.when(atCenter || state.direction.nonEmpty),
        ^.onDragEnd --> handleDragEnd,
        ^.onDragStart ==> handleDragStart,
        ^.onDrag ==> drag,
        ^.draggable := true,
        <.div(
          Style.overlay,
          Style.fadeIn.when(state.direction.contains(Direction.Left)),
          props.leftTag,
          opacity(-1)
        ),
        <.div(
          Style.overlay,
          Style.fadeIn.when(state.direction.contains(Direction.Right)),
          props.rightTag,
          opacity(1)
        ),
        child
      )
    }
  }

  val component = ScalaComponent
    .builder[Props]("Swipeable")
    .initialState(State())
    .renderBackendWithChildren[Backend]
    .configure(EventListener[KeyboardEvent].install("keydown", _.backend.onKeyDown, _ => dom.window))
    .build

  def apply(props: Props)(node: VdomNode) = component(props)(node)

}
