package lavagames.towers.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.physics.box2d.World
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import lavagames.towers.dependencies.Ctx
import lavagames.towers.ecs.components.Box2dBodyComponent
import lavagames.towers.ecs.components.TransformComponent

/**@
 * This goddamned system will transform box2d coordinates into
 * pixel coordinates.
 *
 * This will mean that we don't need to think about
 * meters and stuff in any other places than when setting up the world
 * and when ... transforming box2d-coords into world-coords
 *
 * No scaling needed anywhere else! No weird scaling of textures or nothing.
 */
class Box2dTransformSystem(
    private val world: World = Ctx.context.inject()) : IteratingSystem(allOf(
    TransformComponent::class,
    Box2dBodyComponent::class).get()) {
  private val bodyMpr = mapperFor<Box2dBodyComponent>()
  private val transMpr = mapperFor<TransformComponent>()

  override fun processEntity(entity: Entity, deltaTime: Float) {
    transMpr[entity]!!.position.set(bodyMpr[entity]!!.body.position)
  }

  override fun update(deltaTime: Float) {
    val frameTime = Math.min(deltaTime, 0.25f)
    accumulator += frameTime
    if (accumulator >= MAX_STEP_TIME) {
      world.step(MAX_STEP_TIME, 6, 2)
      accumulator -= MAX_STEP_TIME
    }
    super.update(deltaTime)
  }

  companion object {
    private val MAX_STEP_TIME = 1 / 60f
    private var accumulator = 0f
  }
}

