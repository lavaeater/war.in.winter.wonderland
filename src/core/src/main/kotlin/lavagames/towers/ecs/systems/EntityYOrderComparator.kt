package lavagames.towers.ecs.systems

import com.badlogic.ashley.core.Entity
import ktx.ashley.mapperFor
import lavagames.towers.ecs.components.TransformComponent

class EntityYOrderComparator : Comparator<Entity> {

	val transMpr = mapperFor<TransformComponent>()

	override fun compare(o1: Entity?, o2: Entity?): Int {
		if (o1 != null || o2 != null) {
			val t1 = transMpr[o1]!!
			val t2 = transMpr[o2]!!
			if (t1.position.y == t2.position.y) return 0
			return if (t1.position.y > t2.position.y) -1 else 1
		}
		return 0
	}

}