package lavagames.towers.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.Batch
import lavagames.towers.GameFlowControl
import lavagames.towers.dependencies.Ctx
import lavagames.towers.ui.charactereditor.CharacterEditorView
import lavagames.towers.ui.charactereditor.CharacterEditorViewModel
import lavagames.towers.spritesheet.LpcSpriteSheetHelper
import lavagames.towers.spritesheet.SheetDef
import lavagames.towers.spritesheet.TextureRegionDef

class CharacterEditorScreen(game: GameFlowControl) : GameScreen(game) {

	private val batch : Batch = Ctx.context.inject()
	private val basePath = "localfiles/lpc"
	private val charEditor = CharacterEditorViewModel(LpcSpriteSheetHelper().categories)
	private val charEditorView = CharacterEditorView(batch, charEditor)

	private var currentAnim = 2

	private val sheetDef = SheetDef(
			"JustWalkin'",
			listOf(
					TextureRegionDef("walknorth", 8, 9),
					TextureRegionDef("walkwest", 9, 9),
					TextureRegionDef("walksouth", 10, 9),
					TextureRegionDef("walkeast", 11, 9)
			))

	private val genders = listOf("male", "female")

	override fun show() {
//		camera.position.set(125f, 125f, 0f)
//		camera.update()
		charEditorView.show()
	}

	val fps = 1f / 9f
	var accDelta = 0f
	var currentFrame = 1

	override fun render(delta: Float) {
		Gdx.gl.glClearColor(0.3f, 0.5f, 0.8f, 1f)
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

		accDelta += delta

		if(accDelta > fps) {
			accDelta = 0f
			currentFrame++

			if(currentFrame > 8)
				currentFrame = 1
		}
		charEditorView.update(delta)
	}

	override fun resize(width: Int, height: Int) {
		//charEditorView.resize(width, height) //viewPort.update(width, height)
		//batch.projectionMatrix = camera.combined
	}
}

