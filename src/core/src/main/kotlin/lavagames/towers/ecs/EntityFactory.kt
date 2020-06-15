package lavagames.towers.ecs

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import ktx.math.vec2
import lavagames.towers.data.Control
import lavagames.towers.data.Pad
import lavagames.towers.dependencies.Ctx
import lavagames.towers.ecs.components.*

class EntityFactory(
		private val engine: Engine = Ctx.context.inject(),
		private val pixelsPerMeter: Float) {

	private val metersPerPixel = 1 / pixelsPerMeter

	companion object {
		const val CharacterEntity = "character"
		const val ProjectileEntity = "entity"
		const val TargetPracticeEntity = "targetpractice"
	}

	fun createAndAddEntityToEngine(type:String, init: (e: Entity) -> Unit) : Entity {
		val entity = createEntity(type)
		init(entity)
		engine.addEntity(entity)
		return entity
	}

	private fun createEntity(type: String) : Entity {
		return when(type) {
			CharacterEntity -> createCharacterEntity()
			ProjectileEntity -> createProjectileEntity()
			else -> createTargetPracticeEntity()
		}
	}

	private fun createTargetPracticeEntity(): Entity {
		return engine.createEntity()
				.isCharacter()
				.visible()
				.withTexture("target")
	}

	private fun createProjectileEntity(): Entity {
		return engine.createEntity()
				.visible()
				.withTexture("snowball")
	}

	private fun createCharacterEntity(): Entity {
		return engine.createEntity()
				.isCharacter()
				.visible()
				.withAnimatedCharacter()
	}

	fun applyCharacterComponents(
			entity: Entity,
			x:Float,
			y: Float,
			playerControl: Control,
			follow: Boolean) {

		entity
				.withTransform(x * metersPerPixel, y * metersPerPixel)
				.withControls(playerControl.pad)
				.withBox(x * metersPerPixel, y * metersPerPixel, 24f * metersPerPixel, 36f * metersPerPixel, collisionInfo = PhysicsBodyType.Character(entity))
				.withCrossHairs()

			if(follow)
				entity.follow()
	}
}

fun Entity.withTransform(x:Float = 0f, y:Float = 0f, rotation:Float = 0f): Entity = apply { add(TransformComponent(x, y, rotation)) }

fun Entity.withControls(pad: Pad): Entity = apply {
	add(PlayerWalkAndAimComponent(pad))
	add(PlayerShootComponent(pad)) }

fun Entity.projectile(
		acc: Float = 10f,
    topSpeed: Float = 50f,
    currentSpeed: Float = 5f,
    direction: Vector2) : Entity = apply {add(ProjectileComponent(acc, topSpeed, currentSpeed, direction)) }

fun Entity.withTexture(textureName: String) : Entity = apply { add(RenderableComponent(RenderType.StaticTextureComponent(textureName))) }

fun Entity.visible() : Entity = apply { add(VisibleComponent())	}

fun Entity.withAnimatedCharacter() : Entity = apply { add(RenderableComponent(RenderType.AnimatedCharacterComponent())) }

fun Entity.isCharacter() : Entity = apply { add(CharacterComponent()) }

fun Entity.follow() : Entity = apply { add(CameraFollowComponent())}

fun Entity.withBox(
		x: Float = 0f,
		y: Float = 0f,
		width: Float = 1f,
		height: Float = 1f,
		density: Float = 10f,
		bodyType: BodyDef.BodyType = BodyDef.BodyType.DynamicBody,
		collisionInfo: PhysicsBodyType
) : Entity {
	val bodyFactory = Ctx.context.inject<BodyFactory>()
	apply {
		add(Box2dBodyComponent(bodyFactory.createBox(width,height,density, vec2(x,y), bodyType).apply {
			userData = collisionInfo
		}))
	}
	return this
}

fun Entity.withCrossHairs() : Entity {
	add(CrossHairComponent())
	return this
}

fun Entity.withMovingBall(
		x:Float = 0f,
    y:Float = 0f,
    radius:Float =1f,
    density:Float = 0.01f,
    linearVelocity: Vector2,
		collisionInfo: PhysicsBodyType) : Entity {
	val bodyFactory = Ctx.context.inject<BodyFactory>()
	apply {
		add(Box2dBodyComponent(bodyFactory.createBall(radius,density, vec2(x,y), BodyDef.BodyType.DynamicBody).apply {
			applyForce(linearVelocity, vec2(x,y), true)
			userData = collisionInfo
		}))
	}
	return this
}