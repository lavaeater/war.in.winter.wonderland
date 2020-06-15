package lavagames.towers.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import ktx.math.vec2
import lavagames.towers.data.Pad

class PlayerWalkAndAimComponent(val pad: Pad) : Component {
  /*
  This will be a fairly advanced component. I don't even see how we could
  make it work with the keyboard component at the moment, I will figure it out though
   */

  private var aim = vec2(0f,1f)
  private var walk = vec2(0f,0f)

  val aimVector: Vector2 get() = aim.set(pad.AxisRightX, -1 * pad.AxisRightY)

  val walkVector: Vector2 get() = walk.set(pad.AxisLeftX, pad.AxisLeftY)

  //Add props for idle / or other actions
  val mode: String get() = if(walkVector != Vector2.Zero) "WALK" else "IDLE"
}

/*
Thoughts on gamepads and axes - The values reported by the pad
are device and driver specific. We need to define what we should
use inside our game, what the overall "up down, left right" policy
should be, for every implementation of pad / device that we use.

So, the keyboard UP should be the same value as corresponding UP
for the gamepad - the Pad Interface should always return +1 for Y axis
as up (since the coordinate system is Y up) and always return -1 for
left for x-axes.

This means that the walking and aiming vectors can be predictably
implemented, transforming the values from negatives to positives
as needed.

 */

