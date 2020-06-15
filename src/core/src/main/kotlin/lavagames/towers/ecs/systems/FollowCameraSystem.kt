package lavagames.towers.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.Camera
import lavagames.towers.ecs.components.CameraFollowComponent
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import lavagames.towers.ecs.components.TransformComponent

class FollowCameraSystem(private val camera:Camera) : EntitySystem(300) {
	private var needsInit = true
	private lateinit var trackedEntity: Entity
	private lateinit var transformComponent: TransformComponent
	private val speed = 10f

	override fun update(deltaTime: Float) {
		if(needsInit) {
			lateInit()
		}
		camera.position.set(transformComponent.position, 0f)
	}

	private fun lateInit() {
		/*
		This late init stuff is used to enable us to create this system without having any
		entities in it and stuff. We'll figure out the best way to do this as we go.
		 */

		needsInit = false
		trackedEntity = engine.getEntitiesFor(allOf(CameraFollowComponent::class).get()).first()
		transformComponent = mapperFor<TransformComponent>()[trackedEntity]
	}
}