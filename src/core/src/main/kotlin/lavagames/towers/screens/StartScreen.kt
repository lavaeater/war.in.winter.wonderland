package lavagames.towers.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.controllers.ControllerAdapter
import com.badlogic.gdx.controllers.Controllers
import ktx.app.KtxInputAdapter
import lavagames.towers.GameFlowControl
import lavagames.towers.data.Control
import lavagames.towers.data.XBox360Pad

class StartScreen(game: GameFlowControl) : GameScreen(game) {

  private val adapter = object:ControllerAdapter() {
    override fun buttonUp(controller: Controller, buttonIndex: Int): Boolean {
      //So, what happens?
      when(buttonIndex) {
        XBox360Pad.BUTTON_A -> game.addController(Control.GamepadControl(controller))
        XBox360Pad.BUTTON_B -> game.removeController(Control.GamepadControl(controller))
        XBox360Pad.BUTTON_START -> game.tryToStartGame(Control.GamepadControl(controller))
      }
      return true
    }
  }

  private val inputAdapter = object : KtxInputAdapter {
    override fun keyUp(keycode: Int): Boolean {
      when(keycode) {
        Input.Keys.SPACE -> game.addController(Control.KeyboardControl())
        Input.Keys.ESCAPE -> game.removeController(Control.KeyboardControl())
        Input.Keys.ENTER -> game.tryToStartGame(Control.KeyboardControl())
      }
      return true
    }
  }

  override fun show() {
    Controllers.addListener(adapter)
    Gdx.input.inputProcessor = inputAdapter
  }

  override fun hide() {
    Controllers.removeListener(adapter)
    //No need to remove input processor, we will set it to the other screen later...
  }

  override fun render(delta: Float) {
    super.render(delta)
    /*
    show some stuff here...
     */
  }
}