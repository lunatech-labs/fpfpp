package fr.lunatech.fpfpp

import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.html_<^._

object AppRouter {

  sealed trait Page

  case object Home extends Page

  val routerConfig = RouterConfigDsl[Page].buildConfig { dsl =>
    import dsl._

    (trimSlashes
      | staticRoute(root, Home) ~> render(<.h1("Welcome!"))
      ).notFound(redirectToPage(Home)(Redirect.Replace))
      .renderWith(layout)
  }

  def layout(c: RouterCtl[Page], r: Resolution[Page]) =
    <.div(
      <.div(^.cls := "container", r.render()))


  val router: Router[Page] = Router(BaseUrl.fromWindowOrigin, routerConfig.logToConsole)

}
