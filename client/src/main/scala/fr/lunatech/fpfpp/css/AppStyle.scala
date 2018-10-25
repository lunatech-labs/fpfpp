package fr.lunatech.fpfpp.css

import scalacss.internal.mutable.StyleSheet
import scalacss.DevDefaults._

object AppStyle extends StyleSheet.Inline {
  import dsl._

  val app = style(
    height(100.vh),
    background := "linear-gradient(#202020, #111119)"
  )

  val appContent = style(
    marginTop(30.px)
  )
}
