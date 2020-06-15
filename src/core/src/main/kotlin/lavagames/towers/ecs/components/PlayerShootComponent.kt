package lavagames.towers.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import ktx.math.vec2
import lavagames.towers.data.Pad

class PlayerShootComponent(val pad: Pad) : Component {
  private var aim = vec2(0f,1f)
  private val timeBetweenShots = 0.5f
  private val firing : Boolean get() = pad.RT > -0.8f
  val aimVector: Vector2 get() = aim.set(pad.AxisRightX, -1 * pad.AxisRightY)
  var delta = 0f

  private val canShoot get() = delta > timeBetweenShots
  fun shoot(d: Float) : Boolean {
    //Tries to shoot with d as the elapsed time since last trying.
    //Returns true if we could indeed fire and also resets the delta.
    return if(firing) {
      delta += d
      if (canShoot) {
        delta = 0f
        true
      } else false
    } else {
      delta = 0f
      false
    }
  }
}