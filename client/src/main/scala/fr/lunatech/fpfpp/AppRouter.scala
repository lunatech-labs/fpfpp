package fr.lunatech.fpfpp

import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.html_<^._

object AppRouter {

  sealed trait AppPage

  case object Home extends AppPage
  case object Page2 extends AppPage

  val config = RouterConfigDsl[AppPage].buildConfig { dsl =>
    import dsl._
    val page2 = "#page2"

    (trimSlashes
      | staticRoute(root, Home) ~> render(HomePage(HomePage.Props()))
      | staticRoute(page2, Page2) ~> render(Page(Page.Props(2))))
      .notFound(redirectToPage(Home)(Redirect.Replace))
  }

  val baseUrl = BaseUrl.fromWindowOrigin
  val router = Router(baseUrl, config)
}
