package lavagames.towers.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Rectangle

sealed class HitBox(var x: Float, y: Float) {
  fun overlaps(otherBox: HitBox):Boolean {
    return when(otherBox) {
      is RectangleBox -> overlaps(otherBox.rectangle)
      is CircleBox -> overlaps(otherBox.circle)
    }
  }

  fun overlaps(circle: Circle): Boolean {
    return when(this) {
      is RectangleBox -> rectangle.contains(circle)
      is CircleBox -> circle.contains(circle)
    }
  }

  fun overlaps(rectangle: Rectangle): Boolean {
    return when(this) {
      is RectangleBox -> rectangle.contains(rectangle)
      is CircleBox -> rectangle.contains(circle)
    }
  }

  class RectangleBox(x:Float, y:Float, val width:Float, val height:Float) : HitBox(x,y) {
    val rectangle = Rectangle(x,y, width, height)
  }

  class CircleBox(x:Float, y: Float, val radius:Float):HitBox(x,y) {
    val circle = Circle(x,y,radius)
  }
}

class HitboxComponent(val hitbox: HitBox) : Component {
}