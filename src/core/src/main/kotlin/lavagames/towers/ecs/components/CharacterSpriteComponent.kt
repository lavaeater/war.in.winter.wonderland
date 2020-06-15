package lavagames.towers.ecs.components

import com.badlogic.ashley.core.Component

class CharacterSpriteComponent(val spriteKey: String,
                               val animated:Boolean = true,
                               var currentAnim:String = "idle",
                               var currentIndex : Int = 0,
                               var deltaTime: Float = 0f) : Component