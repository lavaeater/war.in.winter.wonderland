package lavagames.towers.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.ashley.allOf
import ktx.ashley.mapperFor
import ktx.scene2d.table
import lavagames.towers.ecs.components.PlayerWalkAndAimComponent
import lavagames.towers.ecs.components.RenderableComponent
import lavagames.towers.ecs.components.TransformComponent
import lavagames.towers.extensions.toAnimKey
import lavagames.towers.mvvm.ViewBase
import lavagames.towers.mvvm.ViewModelBase
import lavagames.towers.mvvm.bindableLabel
import lavagames.towers.mvvm.notifyChanged

/*

	To start, we'll just log some stuff
	Aaah, the debug info system!

	Lets do it as a panel that follows a player around on the playing field
	Lets make it so that we can show / hide it with a press of a button

	So, this system updates the info and shows any panels that are visible.

	The panel should be slightly transparent, perhaps?

	So this should be shown in the same viewport as the players?
 */


class DebugInfoSystem : IntervalIteratingSystem(
		allOf(
				TransformComponent::class,
				RenderableComponent::class).get(), 1f, 10000) {
	lateinit var debugViewModel:DebugViewModel
	val transformMapper = mapperFor<PlayerWalkAndAimComponent>()
	val characterMapper = mapperFor<RenderableComponent>()
	lateinit var transformComponent : PlayerWalkAndAimComponent
	var needsInit = true
	private lateinit var debugView: DebugView

	/**
	 * The user should place the entity processing logic here.
	 * @param entity
	 */
	override fun processEntity(entity: Entity) {
		if(needsInit) {
//			if(characterMapper[entity]!!.id == 1) {
//				transformComponent = transformMapper[entity]!!
//				debugViewModel = DebugViewModel().apply {
//					currentAngle = transformComponent.aimVector.angle()
//					currentAnim = currentAngle.toAnimKey()
//				}
//				debugView = DebugView(debugViewModel)
//				debugView.show()
//				needsInit = false
//			}
		}
	}

	override fun updateInterval() {
		super.updateInterval()
		//This is done every update!
		debugViewModel.currentAngle = transformComponent.aimVector.angle()
		debugViewModel.currentAnim = debugViewModel.currentAngle.toAnimKey()

		debugView.update(interval)
	}

	/**
	 * The processing logic of the system should be placed here.
	 */
}

class DebugViewModel : ViewModelBase() {
	var currentAngle by notifyChanged(0f)
	var currentAnim by notifyChanged("WALKSOUTH")
}

class DebugView(viewModel: DebugViewModel) : ViewBase<DebugViewModel>(SpriteBatch(), viewModel, false) {
	override fun initLayout() {
		stage.isDebugAll = true
		val debugInfoTable = table {
			top()
			left()
			table {
				bindableLabel(viewModel::currentAngle.name)
				row()
				bindableLabel(viewModel::currentAnim.name)
				center()
				width = uiWidth / 2f
				height = width
			}

		}
		stage.addActor(debugInfoTable)
	}

	override fun hide() {
	}

	/** Releases all resources of this object.  */
	override fun dispose() {
	}
}