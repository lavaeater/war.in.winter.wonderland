package lavagames.towers.data

import com.badlogic.gdx.controllers.Controller

sealed class Control(val id: String, connected: Boolean = false) {
  var isConnected = connected
  val connected:Boolean get() = isConnected

  fun disconnect() {
    isConnected = false
  }

  fun connect() {
    isConnected = true
  }

  abstract val pad: Pad

  class KeyboardControl : Control("keyboard", true) {
    override val pad: Pad by lazy { KeyboardPad() }
  }

  class GamepadControl(var controller: Controller) : Control(controller.toString(), true) {
    override val pad: Pad by lazy {GamePad(controller)}
  }
}

