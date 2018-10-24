package fr.lunatech.fpfpp

import fr.lunatech.fpfpp.component.Footer
import fr.lunatech.fpfpp.utils.ApiClient
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.DevDefaults._
import scalacss.ScalaCssReact._
import scalacss.internal.mutable.StyleSheet

object AppRouter {

  sealed trait AppPage
  case object Home extends AppPage
  case object Page2 extends AppPage

  val config = RouterConfigDsl[AppPage].buildConfig { dsl =>
    import dsl._
    val page2 = "#page2"

    (trimSlashes
      | staticRoute(root, Home) ~> render(HomePage(HomePage.Props(new ApiClient {})))
      | staticRoute(page2, Page2) ~> render(Page(Page.Props(2))))
      .notFound(redirectToPage(Home)(Redirect.Replace))
      .renderWith(layout)
  }

  def layout(c: RouterCtl[AppPage], r: Resolution[AppPage]): VdomElement = {
    <.div(
      Style.app,
      <.header(
        Style.header,
        <.img(Style.logo, ^.src := "/assets/img/logo.png")
      ),
      r.render(),
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
  }

  Style.addToDocument()
}
