package fr.lunatech.fpfpp.component

import external.KonamiCode
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react._
import org.scalajs.dom.raw.HTMLAudioElement
import scalacss.DevDefaults._
import scalacss.ScalaCssReact._
import scalacss.internal.mutable.StyleSheet

import scala.concurrent.duration._

object Footer {

  val animationTime: FiniteDuration = 2000.millis

  object Style extends StyleSheet.Inline {

    import dsl._

    val raptorGOAnimation = keyframes(
      25.%% -> keyframe(transform := "translateY(0)"),
      35.%% -> keyframe(
        right.`0`,
        transform := "translateY(25%)"
      ),
      75.%% -> keyframe(opacity(1)),
      100.%% -> keyframe(
        opacity(0),
        right(100.%%),
        transform := "translateY(25%)"
      )
    )

    val raptor = style(
      display.none,
      bottom.`0`,
      position.fixed,
      transform := "translateY(0)",
      right.`0`,
      zIndex(9999999)
    )

    val raptorGo = style(
      display.block,
      animationName(raptorGOAnimation),
      animationDuration(2500.millis)
    )
  }

  Style.addToDocument()

  val component = ScalaComponent
    .builder[Unit]("Raptor")
    .initialState(State(playing = false))
    .renderBackend[Backend]
    .componentDidMount(_.backend.start)
    .build

  def apply() = component()

  case class State(playing: Boolean)

  class Backend($: BackendScope[Unit, State]) {

    private val audioRef = Ref[HTMLAudioElement]

    val audioPath: String = "assets/img/raptor.mp3"
    val imagePath: String = "assets/img/raptor.png"

    def start = Callback {
      val go = {
        audioRef.get.map(_.play).delay(animationTime / 3) >>
          $.modState(_.copy(playing = true), $.modState(_.copy(playing = false)).delay(animationTime).void)
      }.toJsFn1

      KonamiCode(go)
    }

    def render(state: State) = {
      <.div(
        <.audio(
          <.source(
            ^.src := audioPath
          )
        ).withRef(audioRef),
        <.img(
          Style.raptor,
          Style.raptorGo.when(state.playing),
          ^.src := imagePath
        )
      )
    }
  }
}
