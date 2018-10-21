package com.mygdx.game;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
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
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.SpriteService;
import com.mygdx.service.collision.CustomContactListener;

public class Game {

	private final MultiBombermanGame mbGame;

	/********************
	 * --- TEXT ---
	 ********************/
	private GlyphLayout layout;
	private BitmapFont font;

	/********************
	 * --- DRAW ---
	 ********************/
	// layout
	private FrameBuffer backgroundLayer;
	private FrameBuffer blocsLayer;
	private FrameBuffer bricksLayer;
	private FrameBuffer playerLayer;
	private FrameBuffer frontLayer;
	private FrameBuffer shadowLayer;

	private Texture backgroundLayerTexture;
	private Texture blocsLayerTexture;
	private Texture bricksLayerTexture;
	private Texture playerLayerTexture;
	private Texture frontLayerTexture;
	private Texture shadowLayerTexture;

	private TextureRegion backgroundLayerTextureRegion;
	private TextureRegion blocsLayerTextureRegion;
	private TextureRegion bricksLayerTextureRegion;
	private TextureRegion playerLayerTextureRegion;
	private TextureRegion frontLayerTextureRegion;
	private TextureRegion shadowLayerTextureRegion;

	private OrthographicCamera gameCamera;
	private ShapeRenderer shapeRenderer;

	/********************
	 * --- PHYSICS ---
	 ********************/
	private World world;
	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera debugCamera;

	private List<Player> players;

