package fr.lunatech.fpfpp.component

import fr.lunatech.fpfpp.{HomePage, Image}
import fr.lunatech.fpfpp.component.FPFPPTestDsl._
import fr.lunatech.fpfpp.component.FPFPPTestState._
import fr.lunatech.fpfpp.utils.ApiClient
import japgolly.scalajs.react.Callback
import japgolly.scalajs.react.test._
import utest._

object FPFPPTest extends TestSuite {

  /**
    * Here be all the invariants for the TodoComponent.
    *
    * They be validated before and after each action executes, arr.
    */
  val invariants: dsl.Invariants = {
    var invars = dsl.emptyInvariant

    // Invariant #1: Tree buttons only
    invars &= dsl.focus("Display only three buttons (refresh, swipe left and swipe right)")
      .compare(_.obs.buttonsCount, os => 3)
      .assert.equal

    // Invariant #2: Equation have to be valid
    invars &= dsl.test("Equation have to be valid (nbInitialImages = nbFear + nbNotFear + nbRemainingImages) ")(os => {
      os.obs.nbInitialImages == os.obs.nbFear + os.obs.nbNotFear + os.obs.nbRemainingImages
    })

    // State: Check that the local state is sync with what we retrieve by the observer
    val expectedStateIsCorrect =
      dsl.focus("Check the number of fear")
        .obsAndState(obs => obs.nbFear, state => state.nbSwipeLeft).assert.equal &
      dsl.focus("Check the number of not fear")
        .obsAndState(_.nbNotFear, _.nbSwipeRight).assert.equal

    invars & expectedStateIsCorrect
  }

  def apiClient: ApiClient = new ApiClient {
    override def getImages(success: Seq[Image] => Callback,
                           error: Exception => Callback): Callback =
      success(
        Seq(
          Image("5", "assets/img/images/5.png"),
          Image("28", "assets/img/images/28.png"),
          Image("26", "assets/img/images/26.png"),
          Image("42", "assets/img/images/42.png"),
        ))
  }

  override def tests = utest.Tests {
    runTest(
      Plan.action(
        swipeRight //+> checkEquation
          >> swipeLeft //+> checkEquation
          >> refreshCards //+> checkEquation
      )
    ).assert()
  }

  def runTest(plan: dsl.Plan): Report[String] = {
    ReactTestUtils.withRenderedIntoDocument(
    HomePage.component(HomePage.Props(apiClient))) { c =>
      def observe() = new FPFPPTestObs(c.htmlDomZipper)

      val test = plan
      .addInvariants(invariants)
      .withInitialState(State(0, 0, 0))
      .test(Observer watch observe())

      test.runU
    }
  }

}
