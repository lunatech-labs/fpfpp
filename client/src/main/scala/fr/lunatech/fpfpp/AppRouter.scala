package fr.lunatech.fpfpp

import fr.lunatech.fpfpp.component.Footer
import fr.lunatech.fpfpp.utils.ApiClient
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.DevDefaults._
import scalacss.ScalaCssReact._
import scalacss.internal.mutable.StyleSheet
import scala.concurrent.duration._

object AppRouter {

  sealed trait AppPage
  case object Home extends AppPage
  case object Page2 extends AppPage

  val config = RouterConfigDsl[AppPage].buildConfig { dsl =>
    import dsl._
    val page2 = "#page2"

    (trimSlashes
      | staticRoute(root, Home) ~> render(
        HomePage(HomePage.Props(new ApiClient {})))
      | staticRoute(page2, Page2) ~> render(Page(Page.Props(2))))
      .notFound(redirectToPage(Home)(Redirect.Replace))
      .renderWith(layout)
  }

  def layout(c: RouterCtl[AppPage], r: Resolution[AppPage]): VdomElement = {
    <.div(
      Style.app,
      <.header(
        Style.header,
        <.img(Style.logo, ^.src := "/assets/img/logo.png"),
        <.h1(Style.h1, "Happy Halloween !")
      ),
      r.render(),
      <.img(Style.spiderCornerRight,
            ^.src := "/assets/img/spiderweb-corner-right.png"),
      <.div(Style.content, r.render()),
      <.img(Style.spiderCornerLeft,
            ^.src := "/assets/img/spiderweb-corner-right.png"),
      Footer()
    )
  }

  val baseUrl = BaseUrl.fromWindowOrigin
  val router = Router(baseUrl, config)

  object Style extends StyleSheet.Inline {

    import dsl._

    val app = style(
      height(100.vh),
      background := "linear-gradient(#202020, #111119)"
    )

    val content = style(
      marginTop(30.px)
    )

    val header = style(
      padding(15.px, 0.px),
      display.flex,
      alignItems.center
    )

    val logo = style(
      height(55.px),
      marginLeft(15.px)
    )

    val flicker = keyframes(
      0.%% -> keyframe(textShadow := "none", color(c"#111111")),
      3.%% -> keyframe(textShadow := "0 0 8px rgba(#fa6701,0.6)",
                       color(c"#fa6701")),
      6.%% -> keyframe(textShadow := "none", color(c"#111111")),
      9.%% -> keyframe(textShadow := "0 0 8px rgba(#fa6701,0.6)",
                       color(c"#fa6701")),
      12.%% -> keyframe(textShadow := "none", color(c"#111111")),
      60.%% -> keyframe(
        textShadow := "0 0 8px rgba(#fa6701,0.6), " +
          "0 0 16px rgba(#fa6701,0.4), 0 0 20px rgba(255,0,84,0.2)," +
          "0 0 22px rgba(255,0,84,0.1)",
        color(c"#fa6701")),
      100.%% -> keyframe(textShadow := "0 0 8px rgba(#fa6701,0.6)," +
                           "0 0 16px rgba(#fa6701,0.4)," +
                           "0 0 20px rgba(255,0,84,0.2)," +
                           "0 0 22px rgba(255,0,84,0.1)",
                         color(c"#fa6701"))
    )
    val h1 = style(
      position.absolute,
      left(20.%%),
      fontFamily(
        fontFace("Eater")(_.src("url('assets/font/eater-regular.ttf')"))),
      fontSize(5.5.vw),
      color(c"#11115A"),
      flex := "1",
      margin.auto,
      textAlign.center,
      animationName(flicker),
      animationDuration(4.second),
      animationDelay(0.second),
      animationTimingFunction.ease,
      animationIterationCount.infinite,
      animationDirection.normal,
      animationFillMode := "none",
      animationPlayState.running
    )

    val spiderCornerRight = style(
      position.absolute,
      height(300.px),
      width.auto,
      right(-10.px),
      top(-10.px),
      zIndex(1),
      opacity(1.2)
    )

    val spiderCornerLeft = style(
      position.absolute,
      left(-10.px),
      bottom(-10.px),
      height(300.px),
      zIndex(1),
      opacity(1.2),
      transform := "rotate(-180deg)"
    )
  }

  Style.addToDocument()
}
