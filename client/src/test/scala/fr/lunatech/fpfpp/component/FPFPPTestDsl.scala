package fr.lunatech.fpfpp.component

import fr.lunatech.fpfpp.component.FPFPPTestState._
import japgolly.scalajs.react.test._

object FPFPPTestDsl {

  val dsl = Dsl[Unit, FPFPPTestObs, State]

  val checkFearNotFearCounts = dsl.focus("Check the number of fear")
    .obsAndState(obs => obs.nbFear, state => state.nbSwipeLeft).assert.equal &
    dsl.focus("Check the number of not fear")
      .obsAndState(_.nbNotFear, _.nbSwipeRight).assert.equal

  val swipeLeft: dsl.Actions = dsl
    .action("Click on the 'swipe left' button")(Simulate click _.obs.swipeLeftButton)
    .updateState(state => state.copy(nbSwipeLeft = state.nbSwipeLeft + 1))

  val swipeRight: dsl.Actions = dsl
    .action("Click on the 'swipe right' button")(Simulate click _.obs.swipeRightButton)
    .updateState(state => state.copy(nbSwipeRight = state.nbSwipeRight + 1))

  val refreshCards: dsl.Actions = dsl
    .action("Click the 'Refresh' button")(Simulate click _.obs.refreshButton)
    .updateState(state => state.copy(
      nbRefresh = state.nbRefresh + 1,
      nbSwipeLeft = 0,
      nbSwipeRight = 0
    ))

  val checkEquation = dsl.test("Equation have to be valid (nbInitialImages = nbFear + nbNotFear + nbRemainingImages) ")(os => {
    os.obs.nbInitialImages == os.obs.nbFear + os.obs.nbNotFear + os.obs.nbRemainingImages
  })

  case class State(nbRefresh: Int, nbSwipeLeft: Int, nbSwipeRight: Int)

}