	public Game(final MultiBombermanGame mbGame) {
		this.mbGame = mbGame;
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
		this.blocsLayer = new FrameBuffer(Format.RGBA8888, Constante.GAME_SCREEN_SIZE_X, Constante.GAME_SCREEN_SIZE_Y,
				false);
		this.bricksLayer = new FrameBuffer(Format.RGBA8888, Constante.GAME_SCREEN_SIZE_X, Constante.GAME_SCREEN_SIZE_Y,
				false);
		this.playerLayer = new FrameBuffer(Format.RGBA8888, Constante.GAME_SCREEN_SIZE_X, Constante.GAME_SCREEN_SIZE_Y,
				false);
		this.frontLayer = new FrameBuffer(Format.RGBA8888, Constante.GAME_SCREEN_SIZE_X, Constante.GAME_SCREEN_SIZE_Y,
				false);
		this.shadowLayer = new FrameBuffer(Format.RGBA8888, Constante.GAME_SCREEN_SIZE_X, Constante.GAME_SCREEN_SIZE_Y,
				false);

		this.backgroundLayerTexture = backgroundLayer.getColorBufferTexture();
		this.blocsLayerTexture = blocsLayer.getColorBufferTexture();
		this.bricksLayerTexture = bricksLayer.getColorBufferTexture();
		this.playerLayerTexture = playerLayer.getColorBufferTexture();
		this.frontLayerTexture = frontLayer.getColorBufferTexture();
		this.shadowLayerTexture = shadowLayer.getColorBufferTexture();

		backgroundLayerTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		blocsLayerTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		bricksLayerTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		playerLayerTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		frontLayerTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		shadowLayerTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

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
		this.players = this.mbGame.getPlayerService().generatePlayer(this.world);
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
			groundBodyDef.position.set(new Vector2(0.5f, i + 0.5f));
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
			groundBodyDef.position.set(new Vector2(34.5f, i + 0.5f));
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

	public void render(float delta) {
		// clear screen
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.gameCamera.update();
		mbGame.getScreenCamera().update();
		drawBackground();
		drawBlocs();
		drawBricks();
		drawPlayer();
		drawFront();
		drawShadow();
		mbGame.getBatch().begin();
		mbGame.getBatch().setProjectionMatrix(mbGame.getScreenCamera().combined);
		mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.BACKGROUND, 0), 0, 0);
		mbGame.getBatch().draw(backgroundLayerTextureRegion, 5, 5, Constante.GAME_SCREEN_SIZE_X,
				Constante.GAME_SCREEN_SIZE_Y);
		mbGame.getBatch().draw(blocsLayerTextureRegion, 5, 5, Constante.GAME_SCREEN_SIZE_X,
				Constante.GAME_SCREEN_SIZE_Y);
		mbGame.getBatch().draw(bricksLayerTextureRegion, 5, 5, Constante.GAME_SCREEN_SIZE_X,
				Constante.GAME_SCREEN_SIZE_Y);
		mbGame.getBatch().draw(playerLayerTextureRegion, 5, 5, Constante.GAME_SCREEN_SIZE_X,
				Constante.GAME_SCREEN_SIZE_Y);
		mbGame.getBatch().draw(frontLayerTextureRegion, 5, 5, Constante.GAME_SCREEN_SIZE_X,
				Constante.GAME_SCREEN_SIZE_Y);
		mbGame.getBatch().draw(shadowLayerTextureRegion, 5, 5, Constante.GAME_SCREEN_SIZE_X,
				Constante.GAME_SCREEN_SIZE_Y);
		mbGame.getBatch().end();
		if (Constante.DEBUG) {
			debugCamera.update();
			debugRenderer.render(world, debugCamera.combined);
		}
	}

	public void step() {
		world.step(1 / 25f, 6, 2);
	}

	private void drawBackground() {
		backgroundLayer.begin();
		mbGame.getBatch().begin();
		mbGame.getBatch().setProjectionMatrix(gameCamera.combined);
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		for (int x = 0; x < 35; x++) {
			for (int y = 0; y < 21; y++) {
				mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.LEVEL, 2), x * 18, y * 16);
			}
		}
		mbGame.getBatch().end();
		backgroundLayerTextureRegion = new TextureRegion(backgroundLayerTexture);
		backgroundLayerTextureRegion.flip(false, true);
		backgroundLayer.end();
	}

	private void drawBlocs() {
		blocsLayer.begin();
		mbGame.getBatch().begin();
		mbGame.getBatch().setProjectionMatrix(gameCamera.combined);
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		for (int i = 0; i < 35; i++) {
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.LEVEL1, 0), i * 18, 0);
		}
		for (int i = 0; i < 35; i++) {
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.LEVEL1, 0), i * 18, 20 * 16);
		}
		for (int j = 0; j < 21; j++) {
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.LEVEL1, 0), 0, j * 16);
		}
		for (int j = 0; j < 21; j++) {
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.LEVEL1, 0), 34 * 18, j * 16);
		}
		for (int j = 1; j < 19; j++) {
			for (int i = 0; i < 35; i++) {
				if (i % 2 == 0 && j % 2 == 0) {
					mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.LEVEL1, 0), i * 18, j * 16);
				}
			}
		}
		mbGame.getBatch().end();
		blocsLayerTextureRegion = new TextureRegion(blocsLayerTexture);
		blocsLayerTextureRegion.flip(false, true);
		blocsLayer.end();
	}

	private void drawBricks() {
		bricksLayer.begin();
		mbGame.getBatch().begin();
		mbGame.getBatch().setProjectionMatrix(gameCamera.combined);
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		mbGame.getBatch().end();
		bricksLayerTextureRegion = new TextureRegion(bricksLayerTexture);
		bricksLayerTextureRegion.flip(false, true);
		bricksLayer.end();
	}

	private void drawFront() {
		frontLayer.begin();
		mbGame.getBatch().begin();
		mbGame.getBatch().setProjectionMatrix(gameCamera.combined);
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		mbGame.getBatch().end();
		frontLayerTextureRegion = new TextureRegion(frontLayerTexture);
		frontLayerTextureRegion.flip(false, true);
		frontLayer.end();
	}

	private void drawShadow() {
		shadowLayer.begin();
		mbGame.getBatch().begin();
		mbGame.getBatch().setProjectionMatrix(gameCamera.combined);
		Gdx.gl.glClearColor(0f, 0f, 0f, 0.5f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Gdx.gl.glColorMask(false, false, false, true);
		shapeRenderer.setProjectionMatrix(gameCamera.combined);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(new Color(0f, 0f, 0f, 0f));

		players.stream().forEach(p -> {
			shapeRenderer.circle(p.getX(), p.getY(), 24);
		});

		shapeRenderer.end();
		Gdx.gl.glColorMask(true, true, true, true);

		mbGame.getBatch().end();
		shadowLayerTextureRegion = new TextureRegion(shadowLayerTexture);
		shadowLayerTextureRegion.flip(false, true);
		shadowLayer.end();
	}

	private void drawPlayer() {
		playerLayer.begin();
		mbGame.getBatch().begin();
		mbGame.getBatch().setProjectionMatrix(gameCamera.combined);
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		players.stream().forEach(Player::drawIt);
		mbGame.getBatch().end();
		playerLayerTextureRegion = new TextureRegion(playerLayerTexture);
		playerLayerTextureRegion.flip(false, true);
		playerLayer.end();
	}

	public void dispose() {
		this.shapeRenderer.dispose();
		this.font.dispose();
		this.backgroundLayer.dispose();
		this.blocsLayer.dispose();
		this.bricksLayer.dispose();
		this.playerLayer.dispose();
		this.frontLayer.dispose();
		this.shadowLayer.dispose();
		this.backgroundLayerTexture.dispose();
		this.blocsLayerTexture.dispose();
		this.bricksLayerTexture.dispose();
		this.playerLayerTexture.dispose();
		this.frontLayerTexture.dispose();
		this.shadowLayerTexture.dispose();
	}
}
