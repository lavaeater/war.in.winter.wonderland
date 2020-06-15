package lavagames.towers.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.physics.box2d.World
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import lavagames.towers.dependencies.Ctx
import lavagames.towers.ecs.components.Box2dBodyComponent
import lavagames.towers.ecs.components.RemoveComponent

class Box2dRemoveBodySystem(
    private val world: World = Ctx.context.inject()):
    IteratingSystem(allOf(RemoveComponent::class, Box2dBodyComponent::class).get()) {

  private val bodyMapper = mapperFor<Box2dBodyComponent>()

  override fun processEntity(entity: Entity, deltaTime: Float) {
    val b2c = bodyMapper.get(entity)
    world.destroyBody(b2c.body)
    engine.removeEntity(entity)
  }
}