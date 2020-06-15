package lavagames.towers.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2

data class ProjectileComponent(
		val acc: Float = 10f,
		val topSpeed: Float = 50f,
		var currentSpeed: Float = 5f,
		val direction: Vector2) : Component
