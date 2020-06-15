package lavagames.towers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Disposable
import ktx.collections.toGdxArray
import ktx.scene2d.Scene2DSkin
import lavagames.towers.data.GameSettings
import lavagames.towers.spritesheet.SheetDef
import lavagames.towers.spritesheet.TextureRegionDef

object Assets : Disposable {
  private lateinit var gameSettings: GameSettings
  private lateinit var am: AssetManager

  private const val IDLE = "idle"
  private const val WALK = "walk"
  private const val GESTURE = "gesture"
  private const val ATTACK = "attack"
  private const val DEATH = "death"

  const val WALKNORTH = "walknorth"
  const val WALKEAST = "walkeast"
  const val WALKSOUTH = "walksouth"
  const val WALKWEST = "walkwest"

  val characters = mutableMapOf<Int, MutableMap<String, Animation<TextureRegion>>>()

  val animatedCharacterSprites by lazy { mutableMapOf<String, Map<String, List<Sprite>>>() }

  val textures by lazy { mutableMapOf<String, TextureRegion>() }

  fun load(gameSettings: GameSettings): AssetManager {
    Assets.gameSettings = gameSettings
    am = AssetManager()

    initUi()
    initAnimations()
    initSprites()

    return am
  }

  private fun initSprites() {
    textures["snowball"] = TextureRegion(Texture(Gdx.files.internal("sprites/snowball.png")))
    textures["crosshair"] = TextureRegion(Texture(Gdx.files.internal("sprites/crosshair.png")))
  }

  private fun initUi() {
    Scene2DSkin.defaultSkin = Skin(Gdx.files.internal("skins/uiskin.json"))
  }

  private fun initAnimations() {


    //USe our internal definition of the textureRegions
    val sheetDef = SheetDef(
        "WalkAnimations'",
        listOf(
            TextureRegionDef(WALKNORTH, 8, 9),
            TextureRegionDef(WALKWEST, 9, 9),
            TextureRegionDef(WALKSOUTH, 10, 9),
            TextureRegionDef(WALKEAST, 11, 9)
        ))
    /*
    The above def is all we need to load the anims, really.
    */
    val basePath = "chars"
    for (i in 1..4) {
      characters[i] = mutableMapOf()
      val texture = Texture(Gdx.files.internal("$basePath/$i.png"))
      for (region in sheetDef.textureRegionDef) {

        characters[i]!![region.name] = Animation(0.2f, (1 until region.frames)
            .map { TextureRegion(
                texture,
                it * sheetDef.itemWidth,
                region.row * sheetDef.itemHeight,
                sheetDef.itemWidth,
                sheetDef.itemHeight) }.toGdxArray())
      }
    }
  }

  private fun createAndAddSprite(spriteCollection: HashMap<String, MutableList<Sprite>>, atlas: TextureAtlas, region: TextureAtlas.AtlasRegion, width: Float, height: Float, spriteKey: String) {
    spriteCollection[spriteKey]!!.add(atlas.createSprite(region.name).apply {
      setSize(width, height)
      setOriginCenter()
    })
  }

  override fun dispose() {
  }
}
