package lavagames.towers.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import ktx.math.vec2

data class TransformComponent(
		private var x: Float = 0f,
		private var y: Float = 0f,
		var rotation: Float = 0f,
    val position: Vector2 = vec2(x,y)) : Component