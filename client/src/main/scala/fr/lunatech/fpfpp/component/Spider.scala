package fr.lunatech.fpfpp.component

import fr.lunatech.fpfpp.css.SpiderStyle
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import scalacss.DevDefaults._
import scalacss.ScalaCssReact._

object Spider {

  private def legs(sideStyle: StyleA) = List.fill(4)(<.span(SpiderStyle.leg, sideStyle))

  val component = ScalaComponent
    .builder[StyleA]("Spider")
    .render($ =>
      <.div(
        SpiderStyle.spider,
        $.props,
        <.div(SpiderStyle.eye, SpiderStyle.eyeLeft),
        <.div(SpiderStyle.eye, SpiderStyle.eyeRight),
        TagMod(legs(SpiderStyle.legLeft): _*),
        TagMod(legs(SpiderStyle.legRight): _*)
      )
    )
    .build

  def apply(style: StyleA) = component(style)

}
