package lavagames.towers.ecs.systems

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import lavagames.towers.dependencies.Ctx

class Box2dDebugRenderSystem(
    private val world: World = Ctx.context.inject(),
    private val camera:Camera = Ctx.context.inject<OrthographicCamera>()) : EntitySystem(1000) {
  private val debugRenderer: Box2DDebugRenderer = Box2DDebugRenderer()

  override fun update(deltaTime: Float) {
    super.update(deltaTime)
	  //This might go bonkers, though
    debugRenderer.render(world, camera.combined)
  }
}