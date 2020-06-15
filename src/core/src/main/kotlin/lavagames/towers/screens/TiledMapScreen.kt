package lavagames.towers.screens

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.maps.objects.PolygonMapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.box2d.body
import ktx.box2d.box
import ktx.box2d.polygon
import ktx.math.vec2
import lavagames.towers.GameFlowControl
import lavagames.towers.data.KeyboardPad
import lavagames.towers.dependencies.Ctx
import lavagames.towers.ecs.EntityFactory
import lavagames.towers.ecs.systems.*
import lavagames.towers.ecs.withTransform

class TiledMapScreen(game: GameFlowControl) : GameScreen(game) {
	private lateinit var tiledMapRenderer : OrthogonalTiledMapRenderer
	private val engine = Ctx.context.inject<Engine>()
	private val viewPort = Ctx.context.inject<Viewport>()
	private val batch = Ctx.context.inject<Batch>()
	private val camera = Ctx.context.inject<OrthographicCamera>()
	lateinit var mapName: String

	private lateinit var renderSystem : RenderSystem

	init {
		//Load map here?
    engine.addSystem(FollowCameraSystem(Ctx.context.inject<OrthographicCamera>()))
		engine.addSystem(ZoomSystem(inputProcessor = Ctx.context.inject()))
		engine.addSystem(WalkAndAimSystem())
		engine.addSystem(ShootSystem(game.gameSettings.pixelsPerMeter))
		engine.addSystem(Box2dTransformSystem())
		engine.addSystem(Box2dDebugRenderSystem())
		engine.addSystem(LogSystem())
		engine.addSystem(Box2dRemoveBodySystem())
		engine.addSystem(CrossHairSystem())
		batch.projectionMatrix = camera.combined
	}

	private lateinit var currentMap: TiledMap
	private val pixelsPerMeter: Float = game.gameSettings.pixelsPerMeter
	private val metersPerPixel: Float = 1 / pixelsPerMeter

	private fun loadMap(mapName: String = "first") {
		/*
		Since we are using, for now, a zoomable ui... we shouldn't, obviously. No, we should really
		just stop. We still need to scale everything and calculate everything.

		Here goes. We decide that 64 units in tiled equals 1 meter. This means that every tile is
		0.5 * 0.5 meters and that every person is basically 1 meters tall (they are children).

		So, what is the scale then?

		Our first map is 25 * 50 tiles, meaning 25 * 0.5 = 12.5 meters by 25 meters. According to docs,
		box2d works best with "things" that are no more than 100 meters on a side in size.

		So, positions bositions. Sizes Schmizes.

		It might be pertinent to... not have the transformation part of the components anymore?

		Or should transformations be in pixels? So we do the transformation in the physics system
		(which actually should be call physical transformation system?)

		WRONG!

		RIGHT?

		We only use box2d measurements.

		Everything we render should be a textureregion (for sameness)

		We add extension method for the rendering of textureregions with scaling.

		Will the goddamned tiled map work with this?

		So,
		 */

		currentMap = com.badlogic.gdx.maps.tiled.TmxMapLoader().load("maps/$mapName.tmx")
		//tileSize = currentMap.properties["tilewidth", Integer::class.java].toFloat()
		//scale = tileSize / game.gameSettings.width

		setupFixturesAndStuff()

		tiledMapRenderer = OrthogonalTiledMapRenderer(currentMap, metersPerPixel)
		renderSystem = RenderSystem(
				tiledMapRenderer,
				Ctx.context.inject(),
				Ctx.context.inject(),
				game.gameSettings.pixelsPerMeter)
    engine.addSystem(renderSystem) //whoopsie
	}

	private fun setupFixturesAndStuff() {
		for (mapObject in currentMap.layers.get(mapObjects).objects) {
			when(mapObject) {
				is PolygonMapObject -> setupPolygonObject(mapObject)
				is RectangleMapObject -> setupRectangleObject(mapObject)
			}
		}
	}

	private fun setupRectangleObject(mapObject: RectangleMapObject) {
		when(mapObject.properties.get("type")) {
			"start" -> setupStartArea(mapObject)
			"wall" -> setupWall(mapObject)
		}
	}

	private fun setupWall(mapObject: RectangleMapObject) {
		val world = Ctx.context.inject<World>()

		world.body {
			position.set(
					vec2(
							mapObject.rectangle.x + mapObject.rectangle.width / 2 ,
							mapObject.rectangle.y + mapObject.rectangle.height / 2).scl(metersPerPixel))
			angle = 0f
			fixedRotation = true
			type = BodyDef.BodyType.StaticBody
			box(mapObject.rectangle.width * metersPerPixel, mapObject.rectangle.height * metersPerPixel) {
				density = 10f
				friction = 0.1f
			}
		}
	}

	private lateinit var startArea: Rectangle

	private fun setupStartArea(mapObject: RectangleMapObject) {
		startArea = mapObject.rectangle //This is world coords, is good
	}

	private fun setupPolygonObject(mapObject: PolygonMapObject) {
		val pols = mapObject.polygon
		val world = Ctx.context.inject<World>()

		world.body {
			position.set(vec2(pols.x, pols.y).scl(metersPerPixel))
			angle = 0f
			fixedRotation = true
			type = BodyDef.BodyType.KinematicBody
			polygon(pols.vertices) {
				density = 10f
				friction = 0.1f
			}
		}
	}

	override fun show() {
		Gdx.input.inputProcessor = Ctx.context.inject()
		loadMap(mapName)
		addPlayers()
		//addTargetPractice()
	}

	private val entityFactory = Ctx.context.inject<EntityFactory>()

	private fun addPlayers() {

		for((i, pc) in game.playingControllers.values.withIndex()) {
			entityFactory.createAndAddEntityToEngine(EntityFactory.CharacterEntity) {
				entityFactory.applyCharacterComponents(it, startArea.x +  i * 64f, startArea.y, pc, i == 0)
			}

			if(pc.pad is KeyboardPad) {
				(Gdx.input.inputProcessor as InputMultiplexer).addProcessor(pc.pad as KeyboardPad)
			}
		}
	}

	private fun addTargetPractice() {
		for (i in 0..1) {
			entityFactory.createAndAddEntityToEngine(EntityFactory.TargetPracticeEntity) {
				it.apply {
					withTransform(i * 100f, i*50f,0f)
				}
			}
		}
	}

	override fun hide() {
		engine.removeAllEntities()
		engine.removeSystem(renderSystem)
		if(game.playingControllers.containsKey("keyboard")) {
			(Gdx.input.inputProcessor as InputMultiplexer).removeProcessor(game.playingControllers["keyboard"]!!.pad as KeyboardPad)
		}
	}

	override fun dispose() {
	}

	override fun resize(width: Int, height: Int) {
		viewPort.update(width, height)
		batch.projectionMatrix = camera.combined
	}

	override fun render(delta: Float) {
		Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
		engine.update(delta)
	}

	companion object {
		private const val mapObjects = "mapObjects"
	}
}