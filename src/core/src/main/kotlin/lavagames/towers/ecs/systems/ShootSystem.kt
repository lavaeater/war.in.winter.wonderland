package lavagames.towers.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.math.vec2
import lavagames.towers.dependencies.Ctx
import lavagames.towers.ecs.*
import lavagames.towers.ecs.components.Box2dBodyComponent
import lavagames.towers.ecs.components.PlayerShootComponent
import lavagames.towers.ecs.components.TransformComponent

class ShootSystem(private val pixelsPerMeter: Float) : IteratingSystem(allOf(PlayerShootComponent::class, TransformComponent::class).get()) {
  private val shootMapper = mapperFor<PlayerShootComponent>()
  private val transformMapper = mapperFor<TransformComponent>()
  private val bodyMapper = mapperFor<Box2dBodyComponent>()
  private val metersPerPixel = 1 / pixelsPerMeter

  private val entityFactory = Ctx.context.inject<EntityFactory>()
  override fun processEntity(entity: Entity, deltaTime: Float) {
    val wac = shootMapper[entity]!!
    if(wac.shoot(deltaTime)) {
      //1. Create a entity
      val t = transformMapper[entity]!!
      val b = bodyMapper.get(entity)

      //Calculate startpos using aimvector
      val aV =vec2(-1 * wac.aimVector.x, wac.aimVector.y).nor().scl(0.5f)
      val sV = vec2(t.position.x - aV.x, t.position.y - aV.y)
      val speedVector = vec2(wac.aimVector.x, -1f * wac.aimVector.y).nor().scl(.5f * metersPerPixel)
      entityFactory.createAndAddEntityToEngine(EntityFactory.ProjectileEntity) {
        it.apply {
          withTransform(sV.x, sV.y)
          withMovingBall(
              sV.x,
              sV.y,
              6f * metersPerPixel,
              0.001f,
              speedVector,
              PhysicsBodyType.Projectile(it))
        }
      }
    }
  }
}