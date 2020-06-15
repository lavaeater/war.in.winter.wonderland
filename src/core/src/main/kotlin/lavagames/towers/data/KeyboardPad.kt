package lavagames.towers.data

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter

class KeyboardPad : Pad, InputAdapter() {

  private var rb = false
  private var lb = false
  private var a = false
  private var b = false
  private var x = false
  private var y = false
  private var alx = 0f
  private var aly = 0f
  private var arx = 0f
  private var ary = 0f

  private var lt = 0f
  private var rt = -1f

  override fun keyDown(keycode: Int): Boolean {
    return when(keycode) {
      Input.Keys.W -> {
        arx  = 0f
        ary = 1f
        aly = 1f
        true
      }
      Input.Keys.S -> {
        arx = 0f
        ary = -1f
        aly = -1f
        true
      }
      Input.Keys.A -> {
        ary = 0f
        arx = -1f
        alx = -1f
        true
      }
      Input.Keys.D -> {
        ary = 0f
        arx = 1f
        alx = 1f
        true
      }
      Input.Keys.SPACE -> {
        rt = 1f
        true
      }
      else -> false
    }
  }

  override fun keyUp(keycode: Int): Boolean {
    return when (keycode) {
      Input.Keys.W -> {
        aly = 0f
        true
      }
      Input.Keys.S -> {
        aly = 0f
        true
      }
      Input.Keys.A -> {
        alx = 0f
        true
      }
      Input.Keys.D -> {
        alx = 0f
        true
      }
      Input.Keys.SPACE -> {
        rt = -1f
        true
      }
      else -> false
    }
  }

  override val RB: Boolean
    get() = rb
  override val RT: Float
    get() = rt
  override val LB: Boolean
    get() = lb
  override val LT: Float
    get() = lt
  override val A: Boolean
    get() = a
  override val B: Boolean
    get() = b
  override val X: Boolean
    get() = x
  override val Y: Boolean
    get() = y
  override val AxisLeftX: Float
    get() = alx
  override val AxisLeftY: Float
    get() = aly
  override val AxisRightX: Float
    get() = arx
  override val AxisRightY: Float
    get() = ary
  override val connected: Boolean
    get() = true
}