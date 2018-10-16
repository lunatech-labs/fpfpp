package fr.lunatech.fpfpp

import fr.lunatech.fpfpp.component.Swipeable.Direction
import fr.lunatech.fpfpp.component.{ Button, Card, _ }
import fr.lunatech.fpfpp.model.Profile
import fr.lunatech.fpfpp.utils.ApiClient
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.DevDefaults._
import scalacss.ScalaCssReact._
import scalacss.internal.mutable.StyleSheet

import scala.concurrent.duration._

object HomePage {

  def apply(props: Props) = component(props)

  val component =
    ScalaComponent
      .builder[Props]("HomePage")
      .initialState(State(Seq.empty, None))
      .renderBackend[Backend]
      .componentDidMount(_.backend.start)
      .build

  case class State(
    profiles: Seq[Profile],
    swipe: Option[Direction]
  ) {
    def swipeLeft: State = copy(swipe = Some(Direction.Left))

    def swipeRight: State = copy(swipe = Some(Direction.Right))

    def swipeReset: State = copy(swipe = None)
  }

  case class Props()

  class Backend($: BackendScope[Props, State]) {

    val stackRef = Ref.toScalaComponent(CardStack.component)

    def pop = $.modState(state =>
      state.copy(profiles = state.profiles.drop(1))
    )

    private def iHaveFear: Callback = Callback {
      println("I have fear dude !!")
    } >> pop >> $.modState(_.swipeLeft, $.modState(_.swipeReset).delay(300.millis).void)

    private def iHaveNotFear: Callback = Callback {
      println("I have not fear dude !!")
    } >> pop >> $.modState(_.swipeRight, $.modState(_.swipeReset).delay(300.millis).void)

    private def refresh: Callback = start

    val swipeLeft = stackRef.get.flatMap(_.backend.swipeLeft).void
    val swipeRight = stackRef.get.flatMap(_.backend.swipeRight).void

    def modStateError: Exception => Callback =
      e => $.modState(_.copy(profiles = Seq.empty))

    def start: Callback =
      ApiClient.getImages(images =>
        $.modState(_.copy(profiles = images.zipWithIndex.map {
          case (img, i) => Profile(i.toString, img)
        })), modStateError
      )


    def render(props: Props, state: State) =
      <.div(
        Style.app,
        <.header(Style.header, <.img(Style.logo, ^.src := "/assets/img/logo.png")),
        <.div(
          Style.content,

          state.swipe.map(dir => <.div(Style.swiped, Style.swipedLeft.when(dir == Direction.Left), Style.swipedRight.when(dir == Direction.Right))),
          <.div(
            Style.cards,
            stackRef.component(
              CardStack.Props(
                state.profiles.map(profile => profile.id -> Card(Card.Props(profile))),
                CardOverlay(CardOverlay.Props("fa-flask-poison", "#FaitPeur", Style.whiteColor, Style.fp)),
                iHaveFear,
                CardOverlay(CardOverlay.Props("fa-flask-potion", "#FaitPasPeur", Style.whiteColor, Style.fpp)),
                iHaveNotFear
              )
            ),
            <.div(Style.button, Style.stickLeftCenter, Button(Button.Props("fa-flask-poison", swipeLeft))),
            <.div(Style.button, Style.stickBottomCenter, Button(Button.Props("fa-hat-witch", refresh))),
            <.div(Style.button, Style.stickRightCenter, Button(Button.Props("fa-flask-potion", swipeRight))),
          )
        )
      )
  }

  object Style extends StyleSheet.Inline {

    import dsl._

    val app = style(
      height(100.vh),
      background := "linear-gradient(#202020, #111119)"
    )

    val header = style(
      backgroundColor(c"#161719"),
      padding(15.px, 0.px),
      borderBottom(1.px, solid, c"#131313")
    )

    val logo = style(
      height(55.px),
      display.block,
      margin.auto
    )

    val content = style(
      position.relative,
      height(100.%%)
    )

    val cards = style(
      position.relative,
      margin(auto),
      maxWidth(375.px),
      height(667.px),
      maxHeight := "calc(100% - 110px)",
      paddingTop(25.px)
    )

    val button = style(
      position.absolute,
      zIndex(100)
    )

    val stickLeftCenter = style(
      left.`0`,
      top(50.%%),
      transform := "translate(-50%, -50%)"
    )

    val stickBottomCenter = style(
      left(50.%%),
      bottom.`0`,
      transform := "translate(-50%, -50%)"

    )

    val stickRightCenter = style(
      right.`0`,
      top(50.%%),
      transform := "translate(50%, -50%)"
    )

    val whiteColor = white
    val fp = rgba(255, 0, 0, 0.5)
    val fpp = rgba(0, 255, 0, 0.5)

    val flash = keyframes(
      0.%% -> keyframe(opacity(0)),
      50.%% -> keyframe(opacity(1)),
      100.%% -> keyframe(opacity(0)),
    )

    val swiped = style(
      position.absolute,
      animationName(flash),
      animationDuration(150.millis),
      width(25.%%),
      height(100.%%),
      opacity(0)
    )

    val swipedLeft = style(
      left.`0`,
      background := "linear-gradient(to right, rgba(255,0,0,0.5) 0%,rgba(255, 0, 0,0) 100%)"
    )

    val swipedRight = style(
      right.`0`,
      background := "linear-gradient(to left, rgba(0,255,0,0.5) 0%,rgba(0, 255, 0,0) 100%)"
    )
  }

  Style.addToDocument()

}
