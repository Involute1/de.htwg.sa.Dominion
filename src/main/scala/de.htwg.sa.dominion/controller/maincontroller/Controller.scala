package de.htwg.sa.dominion.controller.maincontroller

import com.google.inject.{Guice, Injector}
import de.htwg.sa.dominion.DominionModule
import de.htwg.sa.dominion.controller.ControllerInterface
import de.htwg.sa.dominion.model.RoundmanagerInterface
import de.htwg.sa.dominion.model.cardcomponent.CardName
import de.htwg.sa.dominion.model.cardcomponent.CardName.CardName
import de.htwg.sa.dominion.model.roundmanagerComponent.Roundmanager
import de.htwg.sa.dominion.util.UndoManager
import javax.inject.Inject

class Controller @Inject()(var roundmanager: RoundmanagerInterface) extends ControllerInterface {

  var controllerMessage: String = ""
  var controllerState: ControllerState = PreSetupState(this)
  val undoManager = new UndoManager
  val injector: Injector = Guice.createInjector(new DominionModule)

  override def eval(input: String): Unit = {
    undoManager.doStep(new SetCommand(this))
    controllerState.evaluate(input)
    setControllerMessage(controllerState.getCurrentControllerMessage)
    notifyObservers
  }

  override def undo(): Unit = {
    undoManager.undoStep()
    notifyObservers
  }

  override def redo(): Unit = {
    undoManager.redoStep()
    notifyObservers
  }

  override def save(): Unit = {
    // TODO
    ???
  }

  override def load(): Unit = {
    // TODO
    ???
  }

  override def getControllerMessage: String = {
    controllerMessage
  }

  override def setControllerMessage(message: String): Unit = {
    controllerMessage = message
  }

  override def getHelpPage(): Unit = {
    // TODO print helpscreen
    notifyObservers
  }

  override def getControllerStateAsString: String = {
    controllerState match {
      case _: PreSetupState => "PreStetupState"
      case _: PlayerSetupState => "PlayerSetupState"
      case _: ActionPhaseState => "ActionState"
      case _: BuyPhaseState => "BuyState"
      case _: GameOverState => "GameOverState"
    }
  }
}

object Controller {
  def toInt(s: String): Option[Int] = {
    try {
      Some(s.toInt)
    } catch {
      case _: Exception => None
    }
  }
}

trait ControllerState {
  def evaluate(input: String): Unit

  def getCurrentControllerMessage: String

  def nextState: ControllerState
}

case class PreSetupState(controller: Controller) extends ControllerState {
  override def evaluate(input: String): Unit = {
    val number = Controller.toInt(input)
    if (number.isEmpty) return
    if (number.get < 3 || number.get > 5) return

    val initCardNames: List[CardName] = List(CardName.COPPER, CardName.SILVER, CardName.GOLD, CardName.ESTATE,
      CardName.DUCHY, CardName.PROVINCE, CardName.VILLAGE, CardName.FESTIVAL, CardName.CELLAR, CardName.MINE,
      CardName.SMITHY, CardName.REMODEL, CardName.MERCHANT, CardName.WORKSHOP, CardName.GARDENS, CardName.MARKET)

    initCardNames.foreach(x => controller.roundmanager = controller.roundmanager.createPlayingDecks(x))
    controller.roundmanager = controller.roundmanager.updateNumberOfPlayer(number.get)

    controller.controllerState = nextState
  }

  override def getCurrentControllerMessage: String = "Please enter the number of Players, must be between 3 & 5:"

  override def nextState: ControllerState = PlayerSetupState(controller)
}

  case class PlayerSetupState(controller: Controller) extends ControllerState {
    override def evaluate(input: String): Unit = {
      val name = input
      if (name.isEmpty) return
      controller.roundmanager = controller.roundmanager.updateListNames(input)
      if (!controller.roundmanager.namesEqualPlayer()) return
      for (i <- 0 until controller.roundmanager.getNumberOfPlayers) {
        controller.roundmanager = controller.roundmanager.initializePlayersList(i)
        for (f <- 0 until 5) {
          controller.roundmanager = controller.roundmanager.drawCard(i)
        }
      }

      controller.controllerState = nextState
    }

  override def getCurrentControllerMessage: String = controller.roundmanager.constructControllerAskNameString

  override def nextState: ControllerState = ActionPhaseState(controller)
}

case class ActionPhaseState(controller: Controller) extends ControllerState {
  override def evaluate(input: String): Unit = {
    if (input.isBlank) return
    controller.roundmanager = controller.roundmanager.actionPhase(input)
    if (controller.roundmanager.checkIfActionPhaseDone) controller.controllerState = nextState
  }

  override def getCurrentControllerMessage: String = controller.roundmanager.constructRoundermanagerStateString

  override def nextState: ControllerState = BuyPhaseState(controller)
}

case class BuyPhaseState(controller: Controller) extends ControllerState {
  override def evaluate(input: String): Unit = {
    if (input.isBlank) return
    controller.roundmanager = controller.roundmanager.buyPhase(input)
    if (controller.roundmanager.checkForNextPlayer) controller.controllerState = ActionPhaseState(controller)
    if (controller.roundmanager.checkForGameEnd()) controller.controllerState = nextState

  }

  override def getCurrentControllerMessage: String = controller.roundmanager.constructRoundermanagerStateString

  override def nextState: ControllerState = GameOverState(controller)
}

case class GameOverState(controller: Controller) extends ControllerState {
  override def evaluate(input: String): Unit = ()

  override def getCurrentControllerMessage: String = ???

  override def nextState: ControllerState = this
}

