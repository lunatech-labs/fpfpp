package fr.lunatech.fpfpp

import fr.lunatech.fpfpp.component.Footer
import fr.lunatech.fpfpp.css.MainStyle
import fr.lunatech.fpfpp.utils.ApiClient
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.DevDefaults._
import scalacss.ScalaCssReact._

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

  def legs(sideStyle: StyleA) = List.fill(4)(<.span(MainStyle.leg, sideStyle))

  def layout(c: RouterCtl[AppPage], r: Resolution[AppPage]): VdomElement = {
    <.div(
      MainStyle.app,
      <.header(
        MainStyle.header,
        <.img(MainStyle.logo, ^.src := "/assets/img/logo.png"),
        <.p(MainStyle.h1, "Happy Halloween !")
      ),
      <.div(
        MainStyle.spider,
        <.div(MainStyle.eye, MainStyle.eyeLeft),
        <.div(MainStyle.eye, MainStyle.eyeRight),
        TagMod(legs(MainStyle.legLeft): _*),
        TagMod(legs(MainStyle.legRight): _*)
      ),
      r.render(),
      <.img(MainStyle.spiderwebCornerRight,
            ^.src := "/assets/img/spiderweb-corner-right.png"),
      <.div(MainStyle.appContent, r.render()),
      <.img(MainStyle.spiderwebCornerLeft,
            ^.src := "/assets/img/spiderweb-corner-right.png"),
      Footer()
    )
  }

  val baseUrl = BaseUrl.fromWindowOrigin
  val router = Router(baseUrl, config)

  MainStyle.addToDocument()
}
