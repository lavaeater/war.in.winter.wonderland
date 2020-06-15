package lavagames.towers.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.math.vec2
import lavagames.towers.ecs.components.CrossHairComponent
import lavagames.towers.ecs.components.PlayerWalkAndAimComponent
import lavagames.towers.ecs.components.TransformComponent

class CrossHairSystem : IteratingSystem(
    allOf(PlayerWalkAndAimComponent::class,
		    TransformComponent::class,
		    CrossHairComponent::class).get()) {

  private val transMapper = mapperFor<TransformComponent>()
  private val crossMapper = mapperFor<CrossHairComponent>()
  private val length = 0.7f
  private val vector = vec2(1f, 1f)

  /**
   * This method is called on every entity on every update call of the EntitySystem. Override this to implement your system's
   * specific processing.
   * @param entity The current Entity being processed
   * @param deltaTime The delta time between the last and current frame
   */
  override fun processEntity(entity: Entity, deltaTime: Float) {
    /*
    Do we need the walkandaim component, really?

    1. set angle of vector to transforms rotation
    2. set length to length.
    3. calculate x and y for crosshaircomponent
    4. profit
     */
    val t = transMapper.get(entity)
    vector.setLength(length)
    vector.setAngle(t.rotation)
    val ch = crossMapper.get(entity)
    ch.position.set(t.position.x + vector.x, t.position.y - vector.y)
  }

}