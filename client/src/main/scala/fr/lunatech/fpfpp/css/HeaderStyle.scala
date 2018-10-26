package fr.lunatech.fpfpp.css

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet
import scala.concurrent.duration._

object HeaderStyle extends StyleSheet.Inline {
  import dsl._

  val header = style(
    padding(15.px, 0.px),
    display.flex,
    alignItems.center
  )

  val logo = style(
    height(55.px),
    marginLeft(15.px),
    zIndex(2)
  )

  val flicker = keyframes(
    0.%% -> keyframe(
      textShadow := "none",
      color(c"#111111")
    ),
    3.%% -> keyframe(
      textShadow := "0 0 8px rgba(#fa6701,0.6)",
      color(c"#fa6701")
    ),
    6.%% -> keyframe(
      textShadow := "none",
      color(c"#111111")
    ),
    9.%% -> keyframe(
      textShadow := "0 0 8px rgba(#fa6701,0.6)",
      color(c"#fa6701")
    ),
    12.%% -> keyframe(
      textShadow := "none",
      color(c"#111111")
    ),
    60.%% -> keyframe(
      textShadow := "0 0 8px rgba(#fa6701,0.6), " +
        "0 0 16px rgba(#fa6701,0.4), 0 0 20px rgba(255,0,84,0.2)," +
        "0 0 22px rgba(255,0,84,0.1)",
      color(c"#fa6701")
    ),
    100.%% -> keyframe(
      textShadow := "0 0 8px rgba(#fa6701,0.6)," +
        "0 0 16px rgba(#fa6701,0.4)," +
        "0 0 20px rgba(255,0,84,0.2)," +
        "0 0 22px rgba(255,0,84,0.1)",
      color(c"#fa6701")
    )
  )

  val h1 = style(
    position.absolute,
    left(20.%%),
    fontFamily(
      fontFace("Eater")(_.src("url('assets/font/eater-regular.ttf')"))
    ),
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
    animationPlayState.running
  )
}
