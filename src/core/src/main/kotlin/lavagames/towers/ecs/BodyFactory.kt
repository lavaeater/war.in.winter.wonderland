package lavagames.towers.ecs

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import ktx.box2d.body
import ktx.box2d.box
import ktx.box2d.circle

class BodyFactory(private val world: World) {

  fun createBox(width: Float,
                height: Float,
                densityIn: Float,
                position: Vector2,
                bodyType: BodyDef.BodyType): Body {

    return world.body {
      this.position.set(position)
      angle = 0f
      fixedRotation = true
      type = bodyType
      box(width, height) {
        density = densityIn
        friction = 0f
      }
    }
  }

  fun createBall(radius:Float,
                densityIn: Float,
                position: Vector2,
                bodyType: BodyDef.BodyType): Body {

    return world.body {
      this.position.set(position)
      angle = 0f
      fixedRotation = true
      type = bodyType
      //Do we need to dispose
      circle(radius) {
        density = densityIn
        friction = 0f
      }
    }
  }
}