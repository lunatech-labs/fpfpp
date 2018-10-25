package fr.lunatech.fpfpp

import scala.concurrent.duration._
import fr.lunatech.fpfpp.component.Swipeable.Direction
import fr.lunatech.fpfpp.component._
import fr.lunatech.fpfpp.utils.ApiClient
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.DevDefaults._
import scalacss.ScalaCssReact._
import scalacss.internal.mutable.StyleSheet

object HomePage {

  def component =
    ScalaComponent
      .builder[Props]("HomePage")
      .initialState(State(Seq.empty, 0, None, Seq.empty))
      .renderBackend[Backend]
      .componentDidMount(_.backend.start)
      .build

  def apply(props: Props) = component(props)

  case class State(
      images: Seq[Image],
      nbInitialImages: Int,
      swipe: Option[Direction],
      history: Seq[Direction]
  ) {
    def swipeLeft: State =
      copy(swipe = Some(Direction.Left), history = Direction.Left +: history)

    def swipeRight: State =
      copy(swipe = Some(Direction.Right), history = Direction.Right +: history)

    def swipeReset: State = copy(swipe = None)
  }

  case class Props(apiClient: ApiClient)

  class Backend($ : BackendScope[Props, State]) {

    val stackRef = Ref.toScalaComponent(CardStack.component)

    val swipeLeft = stackRef.get.flatMap(_.backend.swipeLeft).void

    val swipeRight = stackRef.get.flatMap(_.backend.swipeRight).void

    private val pop: Callback =
      $.modState(state => state.copy(images = state.images.drop(1)))

    private val resetFlash: Callback =
      $.modState(_.swipeReset).delay(300.millis).void

    private val iHaveFear: Callback =
      Callback { println("I have fear dude !!") } >> pop >> $.modState(
        _.swipeLeft,
        resetFlash)

    private val iHaveNotFear: Callback =
      Callback { println("I have not fear dude !!") } >> pop >> $.modState(
        _.swipeRight,
        resetFlash)

    private val refresh: Callback =
      Callback { println("Again dude !!") } >> start

    def render(props: Props, state: State) = {
      <.div(
        Style.content,
        state.swipe.map(Flashing(_)),
        BluredBackgound("assets/img/images/29.png"),
        <.div(
          Style.cards,
          stackRef.component(
            CardStack.Props(
              state.images.map(image => image.id -> Card(image)),
              CardOverlay.apply("fa-flask-poison",
                                "#FaitPeur",
                                Style.whiteColor,
                                Style.fp),
              iHaveFear,
              CardOverlay.apply("fa-flask-potion",
                                "#FaitPasPeur",
                                Style.whiteColor,
                                Style.fpp),
              iHaveNotFear
            )
          ),
//          Controls(Controls.Props(swipeLeft, refresh, swipeRight)),
          Controls(Controls.Props(iHaveFear, refresh, iHaveNotFear)),
          <.div(
            ^.visibility := "hidden",
            <.span(<.span("# FaitPeur"),
                   <.span(^.id := "FaitPeur",
                          state.history.count(_ == Direction.Left))),
            <.span(<.span("# FaitPasPeur"),
                   <.span(^.id := "FaitPasPeur",
                          state.history.count(_ == Direction.Right))),
            <.span(<.span("# remaining images"),
                   <.span(^.id := "remainingImages", state.images.size)),
            <.span(<.span("# total images"),
                   <.span(^.id := "totalImages", state.nbInitialImages))
          )
        )
      )
    }

    def start: Callback = $.props.flatMap { props =>
      props.apiClient.getImages(images => {
        $.modState(
          _.copy(
            images = images,
            nbInitialImages = images.size,
            swipe = None,
            history = Seq.empty
          ))
      }, modStateError)
    }

    def modStateError: Exception => Callback =
      e => $.modState(_.copy(images = Seq.empty))
  }

  object Style extends StyleSheet.Inline {

    import dsl._

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

    val whiteColor = white
    val fp = rgba(255, 0, 0, 0.5)
    val fpp = rgba(0, 255, 0, 0.5)
  }

  Style.addToDocument()

}
