package fr.lunatech.fpfpp.css

import scalacss.internal.mutable.StyleSheet
import scalacss.DevDefaults._
import scalacss.ScalaCssReact._
import scala.concurrent.duration._

object MainStyle extends StyleSheet.Inline {

  import dsl._

  val app = style(
    height(100.vh),
    background := "linear-gradient(#202020, #111119)"
  )

  val appContent = style(
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

  val spiderwebCornerRight = style(
    position.absolute,
    height(300.px),
    width.auto,
    right(-10.px),
    top(-10.px),
    zIndex(1),
    opacity(1.2)
  )

  val spiderwebCornerLeft = style(
    position.absolute,
    left(-10.px),
    bottom(-10.px),
    height(300.px),
    zIndex(1),
    opacity(1.2),
    transform := "rotate(-180deg)"
  )

  val spiderMove = keyframes(
    0.%% -> keyframe(marginTop(330.px)),
    50.%% -> keyframe(marginTop(430.px)),
    100.%% -> keyframe(marginTop(330.px))
  )

  val spider = style(
    position.absolute,
    height(40.px),
    width(50.px),
    borderRadius(50.%%),
    marginTop(210.px),
    background := "#110D04",
    right(5.%%),
    animationName(spiderMove),
    animationDuration(4.second),
    animationIterationCount.infinite,
    &.before(
      position.absolute,
      content := "''",
      width(1.px),
      background := "#AAAAAA",
      left(50.%%),
      top(-600.px),
      height(600.px)
    )
  )

  val eye = style(
    position.absolute,
    top(16.px),
    height(14.px),
    width(12.px),
    background := "#FFFFFF",
    borderRadius(50.%%),
    &.after(
      position.absolute,
      content := "''",
      top(6.px),
      height(5.px),
      width(5.px),
      borderRadius(50.%%),
      background := "black"
    )
  )
  val eyeLeft = style(
    left(14.px),
    &.after(
      right(3.px)
    )
  )

  val eyeRight = style(
    right(14.px),
    &.after(
      left(3.px)
    )
  )

  val legsWriggleLeft = keyframes(
    0.%% -> keyframe(transform := transformLeg(36, -20)),
    25.%% -> keyframe(transform := transformLeg(15, -20)),
    50.%% -> keyframe(transform := transformLeg(40, -20)),
    75.%% -> keyframe(transform := transformLeg(15, -20)),
    100.%% -> keyframe(transform := transformLeg(36, -20)),
  )

  val leg = style(
    top(6.px),
    height(12.px),
    width(14.px),
    borderTop := "2px solid #110D04",
    borderRight := "1px solid transparent",
    borderBottom := "1px solid transparent",
    transform := transformLeg(36, -20),
    borderLeft := "2px solid #110D04",
    borderRadius := "60% 0 0 0",
    animationName(legsWriggleLeft),
    animationDuration(1.second),
    animationDelay(0.2.second),
    animationTimingFunction.ease,
    animationIterationCount.infinite,
    animationDirection.normal,
    animationFillMode := "none",
    animationPlayState.running,
    zIndex(1),
    position.absolute,
    &.nthLastOfType(2)(
      top(14.px),
      right(-11.px),
      animationDelay(0.8.second),
    ),
    &.nthLastOfType(3)(
      top(20.px),
      right(-13.px),
      animationDelay(0.2.second),
    ),
    &.nthLastOfType(4)(
      top(25.px),
      right(-12.px),
      animationDelay(0.4.second),
    ),
    &.nthLastOfType(6)(
      top(14.px),
      left(-11.px),
      animationDelay(0.4.second),
    ),
    &.nthLastOfType(7)(
      top(22.px),
      left(-12.px),
      animationDelay(0.8.second),
    ),
    &.nthLastOfType(8)(
      top(27.px),
      left(-10.px),
      animationDelay(0.3.second),
    )
  )

  def transformLeg(rotate: Double, skewX: Double) =
    s"rotate(${rotate}deg) skewX(${skewX}deg)"

  val legLeft = style(
    left(-15.px),
    transformOrigin := "top, right",
  )

  val legRight = style(
    right(-15.px),
    transformOrigin := "top, left",
  )
}
