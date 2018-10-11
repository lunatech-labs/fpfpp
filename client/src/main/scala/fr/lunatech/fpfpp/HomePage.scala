package fr.lunatech.fpfpp

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object HomePage {

  def apply() = ScalaComponent
    .static("Hello")(<.div("Hello!"))

}
