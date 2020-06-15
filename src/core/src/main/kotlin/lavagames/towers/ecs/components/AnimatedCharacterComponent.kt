package lavagames.towers.ecs.components

import com.badlogic.ashley.core.Component



class RenderableComponent(val type: RenderType) : Component

sealed class RenderType {
	data class AnimatedCharacterComponent(val id: Int = getId()): RenderType() {
		companion object {
			var currentIndex = 0
			fun getId(): Int {
				currentIndex++
				return currentIndex
			}
		}
	}
	data class StaticTextureComponent(val textureName: String) : RenderType()
}