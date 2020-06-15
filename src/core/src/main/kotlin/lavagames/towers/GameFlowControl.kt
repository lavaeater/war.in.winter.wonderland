package lavagames.towers

import lavagames.towers.data.Control
import lavagames.towers.data.GameSettings

interface GameFlowControl {
	val playingControllers: MutableMap<String, Control>
	val gameSettings: GameSettings
	fun addController(control: Control)
	fun removeController(control: Control)
	fun startLevel()
	fun pauseGame()
	fun levelFinished()
	fun tryToStartGame(control: Control)
	fun showStartScreen()
	fun showCharacterEditorScreen()
}