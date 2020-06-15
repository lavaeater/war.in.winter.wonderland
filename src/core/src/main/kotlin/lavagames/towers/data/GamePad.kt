package lavagames.towers.data

import com.badlogic.gdx.controllers.Controller

class GamePad(var controller: Controller) : Pad {
  var isConnected = true

  override val RB: Boolean
    get() = controller.getButton(XBox360Pad.BUTTON_RB)

  override val LB: Boolean
    get() = controller.getButton(XBox360Pad.BUTTON_LB)

  override val RT: Float
    get() = controller.getAxis(XBox360Pad.AXIS_RIGHT_TRIGGER)

  override val LT: Float
    get() = controller.getAxis(XBox360Pad.AXIS_LEFT_TRIGGER)

  override val A: Boolean
    get() = controller.getButton(XBox360Pad.BUTTON_A)

  override val B: Boolean
    get() = controller.getButton(XBox360Pad.BUTTON_B)

  override val X: Boolean
    get() = controller.getButton(XBox360Pad.BUTTON_X)

  override val Y: Boolean
    get() = controller.getButton(XBox360Pad.BUTTON_Y)

  override val AxisLeftX: Float
    get() = controller.getAxis(XBox360Pad.AXIS_LEFT_X)

  override val AxisLeftY: Float
    get() = controller.getAxis(XBox360Pad.AXIS_LEFT_Y)

  override val AxisRightX: Float
    get() = controller.getAxis(XBox360Pad.AXIS_RIGHT_X)

  override val AxisRightY: Float
    get() = controller.getAxis(XBox360Pad.AXIS_RIGHT_Y)

  override val connected: Boolean
    get() = isConnected
}