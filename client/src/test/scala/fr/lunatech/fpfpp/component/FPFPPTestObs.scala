package fr.lunatech.fpfpp.component

import fr.lunatech.fpfpp.component.FPFPPTestState._
import org.scalajs.dom.html

final class FPFPPTestObs($: HtmlDomZipper) {

  private val buttons = $.collect0n("button")

  val buttonsCount = buttons.size

  val swipeLeftButton = $("#swipeLeft").as[html.Button].dom

  val swipeRightButton = $("#swipeRight").as[html.Button].dom

  val refreshButton = $("#refresh").as[html.Button].dom

  val nbNotFear = $("#FaitPasPeur").innerText.toInt

  val nbFear = $("#FaitPeur").innerText.toInt

  val nbInitialImages = $("#totalImages").innerText.toInt

  val nbRemainingImages = $("#remainingImages").innerText.toInt
}
