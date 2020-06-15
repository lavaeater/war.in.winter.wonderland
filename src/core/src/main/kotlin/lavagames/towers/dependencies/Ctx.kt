package lavagames.towers.dependencies

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.box2d.createWorld
import ktx.inject.Context
import ktx.inject.register
import lavagames.towers.data.GameSettings
import lavagames.towers.ecs.BodyFactory
import lavagames.towers.ecs.CollisionListener
import lavagames.towers.ecs.EntityFactory

class Ctx {

	companion object {
		val context = Context()

		private fun getEngine() : Engine {
			return Engine()
		}

		fun buildContext(gameSettings:GameSettings) {
			context.register {
				bindSingleton<InputProcessor>(InputMultiplexer())
				bindSingleton(SpriteBatch() as Batch)
				bindSingleton(ShapeRenderer())
				bindSingleton(OrthographicCamera())

				//Bind provider for a viewport with the correct settings for this game!
				bind<Viewport> {
					ExtendViewport(gameSettings.width,
							gameSettings.height,
							this.inject<OrthographicCamera>())
				}
				bindSingleton(getEngine())
				bindSingleton(createWorld().apply {
					setContactListener(CollisionListener())
				})
				bindSingleton(BodyFactory(inject()))
				bindSingleton(EntityFactory(pixelsPerMeter = gameSettings.pixelsPerMeter))
			}
		}
	}
}