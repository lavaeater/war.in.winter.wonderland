package lavagames.towers.data

import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.controllers.ControllerAdapter
import com.badlogic.gdx.controllers.Controllers

class PlayerAndControllerManager(
    private val maxNumberOfPlayers: Int = 5,
    private val keyboardAllowed: Boolean = true) : ControllerAdapter() {

  val controllers = mutableSetOf<Control>()
  val players = mutableSetOf<Player>()

  val needsConnecting: Boolean get() { return controllers.any { !it.connected } }

  init {
    if(keyboardAllowed) {
      controllers.add(Control.KeyboardControl())
    }
    Controllers.addListener(this)
    for(c in Controllers.getControllers()) {
      controllers.add(Control.GamepadControl(c))
    }

    for(playerIndex in 1..maxNumberOfPlayers) {
      players.add(Player("Player $playerIndex"))
    }
  }

  override fun buttonDown(controller: Controller, buttonIndex: Int): Boolean {
    /*
    It'd be nice with a generic way to handle button presses.

    Like, we'd like to map certain buttons to certain actions. In a general way.
     */



    return true
  }

  override fun connected(controller: Controller) {
    val existingController = controllers.firstOrNull {
      it is Control.GamepadControl && it.controller == controller } as Control.GamepadControl?
    if(existingController == null) {
      controllers.add(Control.GamepadControl(controller))
    }
  }

  override fun disconnected(controller: Controller) {
    super.disconnected(controller)
    val existingController = controllers.firstOrNull { it is Control.GamepadControl && it.controller == controller } as Control.GamepadControl?
    existingController?.disconnect()
  }
}