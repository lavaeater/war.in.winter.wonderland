package lavagames.towers.ecs.systems

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import ktx.app.KtxInputAdapter
import ktx.math.minus
import ktx.math.times
import ktx.math.vec2
import lavagames.towers.dependencies.Ctx

/*
The iterating part of this system is not really in use right now...

Maybe it should just be an entity system that grabs all the entities
in one go...
 */
class ZoomSystem(
    private val speed: Float = 1f,
    private val inputProcessor: InputProcessor) :
    KtxInputAdapter, EntitySystem() {

  private var pInput = true
	private val camera by lazy { Ctx.context.inject<OrthographicCamera>()}
  private var zoomSpeed = 0f


  var processInput: Boolean
    get() = this.pInput
    set(value) {
      this.pInput = processInput
      if(value) {
        (inputProcessor as InputMultiplexer).addProcessor(this)
      } else {
        (inputProcessor as InputMultiplexer).removeProcessor(this)
      }
    }

  init {
    (inputProcessor as InputMultiplexer).addProcessor(this)
  }

  override fun update(deltaTime: Float) {
    super.update(deltaTime)

    camera.zoom += zoomSpeed

  }

  override fun keyDown(keycode: Int): Boolean {
    if(!processInput) return false
    return when (keycode) {
      Input.Keys.U -> {
        zoomSpeed = 0.05f
        true
      }
      Input.Keys.J -> {
        zoomSpeed = -0.05f
        true
      }
      else -> false
    }
  }

  private fun touchYtoScreenY(y: Int): Int {
    return Gdx.graphics.height - 1 - y
  }

  private fun touchToVector(touchX: Int, touchY: Int): Vector2 {
    return vec2((Gdx.graphics.width / 2 - touchX).toFloat(), (Gdx.graphics.height / 2 - touchYtoScreenY(touchY)).toFloat()).nor()
  }

  override fun keyUp(keycode: Int): Boolean {
    if(!processInput) return false
    return when (keycode) {
      Input.Keys.U -> {
        zoomSpeed = 0f
        true
      }
      Input.Keys.J -> {
        zoomSpeed = 0f
        true
      }
      else -> false
    }
  }
}

fun Vector2.directionalVelocity(velocity : Float) : Vector2 {
  return (vec2(0f,0f) - this).nor() * velocity
}

fun Vector2.moveTowards(target: Vector2, velocity: Float) : Vector2 {
  return (target - this).nor() * velocity
}