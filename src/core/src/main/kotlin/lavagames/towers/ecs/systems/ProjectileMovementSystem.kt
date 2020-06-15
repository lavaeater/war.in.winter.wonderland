package lavagames.towers.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.math.minus
import ktx.math.times
import lavagames.towers.ecs.components.ProjectileComponent
import lavagames.towers.ecs.components.TransformComponent

class ProjectileMovementSystem : IteratingSystem(allOf(ProjectileComponent::class, TransformComponent::class).get()) {
  private val projectileMapper = mapperFor<ProjectileComponent>()
  private val transformMapper = mapperFor<TransformComponent>()
  override fun processEntity(entity: Entity, deltaTime: Float) {
    val pc = projectileMapper[entity]!!
    val t = transformMapper[entity]!!

    pc.currentSpeed += pc.acc * deltaTime
    val moveVector = pc.direction * pc.currentSpeed
    t.position.set(t.position - moveVector)
  }
}
