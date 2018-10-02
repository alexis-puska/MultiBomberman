package com.mygdx.view;

import java.util.List;

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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.constante.CollisionConstante;
import com.mygdx.constante.Constante;
import com.mygdx.domain.Player;
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
	private FrameBuffer blocsLayer;
	private FrameBuffer BricksLayer;
	private FrameBuffer playerLayer;
	private FrameBuffer frontLayer;
	private FrameBuffer shadowLayer;
	private FrameBuffer mergedLayer;
	private ShapeRenderer shapeRenderer;

	/********************
	 * --- PHYSICS ---
	 ********************/
	private World world;
	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera debugCamera;

	private List<Player> players;

	public GameScreen(final MultiBombermanGame game) {
		this.game = game;
		this.layout = new GlyphLayout();
		this.shapeRenderer = new ShapeRenderer();

		/********************
		 * --- DRAW ---
		 ********************/
		this.backgroundLayer = new FrameBuffer(Format.RGBA8888, Constante.SCREEN_SIZE_X, Constante.SCREEN_SIZE_Y,
				false);
		this.blocsLayer = new FrameBuffer(Format.RGBA8888, Constante.SCREEN_SIZE_X, Constante.SCREEN_SIZE_Y, false);
		this.BricksLayer = new FrameBuffer(Format.RGBA8888, Constante.SCREEN_SIZE_X, Constante.SCREEN_SIZE_Y, false);
		this.playerLayer = new FrameBuffer(Format.RGBA8888, Constante.SCREEN_SIZE_X, Constante.SCREEN_SIZE_Y, false);
		this.frontLayer = new FrameBuffer(Format.RGBA8888, Constante.SCREEN_SIZE_X, Constante.SCREEN_SIZE_Y, false);
		this.shadowLayer = new FrameBuffer(Format.RGBA8888, Constante.SCREEN_SIZE_X, Constante.SCREEN_SIZE_Y, false);
		this.mergedLayer = new FrameBuffer(Format.RGBA8888, Constante.SCREEN_SIZE_X, Constante.SCREEN_SIZE_Y, false);

		/********************
		 * --- PHYSICS ---
		 ********************/

		this.debugRenderer = new Box2DDebugRenderer();
		this.debugCamera = new OrthographicCamera(Constante.SCREEN_SIZE_X, Constante.SCREEN_SIZE_Y);
		float camX = 16.5f;
		float camY = 10.5f;
		this.debugCamera.position.set(camX, camY, 0);
		this.debugCamera.zoom = 0.06f;
		this.debugCamera.update();
		this.world = new World(new Vector2(0, 0), false);
		this.world.setContactListener(new CustomContactListener());
		this.players = this.game.getPlayerService().generatePlayer(this.world);
		initFont();

		for (int i = 0; i < 35; i++) {
			BodyDef groundBodyDef = new BodyDef();
			PolygonShape groundBox = new PolygonShape();
			groundBox.setAsBox(0.5f, 0.5f);
			groundBodyDef.position.set(new Vector2(i + 0.5f, 0.5f));
			Body body = world.createBody(groundBodyDef);
			Fixture fixture = body.createFixture(groundBox, 0.0f);
			fixture.setFriction(0f);
			Filter filter = new Filter();
			filter.categoryBits = CollisionConstante.CATEGORY_BLOCS;
			fixture.setFilterData(filter);
		}
		for (int i = 0; i < 35; i++) {
			BodyDef groundBodyDef = new BodyDef();
			PolygonShape groundBox = new PolygonShape();
			groundBox.setAsBox(0.5f, 0.5f);
			groundBodyDef.position.set(new Vector2(i + 0.5f, 20.5f));
			Body body = world.createBody(groundBodyDef);
			Fixture fixture = body.createFixture(groundBox, 0.0f);
			fixture.setFriction(0f);
			Filter filter = new Filter();
			filter.categoryBits = CollisionConstante.CATEGORY_BLOCS;
			fixture.setFilterData(filter);
		}
		
		for (int i = 0; i < 21; i++) {
			BodyDef groundBodyDef = new BodyDef();
			PolygonShape groundBox = new PolygonShape();
			groundBox.setAsBox(0.5f, 0.5f);
			groundBodyDef.position.set(new Vector2(0.5f, i+0.5f));
			Body body = world.createBody(groundBodyDef);
			Fixture fixture = body.createFixture(groundBox, 0.0f);
			fixture.setFriction(0f);
			Filter filter = new Filter();
			filter.categoryBits = CollisionConstante.CATEGORY_BLOCS;
			fixture.setFilterData(filter);
		}
		
		for (int i = 0; i < 21; i++) {
			BodyDef groundBodyDef = new BodyDef();
			PolygonShape groundBox = new PolygonShape();
			groundBox.setAsBox(0.5f, 0.5f);
			groundBodyDef.position.set(new Vector2(34.5f, i+0.5f));
			Body body = world.createBody(groundBodyDef);
			Fixture fixture = body.createFixture(groundBox, 0.0f);
			fixture.setFriction(0f);
			Filter filter = new Filter();
			filter.categoryBits = CollisionConstante.CATEGORY_BLOCS;
			fixture.setFilterData(filter);
		}

		for (int j = 1; j < 19; j++) {
			for (int i = 1; i < 34; i++) {
				if (i % 2 == 0 && j % 2 == 0) {
					BodyDef groundBodyDef = new BodyDef();
					PolygonShape groundBox = new PolygonShape();
					groundBox.setAsBox(0.5f, 0.5f);
					groundBodyDef.position.set(new Vector2(i + 0.5f, j + 0.5f));
					Body body = world.createBody(groundBodyDef);
					Fixture fixture = body.createFixture(groundBox, 0.0f);
					fixture.setFriction(0f);
					Filter filter = new Filter();
					filter.categoryBits = CollisionConstante.CATEGORY_BLOCS;
					fixture.setFilterData(filter);
				}
			}
		}

	}

	@Override
	public void render(float delta) {
		// clear screen
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		

		// draw background
		backgroundLayer.begin();
		game.getBatch().begin();
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		for (int x = 0; x < 35; x++) {
			for (int y = 0; y < 21; y++) {
				game.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.LEVEL1, 2), x * 18, y * 16);
			}
		}
		game.getBatch().end();
		backgroundLayer.end();

		// draw player
		playerLayer.begin();
		game.getBatch().begin();

		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		players.stream().forEach(p -> p.drawIt());
		game.getBatch().end();
		playerLayer.end();

		// draw blocs
		blocsLayer.begin();
		game.getBatch().begin();
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		for (int i = 0; i < 35; i++) {
			game.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.LEVEL1_BRICK, 0), i * 18, 0);
		}
		for (int i = 0; i < 35; i++) {
			game.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.LEVEL1_BRICK, 0), i * 18, 20 * 16);
		}
		for (int j = 0; j < 21; j++) {
			game.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.LEVEL1_BRICK, 0), 0, j * 16);
		}
		for (int j = 0; j < 21; j++) {
			game.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.LEVEL1_BRICK, 0), 34 * 18, j * 16);
		}
		for (int j = 1; j < 19; j++) {
			for (int i = 0; i < 35; i++) {
				if (i % 2 == 0 && j % 2 == 0) {
					game.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.LEVEL1_BRICK, 0), i * 18,
							j * 16);
				}
			}
		}
		game.getBatch().end();
		blocsLayer.end();
		

		// merge final layer
		mergeFinalTexture();

		
		game.getBatch().begin();
		game.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.BACKGROUND, 0), 0, 0);
		game.getBatch().draw(mergedLayer.getColorBufferTexture(), 5, 5);
		game.getBatch().end();

		// if (Constante.DEBUG) {
		// debugCamera.update();
		// debugRenderer.render(world, debugCamera.combined);
		// }

		world.step(1 / 25f, 6, 2);
	}

	private void mergeFinalTexture() {
		mergedLayer.begin();
		game.getBatch().begin();
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.getBatch().draw(backgroundLayer.getColorBufferTexture(), 0, 0);
		game.getBatch().draw(blocsLayer.getColorBufferTexture(), 0, 0);
		game.getBatch().draw(BricksLayer.getColorBufferTexture(), 0, 0);
		game.getBatch().draw(playerLayer.getColorBufferTexture(), 0, 0);
		game.getBatch().draw(frontLayer.getColorBufferTexture(), 0, 0);
		game.getBatch().draw(shadowLayer.getColorBufferTexture(), 0, 0);
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
