package lavagames.towers.data

import com.badlogic.gdx.controllers.PovDirection


class XBox360Pad {
  companion object {

    /*
     * It seems there are different versions of gamepads with different ID
     Strings.
     * Therefore its IMO a better bet to check for:
     * if (controller.getName().toLowerCase().contains("xbox") &&
                   controller.getName().contains("360"))
     *
     * Controller (Gamepad for Xbox 360)
       Controller (XBOX 360 For Windows)
       Controller (Xbox 360 Wireless Receiver for Windows)
       Controller (Xbox wireless receiver for windows)
       XBOX 360 For Windows (Controller)
       Xbox 360 Wireless Receiver
       Xbox Receiver for Windows (Wireless Controller)
       Xbox wireless receiver for windows (Controller)
     */
    //public static final String ID = "XBOX 360 For Windows (Controller)"
    const val BUTTON_X = 2
    const val BUTTON_Y = 3
    const val BUTTON_A = 0
    const val BUTTON_B = 1
    const val BUTTON_BACK = 6
    const val BUTTON_START = 7
    val BUTTON_DPAD_UP = PovDirection.north
    val BUTTON_DPAD_DOWN = PovDirection.south
    val BUTTON_DPAD_RIGHT = PovDirection.east
    val BUTTON_DPAD_LEFT = PovDirection.west
    const val BUTTON_LB = 4
    const val BUTTON_L3 = 8
    const val BUTTON_RB = 5
    const val BUTTON_R3 = 9
    const val AXIS_LEFT_X = 0 //-1 is left | +1 is right
    const val AXIS_LEFT_Y = 1 //-1 is up | +1 is down
    const val AXIS_LEFT_TRIGGER = 4 //value 0 to 1f
    const val AXIS_RIGHT_X = 2 //-1 is left | +1 is right
    const val AXIS_RIGHT_Y = 3 //-1 is up | +1 is down
    const val AXIS_RIGHT_TRIGGER = 5 //value 0 to -1f
  }
}

fun String.isXbox360Pad() : Boolean {
  return this.toLowerCase().contains("xbox") && this.contains("360")
}