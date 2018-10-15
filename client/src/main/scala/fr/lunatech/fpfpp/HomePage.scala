package fr.lunatech.fpfpp

import fr.lunatech.fpfpp.component.{ Button, Card, _ }
import fr.lunatech.fpfpp.model.Profile
import fr.lunatech.fpfpp.utils.ApiClient
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.DevDefaults._
import scalacss.ScalaCssReact._
import scalacss.internal.mutable.StyleSheet

object HomePage {

  def apply(props: Props) = component(props)

  val component =
    ScalaComponent
      .builder[Props]("HomePage")
      .initialState(State(Seq.empty))
      .renderBackend[Backend]
      .componentDidMount(_.backend.start)
      .build

  case class State(
    profiles: Seq[Profile]
  )

  case class Props()

  class Backend($: BackendScope[Props, State]) {

    val stackRef = Ref.toScalaComponent(CardStack.component)

    def pop = $.modState(state =>
      state.copy(profiles = state.profiles.drop(1))
    )

    private def iHaveFear: Callback = Callback {
      println("I have fear dude !!")
    } >> pop

    private def iHaveNotFear: Callback = Callback {
      println("I have not fear dude !!")
    } >> pop

    private def refresh: Callback = Callback {
      println("Refresh !!")
    }

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
        ^.className := "fpfpp",
        Style.content,
        <.div(
          Style.cards,
          stackRef.component(
            CardStack.Props(
              state.profiles.map(profile => profile.id -> Card(Card.Props(profile))),
              <.span(Style.tag, Style.fpTag, "Fait Peur"),
              iHaveFear,
              <.span(Style.tag, Style.fppTag, "Fait Pas Peur"),
              iHaveNotFear
            )
          ),
          <.div(
            Style.buttons,
            Button(Button.Props("nop", "fa-ban", swipeLeft)),
            Button(Button.Props("refresh", "fa-sync", refresh)),
            Button(Button.Props("love", "fa-heart", swipeRight))
          )
        )
      )
  }

  object Style extends StyleSheet.Inline {

    import dsl._

    val content = style()

    val cards = style(
      margin(auto),
      maxWidth(375.px),
      height(667.px),
      maxHeight := "calc(100% - 100px)"
    )

    val buttons = style(
      display.flex,
      justifyContent.spaceAround,
    )

    val tag = style(
      lineHeight(25.px),
      fontSize(25.px),
      fontWeight._600,
      border(4.px, solid),
      borderRadius(4.px),
      padding(4.px)
    )

    val fpTag = style(
      color(c"#ff4848")
    )

    val fppTag = style(
      color(c"#01df8a")
    )
  }

  Style.addToDocument()

}
