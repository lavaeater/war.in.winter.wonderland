package lavagames.towers

import com.badlogic.gdx.Screen
import ktx.app.KtxGame
import lavagames.towers.data.Control
import lavagames.towers.data.GameSettings
import lavagames.towers.dependencies.Ctx
import lavagames.towers.screens.CharacterEditorScreen
import lavagames.towers.screens.StartScreen
import lavagames.towers.screens.TiledMapScreen

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms.  */
class WarInAWinterWonderland : KtxGame<Screen>(), GameFlowControl {
	override fun tryToStartGame(control: Control) {
		if(playingControllers.contains(control.id	)) {
			startLevel()
		}
	}

	override fun removeController(control: Control) {
		playingControllers.remove(control.id)
	}

	override fun startLevel() {
		//levels and shit
		getScreen<TiledMapScreen>().mapName = "second"
		setScreen<TiledMapScreen>()
	}

	override fun pauseGame() {
		pause()
	}

	override fun levelFinished() {
		setScreen<StartScreen>()
	}

	override fun addController(control: Control) {
		playingControllers[control.id] = control
	}

	override val playingControllers = mutableMapOf<String, Control>()

	override val gameSettings = GameSettings()

	override fun create() {
		Assets.load(gameSettings)
		Ctx.buildContext(gameSettings)
		addScreen(TiledMapScreen(this))
		addScreen(StartScreen(this))
 //		addScreen(CharacterEditorScreen(this))
		showStartScreen()
		//showCharacterEditorScreen()
	}

	override fun showCharacterEditorScreen() {
		setScreen<CharacterEditorScreen>()
	}

	override fun showStartScreen() {
		setScreen<StartScreen>()
	}
}

