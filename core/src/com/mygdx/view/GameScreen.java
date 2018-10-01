package com.mygdx.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.constante.Constante;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.game.MultiBombermanGame;
import com.mygdx.service.SpriteService;
import com.mygdx.service.collision.CustomContactListener;
import com.mygdx.service.input_processor.MenuListener;

public class GameScreen implements Screen, MenuListener {

	private final MultiBombermanGame game;

	/********************
	 * --- TEXT ---
	 ********************/
	private GlyphLayout layout;
	private BitmapFont font;
	private BitmapFont fontScore;

	/********************
	 * --- DRAW ---
	 ********************/
	// layout
	private FrameBuffer backgroundLayer;
	private FrameBuffer BlocsLayer;
	private FrameBuffer BricksLayer;
	private FrameBuffer playerLayer;
	private FrameBuffer frontLayer;
	private FrameBuffer shadowLayer;
	private FrameBuffer mergedLayer;
	private ShapeRenderer shapeRenderer;
	// camera
	private OrthographicCamera gameCamera;

	/********************
	 * --- PHYSICS ---
	 ********************/
	private World world;
	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera debugCamera;

	public GameScreen(final MultiBombermanGame game) {
		this.game = game;
		this.layout = new GlyphLayout();
		this.shapeRenderer = new ShapeRenderer();

		/********************
		 * --- DRAW ---
		 ********************/
		this.gameCamera = new OrthographicCamera(Constante.GAME_SCREEN_SIZE_X, Constante.GAME_SCREEN_SIZE_Y);
		this.gameCamera.position.set(Constante.GAME_SCREEN_SIZE_X / 2, Constante.GAME_SCREEN_SIZE_Y / 2, 0);
		this.gameCamera.update();

		this.backgroundLayer = new FrameBuffer(Format.RGBA8888, Constante.GAME_SCREEN_SIZE_X,
				Constante.GAME_SCREEN_SIZE_Y, false);
		this.BlocsLayer = new FrameBuffer(Format.RGBA8888, Constante.GAME_SCREEN_SIZE_X, Constante.GAME_SCREEN_SIZE_Y,
				false);
		this.BricksLayer = new FrameBuffer(Format.RGBA8888, Constante.GAME_SCREEN_SIZE_X, Constante.GAME_SCREEN_SIZE_Y,
				false);
		this.playerLayer = new FrameBuffer(Format.RGBA8888, Constante.GAME_SCREEN_SIZE_X, Constante.GAME_SCREEN_SIZE_Y,
				false);
		this.frontLayer = new FrameBuffer(Format.RGBA8888, Constante.GAME_SCREEN_SIZE_X, Constante.GAME_SCREEN_SIZE_Y,
				false);
		this.shadowLayer = new FrameBuffer(Format.RGBA8888, Constante.GAME_SCREEN_SIZE_X, Constante.GAME_SCREEN_SIZE_Y,
				false);
		this.mergedLayer = new FrameBuffer(Format.RGBA8888, Constante.GAME_SCREEN_SIZE_X, Constante.GAME_SCREEN_SIZE_Y,
				false);

		/********************
		 * --- PHYSICS ---
		 ********************/
		this.debugRenderer = new Box2DDebugRenderer();
		this.debugCamera = new OrthographicCamera(35f, 21f);
		this.debugCamera.setToOrtho(false, 35f, 21f);
		float camX = 35f;
		float camY = 21f;
		this.debugCamera.position.set(camX / 2, camY / 2, 0);
		this.debugCamera.zoom = 1.01587302f;
		this.debugCamera.update();
		this.world = new World(new Vector2(0, 0), false);
		this.world.setContactListener(new CustomContactListener());
		this.game.getPlayerService().generatePlayer(this.world);
		initFont();
	}

