package lavagames.towers.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMapRenderer
import ktx.ashley.allOf
import ktx.ashley.has
import ktx.ashley.mapperFor
import ktx.graphics.use
import lavagames.towers.Assets
import lavagames.towers.ecs.components.*
import lavagames.towers.extensions.toAnimKey


/*
Can we use sealed classes for the renderable components?

They will not all be animated fuckers, some will be static
images etc.
 */

class RenderSystem(
		private val mapRenderer: TiledMapRenderer,
		private val spriteBatch: Batch,
		private val camera: OrthographicCamera,
		private val pixelsPerMeter: Float) : SortedIteratingSystem(
		allOf(RenderableComponent::class,
				TransformComponent::class,
				VisibleComponent::class
		).get(),
		EntityYOrderComparator(),
		5) {

	private val transformMapper = mapperFor<TransformComponent>()
	private val spriteMapper = mapperFor<RenderableComponent>()
	private var stateTime = 0f
	private val metersPerPixel = 1 / pixelsPerMeter

	override fun processEntity(entity: Entity, deltaTime: Float) {
		val spriteComponent = spriteMapper[entity]

		when(spriteComponent.type) {
			is RenderType.AnimatedCharacterComponent -> renderAnimatedCharacter(entity, deltaTime, spriteComponent.type)
			is RenderType.StaticTextureComponent -> renderStaticTexture(entity, deltaTime, spriteComponent.type)
		}
		renderCrosshair(entity)
	}

	private fun renderStaticTexture(entity: Entity, deltaTime: Float, renderType: RenderType.StaticTextureComponent) {
		val texture = Assets.textures[renderType.textureName]!!
		val pos = transformMapper[entity]!!
		spriteBatch.drawScaled(
				texture,
				(pos.position.x - (texture.regionWidth / 2 * metersPerPixel)),
				(pos.position.y - (texture.regionHeight / 2 * metersPerPixel)),
				metersPerPixel)
	}

	private fun renderAnimatedCharacter(entity: Entity, deltaTime: Float, renderType: RenderType.AnimatedCharacterComponent) {
		val transform = transformMapper[entity]
		stateTime+=deltaTime
		/*
		We need to keep track of anims outside of the render system
		it needs to keep track of the state represented by the direction
		of the character, later...
		 */
		//TODO: We might move this to another system that just fixes animations for animated sprites. That would make
		val texture = Assets.characters[renderType.id]!![(transform.rotation).toAnimKey()]!!.getKeyFrame(stateTime, true)

		//TODO: Idle anims, we need idle!
		spriteBatch.drawScaled(
				texture,
				transform.position.x - (texture.regionWidth / 2 * metersPerPixel),
				transform.position.y - (texture.regionHeight / 2 * metersPerPixel),
				metersPerPixel)
	}

	override fun update(deltaTime: Float) {
		//1. Render maps bottom layer (layers later)
		//camera is updated in  camerafollowsystem
		camera.update()
		mapRenderer.setView(camera)
		mapRenderer.render()
		spriteBatch.projectionMatrix = camera.combined
		spriteBatch.use {
			//Render the entities
			super.update(deltaTime)
		}
	}

	private val crossHairTexture = Assets.textures["crosshair"]!!
	private val chMapper = mapperFor<CrossHairComponent>()

	private fun renderCrosshair(entity:Entity) {
		if(!entity.has(chMapper)) return

		val chc = chMapper[entity]!!
		spriteBatch.drawScaled(
				crossHairTexture,
				chc.position.x - (crossHairTexture.regionWidth / 2 * metersPerPixel),
				chc.position.y - (crossHairTexture.regionHeight / 2 * metersPerPixel),
				metersPerPixel)
	}
}

fun Batch.drawScaled(
		textureRegion: TextureRegion,
		x: Float,
		y: Float,
		scale: Float = 1f,
		rotation: Float = 0f) {

	draw(
	textureRegion,
	x,
	y,
	0f,
	0f,
	textureRegion.regionWidth.toFloat(),
	textureRegion.regionHeight.toFloat(),
	scale,
	scale,
	rotation)
}

