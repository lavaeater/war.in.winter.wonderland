package lavagames.towers.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import com.badlogic.gdx.Gdx
import ktx.ashley.mapperFor
import ktx.ashley.oneOf
import lavagames.towers.ecs.components.PlayerShootComponent
import lavagames.towers.ecs.components.PlayerWalkAndAimComponent

class LogSystem : IntervalIteratingSystem(oneOf(PlayerWalkAndAimComponent::class, PlayerShootComponent::class).get(),0.2f)  {
  val pwaMapper = mapperFor<PlayerWalkAndAimComponent>()
  val psMapper = mapperFor<PlayerShootComponent>()

  override fun processEntity(entity: Entity) {
    //Every entity comes in here and has some components
    if(psMapper.has(entity))
      logWalkAndAim(entity)
  }



  private fun logWalkAndAim(entity: Entity) {
    val c = psMapper[entity]!!
    Gdx.app.debug("Player Aim: ", "av:${c.aimVector.angle()}")


    //Gdx.app.debug("Player Aim: ", "ax:${c.aimVector.x}, ay: ${c.aimVector.y}, aa:${c.aimVector.angle()}")
  }
}