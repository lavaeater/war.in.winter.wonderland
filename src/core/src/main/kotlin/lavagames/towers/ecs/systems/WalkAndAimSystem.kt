package lavagames.towers.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.math.times
import lavagames.towers.ecs.components.Box2dBodyComponent
import lavagames.towers.ecs.components.PlayerWalkAndAimComponent
import lavagames.towers.ecs.components.TransformComponent

/*
Refactor this to act on the player kinematic body!
 */

class WalkAndAimSystem : IteratingSystem(allOf(PlayerWalkAndAimComponent::class, Box2dBodyComponent::class).get()) {
  private val speed = 2f
  private val walkAndAimMapper = mapperFor<PlayerWalkAndAimComponent>()
  private val bodyMapper = mapperFor<Box2dBodyComponent>()
  private val transMapper = mapperFor<TransformComponent>()

  override fun processEntity(entity: Entity, deltaTime: Float) {
    val wac = walkAndAimMapper.get(entity)
    bodyMapper.get(entity).apply {
      body.linearVelocity = wac.walkVector * speed
    }
    transMapper.get(entity).apply { rotation = wac.aimVector.angle() }
  }
}