	@Override
	public void render(float delta) {
		// clear screen
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// update game camera
		gameCamera.update();

		// draw in layer
		game.getBatch().setProjectionMatrix(gameCamera.combined);
		backgroundLayer.begin();
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		shapeRenderer.setProjectionMatrix(gameCamera.combined);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0, 0, 0, 1f);
		shapeRenderer.rect(0, 0, 630, 336);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.line(0, 0, 630, 336);
		shapeRenderer.end();
		game.getBatch().begin();
		for (int x = 0; x < 35; x++) {
			for (int y = 0; y < 21; y++) {
				game.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.LEVEL1, 2), x * 18, y * 16);
			}
		}
		game.getBatch().end();
		backgroundLayer.end();

		frontLayer.begin();
		shapeRenderer.setProjectionMatrix(gameCamera.combined);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.line(630, 0, 0, 336);
		shapeRenderer.end();
		frontLayer.end();

		// merge final layer
		mergeFinalTexture();

		// update viewport
		game.getViewport().apply(true);
		// update screen camera
		game.getScreenCamera().update();
		game.getBatch().setProjectionMatrix(game.getScreenCamera().combined);
		// draw image in screen
		game.getBatch().begin();
		game.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.BACKGROUND, 0), 0, 0);
		game.getBatch().draw(mergedLayer.getColorBufferTexture(), 5, 5);
		game.getBatch().end();

		world.step(1 / 25f, 6, 2);

		if (Constante.DEBUG) {
			debugCamera.update();
			debugRenderer.render(world, debugCamera.combined);
		}
	}

	private void mergeFinalTexture() {
		mergedLayer.begin();
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.getBatch().begin();
		game.getBatch().draw(backgroundLayer.getColorBufferTexture(), 0, 0, Constante.GAME_SCREEN_SIZE_X,
				Constante.GAME_SCREEN_SIZE_Y);
		game.getBatch().draw(BlocsLayer.getColorBufferTexture(), 0, 0, Constante.GAME_SCREEN_SIZE_X,
				Constante.GAME_SCREEN_SIZE_Y);
		game.getBatch().draw(BricksLayer.getColorBufferTexture(), 0, 0, Constante.GAME_SCREEN_SIZE_X,
				Constante.GAME_SCREEN_SIZE_Y);
		game.getBatch().draw(playerLayer.getColorBufferTexture(), 0, 0, Constante.GAME_SCREEN_SIZE_X,
				Constante.GAME_SCREEN_SIZE_Y);
		game.getBatch().draw(frontLayer.getColorBufferTexture(), 0, 0, Constante.GAME_SCREEN_SIZE_X,
				Constante.GAME_SCREEN_SIZE_Y);
		game.getBatch().draw(shadowLayer.getColorBufferTexture(), 0, 0, Constante.GAME_SCREEN_SIZE_X,
				Constante.GAME_SCREEN_SIZE_Y);
		game.getBatch().end();
		mergedLayer.end();
	}

	@Override
	public void show() {
		// unused method
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {
		// unused method
	}

	@Override
	public void resume() {
		// unused method
	}

	@Override
	public void hide() {
		// unused method
	}

	@Override
	public void dispose() {
		shapeRenderer.dispose();
		font.dispose();
	}

	/**************************************************
	 * --- Player 1 control screen --- can pause / resume / exit game
	 **************************************************/
	@Override
	public void pressStart() {// unused method
	}

	@Override
	public void pressSelect() {
		game.getScreen().dispose();
		game.setScreen(new LevelScreen(game));
	}

	@Override
	public void pressA() { // unused method
	}

	@Override
	public void pressUp() { // unused method
	}

	@Override
	public void pressDown() {// unused method
	}

	@Override
	public void pressLeft() {// unused method
	}

	@Override
	public void pressRight() {// unused method
	}

	@Override
	public void pressB() {// unused method
	}

	@Override
	public void pressX() {// unused method
	}

	@Override
	public void pressY() {// unused method
	}

	@Override
	public void pressL() {// unused method
	}

	@Override
	public void pressR() {// unused method
	}

	/******************************
	 * --- Init font draw text ---
	 ******************************/
	public void initFont() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/font_gbboot.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 14;
		parameter.borderWidth = 0f;
		parameter.borderColor = new Color(255, 0, 0, 255);
		parameter.color = new Color(255, 0, 0, 255);
		font = generator.generateFont(parameter);
		generator.dispose();
	}
}
