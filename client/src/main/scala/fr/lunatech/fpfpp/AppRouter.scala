package fr.lunatech.fpfpp

import java.time.Year

import fr.lunatech.fpfpp.component.Footer
import fr.lunatech.fpfpp.css.{AppStyle, HeaderStyle, SpiderStyle}
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
    val page2 = "#page"

    (trimSlashes
      | staticRoute(root, Home) ~> render(HomePage(HomePage.Props(ApiClient)))
      | staticRoute(page2, Page2) ~> render(Page(Page.Props("2018"))))
      .notFound(redirectToPage(Home)(Redirect.Replace))
      .renderWith(layout)
  }

  def legs(sideStyle: StyleA) = List.fill(4)(<.span(SpiderStyle.leg, sideStyle))

  def layout(c: RouterCtl[AppPage], r: Resolution[AppPage]): VdomElement = {
    <.div(
      AppStyle.app,
      <.header(
        HeaderStyle.header,
        <.img(HeaderStyle.logo, ^.src := "/assets/img/logo.png"),
        <.p(HeaderStyle.h1, "Happy Halloween !")
      ),
      <.div(
        SpiderStyle.spider,
        <.div(SpiderStyle.eye, SpiderStyle.eyeLeft),
        <.div(SpiderStyle.eye, SpiderStyle.eyeRight),
        TagMod(legs(SpiderStyle.legLeft): _*),
        TagMod(legs(SpiderStyle.legRight): _*)
      ),
      <.img(SpiderStyle.spiderwebCornerRight,
            ^.src := "/assets/img/spiderweb-corner-right.png"),
      <.div(AppStyle.appContent, r.render()),
      <.img(SpiderStyle.spiderwebCornerLeft,
            ^.src := "/assets/img/spiderweb-corner-right.png"),
      Footer()
    )
  }

  val baseUrl = BaseUrl.fromWindowOrigin
  val router = Router(baseUrl, config)

  AppStyle.addToDocument()
  HeaderStyle.addToDocument()
  SpiderStyle.addToDocument()
}
