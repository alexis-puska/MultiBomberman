package com.mygdx.game;

import java.util.Collections;
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
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.constante.Constante;
import com.mygdx.domain.Player;
import com.mygdx.domain.level.Brick;
import com.mygdx.domain.level.Hole;
import com.mygdx.domain.level.Interrupter;
import com.mygdx.domain.level.Level;
import com.mygdx.domain.level.Rail;
import com.mygdx.domain.level.Wall;
import com.mygdx.enumeration.MusicEnum;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.Context;
import com.mygdx.service.SoundService;
import com.mygdx.service.SpriteService;
import com.mygdx.service.collision.CustomContactListener;
import com.mygdx.service.mapper.LevelMapper;

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

	private Level level;

	public Game(final MultiBombermanGame mbGame) {
		this.mbGame = mbGame;
		this.layout = new GlyphLayout();
		this.shapeRenderer = new ShapeRenderer();
		SoundService.getInstance().playMusic(MusicEnum.BATTLE);

		/********************
		 * --- FONT ---
		 ********************/
		initFont();

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

		/********************
		 * --- INIT LEVEL ---
		 ********************/
		LevelMapper levelMapper = new LevelMapper();
		this.level = levelMapper.toEntity(Context.getLevel());
		this.level.init(this, world);
		this.players = this.mbGame.getPlayerService().generatePlayer(this.world, this.level.getStartPlayer());

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

	public void dispose() {
		SoundService.getInstance().playMusic(MusicEnum.MENU);
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
		this.world.dispose();
		this.debugRenderer.dispose();
	}

	/**
	 * Render function
	 * 
	 * @param delta
	 */
	public void render(float delta) {
		// clear screen
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.gameCamera.update();
		this.mbGame.getScreenCamera().update();
		this.level.update();
		drawBackground();
		drawWall();
		drawBricks();
		drawPlayer();
		drawFront();
		drawShadow();
		merge();
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
		for (int x = 0; x < Constante.GRID_SIZE_X; x++) {
			for (int y = 0; y < Constante.GRID_SIZE_Y; y++) {
				mbGame.getBatch()
						.draw(SpriteService.getInstance().getSprite(this.level.getDefaultBackground().getAnimation(),
								this.level.getDefaultBackground().getIndex()), x * 18, y * 16);
			}
		}
		this.level.getCustomBackgroundTexture().stream()
				.forEach(cbt -> mbGame.getBatch().draw(
						SpriteService.getInstance().getSprite(cbt.getAnimation(), cbt.getIndex()), cbt.getX() * 18,
						cbt.getY() * 16));
		this.level.getRail().stream().forEach(Rail::drawIt);
		this.level.getInterrupter().stream().forEach(Interrupter::drawIt);
		this.level.getHole().stream().forEach(Hole::drawIt);
		mbGame.getBatch().end();
		backgroundLayer.end();
		backgroundLayerTextureRegion = new TextureRegion(backgroundLayerTexture);
		backgroundLayerTextureRegion.flip(false, true);

	}

	private void drawWall() {
		blocsLayer.begin();
		mbGame.getBatch().begin();
		mbGame.getBatch().setProjectionMatrix(gameCamera.combined);
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.level.getWall().stream().forEach(Wall::drawIt);
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
		this.level.getBricks().stream().forEach(Brick::drawIt);
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

		this.level.getCustomForegroundTexture().stream()
				.forEach(cft -> mbGame.getBatch().draw(
						SpriteService.getInstance().getSprite(cft.getAnimation(), cft.getIndex()),
						(cft.getX() * 18) - 18, (cft.getY() * 16) - 16));

		mbGame.getBatch().end();
		frontLayerTextureRegion = new TextureRegion(frontLayerTexture);
		frontLayerTextureRegion.flip(false, true);
		frontLayer.end();
	}

	private void drawShadow() {
		shadowLayer.begin();
		mbGame.getBatch().begin();
		mbGame.getBatch().setProjectionMatrix(gameCamera.combined);
		Gdx.gl.glClearColor(0f, 0f, 0f, this.level.getShadow());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Gdx.gl.glColorMask(false, false, false, true);
		shapeRenderer.setProjectionMatrix(gameCamera.combined);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(new Color(0f, 0f, 0f, 0f));

		Collections.sort(players);
		players.stream().forEach(p -> shapeRenderer.circle(p.getX(), p.getY(), 24));

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

	private void merge() {
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
	}

	public MultiBombermanGame getMultiBombermanGame() {
		return this.mbGame;
	}
}
