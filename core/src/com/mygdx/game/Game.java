package com.mygdx.game;

import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.constante.Constante;
import com.mygdx.domain.Player;
import com.mygdx.domain.enumeration.GameScreenEnum;
import com.mygdx.domain.enumeration.GameStepEnum;
import com.mygdx.domain.game.Bombe;
import com.mygdx.domain.game.BombeLight;
import com.mygdx.domain.game.Bonus;
import com.mygdx.domain.game.Brick;
import com.mygdx.domain.game.Fire;
import com.mygdx.domain.level.Hole;
import com.mygdx.domain.level.Interrupter;
import com.mygdx.domain.level.Level;
import com.mygdx.domain.level.Mine;
import com.mygdx.domain.level.Rail;
import com.mygdx.domain.level.SuddenDeathWall;
import com.mygdx.domain.level.Teleporter;
import com.mygdx.domain.level.Trolley;
import com.mygdx.domain.level.Wall;
import com.mygdx.enumeration.MusicEnum;
import com.mygdx.enumeration.SoundEnum;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.enumeration.TimeEnum;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.Context;
import com.mygdx.service.MessageService;
import com.mygdx.service.SoundService;
import com.mygdx.service.SpriteService;
import com.mygdx.service.collision.CustomContactListener;
import com.mygdx.service.mapper.LevelMapper;
import com.mygdx.service.network.dto.BrickPositionDTO;
import com.mygdx.service.network.dto.MenuDTO;
import com.mygdx.service.network.dto.PauseDTO;
import com.mygdx.service.network.dto.PlayerPixelDTO;
import com.mygdx.service.network.dto.ScoreDTO;
import com.mygdx.service.network.dto.SpriteGridDTO;
import com.mygdx.service.network.dto.SpritePixelDTO;
import com.mygdx.service.network.dto.TimeDTO;
import com.mygdx.service.network.enumeration.NetworkRequestEnum;
import com.mygdx.service.network.server.ServerContext;

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

	/********************
	 * --- GAME ---
	 *******************/
	private List<Player> players;
	private Map<Integer, Integer> score;
	private Level level;
	private float lightCountdown;

	/**********************
	 * --- SCORE / TIME ---
	 **********************/
	private float gameCountDown;
	private ShapeRenderer scoreShapeRenderer;

	/********************
	 * --- SHADER ---
	 *******************/
	private String vertexShader;
	private String fragmentShader;
	private ShaderProgram shaderProgram;
	private float deltaTime;

	/********************
	 * --- STATE ---
	 ********************/
	private GameStepEnum state;

	public Game(final MultiBombermanGame mbGame) {
		this.vertexShader = Gdx.files.internal("shader/vertex.glsl").readString();
		this.fragmentShader = Gdx.files.internal("shader/fragment.glsl").readString();
		this.shaderProgram = new ShaderProgram(vertexShader, fragmentShader);
		this.lightCountdown = Constante.LIGHT_COUNTDOWN;
		this.mbGame = mbGame;
		this.layout = new GlyphLayout();
		this.shapeRenderer = new ShapeRenderer();
		this.scoreShapeRenderer = new ShapeRenderer();
		this.state = GameStepEnum.GAME;
		this.score = new HashMap<>();
		SoundService.getInstance().playMusic(MusicEnum.BATTLE);

		/********************
		 * --- FONT ---
		 ********************/
		initFont();

		/********************
		 * --- DRAW ---
		 ********************/
		this.gameCamera = new OrthographicCamera(Constante.GAME_SCREEN_SIZE_X, Constante.GAME_SCREEN_SIZE_Y);
		this.gameCamera.position.set(Constante.GAME_SCREEN_SIZE_X / 2f, Constante.GAME_SCREEN_SIZE_Y / 2f, 0);
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
		this.backgroundLayerTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		this.blocsLayerTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		this.bricksLayerTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		this.playerLayerTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		this.frontLayerTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		this.shadowLayerTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

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
		initLevel();
	}

	private void initLevel() {
		LevelMapper levelMapper = new LevelMapper();
		this.level = levelMapper.toEntity(Context.getLevel());
		this.level.init(this, world);
		this.players = this.mbGame.getPlayerService().generatePlayer(this, this.world, this.level,
				this.level.getStartPlayer());
		this.players.stream().forEach(p -> this.score.put(p.getIndex(), 0));
		if (this.level.getShadow() > 0f) {
			switch (Context.getLocale()) {
			case ENGLISH:
				SoundService.getInstance().playSound(SoundEnum.AZIZ_LIGHT_EN);
				break;
			case FRENCH:
			default:
				SoundService.getInstance().playSound(SoundEnum.AZIZ_LIGHT_FR);
				break;
			}
		} else {
			SoundService.getInstance().playSound(SoundEnum.VALIDE);
		}
		if (Context.getTime() != TimeEnum.INF) {
			this.gameCountDown = Context.getTime().getTime() * 60f;
		} else {
			this.gameCountDown = -1;
		}
	}

	public void restart() {
		Gdx.app.log("aaa", "restart");
		LevelMapper levelMapper = new LevelMapper();
		this.players.stream().forEach(Player::dispose);
		this.level.dispose();
		this.level = levelMapper.toEntity(Context.getLevel());
		this.level.init(this, world);
		this.players = this.mbGame.getPlayerService().generatePlayer(this, this.world, this.level,
				this.level.getStartPlayer());
		if (this.level.getShadow() > 0f) {
			switch (Context.getLocale()) {
			case ENGLISH:
				SoundService.getInstance().playSound(SoundEnum.AZIZ_LIGHT_EN);
				break;
			case FRENCH:
			default:
				SoundService.getInstance().playSound(SoundEnum.AZIZ_LIGHT_FR);
				break;
			}
		}
		this.state = GameStepEnum.GAME;
		if (Context.getTime() != TimeEnum.INF) {
			this.gameCountDown = Context.getTime().getTime() * 60f;
		} else {
			this.gameCountDown = -1;
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

	public void dispose() {
		mbGame.getBatch().setShader(null);
		SoundService.getInstance().playMusic(MusicEnum.MENU);
		this.shapeRenderer.dispose();
		this.scoreShapeRenderer.dispose();
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
		this.players.stream().forEach(Player::dispose);
		this.level.dispose();
		this.world.dispose();
		this.debugRenderer.dispose();
	}

	public void render() {
		// clear screen
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.gameCamera.update();
		this.mbGame.getScreenCamera().update();
		drawBackground();
		drawWall();
		drawBricks();
		drawPlayer();
		drawFront();
		drawShadow();
		merge();
		drawScore();
		this.level.cleanUp();
		if (Constante.DEBUG) {
			debugCamera.update();
			debugRenderer.render(world, debugCamera.combined);
		}
		if (lightCountdown >= 0f) {
			lightCountdown -= Gdx.graphics.getDeltaTime();
		}
		if (this.state == GameStepEnum.DRAW_GAME) {
			drawDrawGameOVerlay();
		} else if (this.state == GameStepEnum.SCORE) {
			drawScoreOverlay();
		}
	}

	public void step() {
		this.level.update();
		this.players.stream().forEach(Player::update);

		if (this.state == GameStepEnum.GAME) {
			if (Context.getTime() != TimeEnum.INF) {
				this.gameCountDown -= Gdx.graphics.getDeltaTime();
				if (this.gameCountDown <= 0.0f) {
					this.gameCountDown = 0.0f;
				}
			}
			checkEndOfGame();
			if (isSuddentDeathTime() && Context.isSuddenDeath()) {
				players.stream().filter(Player::isBadBomber).forEach(Player::endBadBomberTime);
				this.level.doSuddenDeathThings(gameCountDown);
			}
			world.step(1 / (float) Constante.FPS, 6, 2);
		}
	}

	private void checkEndOfGame() {
		List<Player> alivePlayers = this.players.stream().filter(p -> !p.isDead() && !p.isBadBomber())
				.collect(Collectors.toList());
		if (alivePlayers.isEmpty()) {
			this.state = GameStepEnum.DRAW_GAME;
			SoundService.getInstance().playSound(SoundEnum.DRAW);
		} else if (alivePlayers.size() == 1) {
			alivePlayers.stream().forEach(p -> {
				if (p.isBurning()) {
					this.state = GameStepEnum.DRAW_GAME;
					SoundService.getInstance().playSound(SoundEnum.DRAW);
				} else {
					this.score.put(p.getIndex(), this.score.get(p.getIndex()) + 1);
					p.winTheGame();
					this.state = GameStepEnum.SCORE;
					SoundService.getInstance().playSound(SoundEnum.END);
				}
			});
		} else if (alivePlayers.size() > 1 && (this.gameCountDown <= 0.0f && Context.getTime() != TimeEnum.INF)) {
			alivePlayers.stream().filter(p -> !p.isDead() || p.isBadBomber()).forEach(p -> {
				this.score.put(p.getIndex(), this.score.get(p.getIndex()) + 1);
				p.winTheGame();
			});
			this.state = GameStepEnum.SCORE;
			SoundService.getInstance().playSound(SoundEnum.END);
		}
	}

	public MultiBombermanGame getMultiBombermanGame() {
		return this.mbGame;
	}

	public boolean isScore() {
		return this.state == GameStepEnum.SCORE;
	}

	public boolean isDraw() {
		return this.state == GameStepEnum.DRAW_GAME;
	}

	public boolean isSuddentDeathTime() {
		return gameCountDown <= 50.0f;
	}

	public List<Player> getPlayers() {
		return this.players;
	}

	/***********************************************************************
	 * --- DRAW PART ---
	 ***********************************************************************/
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
								this.level.getDefaultBackground().getIndex()), x * Constante.GRID_PIXELS_SIZE_X,
								y * Constante.GRID_PIXELS_SIZE_Y);
			}
		}
		this.level.getCustomBackgroundTexture().stream()
				.forEach(cbt -> mbGame.getBatch().draw(
						SpriteService.getInstance().getSprite(cbt.getAnimation(), cbt.getIndex()),
						cbt.getX() * Constante.GRID_PIXELS_SIZE_X, cbt.getY() * Constante.GRID_PIXELS_SIZE_Y));
		this.level.getRail().stream().forEach(Rail::drawIt);
		this.level.getInterrupter().stream().forEach(Interrupter::drawIt);
		this.level.getTeleporter().stream().forEach(Teleporter::drawIt);
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
		level.getSuddenDeathWall().stream().filter(SuddenDeathWall::hasBody).forEach(SuddenDeathWall::drawIt);
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
						(cft.getX() * Constante.GRID_PIXELS_SIZE_X) - Constante.GRID_PIXELS_SIZE_X,
						(cft.getY() * Constante.GRID_PIXELS_SIZE_Y) - Constante.GRID_PIXELS_SIZE_Y));

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
		if (lightCountdown <= 0) {
			Collections.sort(players);
			players.stream().forEach(p -> {
				if (!p.isDead()) {
					if (p.isBadBomber()) {
						shapeRenderer.circle(p.getShipPixelX(), p.getShipPixelY(), 24);
					} else {
						shapeRenderer.circle(p.getPixelX(), p.getPixelY(), 24);
					}
				}
			});
			level.getFires().stream().filter(f -> !f.isOff())
					.forEach(f -> shapeRenderer.circle(f.getPixelX(), f.getPixelY(), 24));
			level.getBonuss().stream().filter(Bonus::isBurning)
					.forEach(f -> shapeRenderer.circle(f.getPixelX(), f.getPixelY(), 24));
			level.getBombes().stream().filter(f -> !f.isExploded() && f.isCreateLight()).forEach(b -> {
				shapeRenderer.setColor(new Color(0f, 0f, 0f, 0f));
				BombeLight light = b.getOffesetShadow();
				shapeRenderer.circle((float) (b.getPixelX() + light.getX()),
						b.getPixelY() + b.getReboundOffset() + (float) light.getY(), (float) light.getRadius());
			});
		}
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
		level.getFires().stream().filter(f -> !f.isOff()).forEach(Fire::drawIt);
		level.getMine().forEach(Mine::drawIt);
		level.getTrolley().forEach(Trolley::drawIt);
		level.getBombes().stream().filter(b -> !b.isExploded()).forEach(Bombe::drawIt);
		level.getBonuss().stream().filter(b -> b.isRevealed() || b.isBurning()).forEach(Bonus::drawIt);
		players.stream().forEach(Player::drawIt);
		level.getSuddenDeathWall().stream().filter(sdw -> !sdw.hasBody()).forEach(SuddenDeathWall::drawIt);
		mbGame.getBatch().end();
		playerLayerTextureRegion = new TextureRegion(playerLayerTexture);
		playerLayerTextureRegion.flip(false, true);
		playerLayer.end();
	}

	private void merge() {
		mbGame.getBatch().begin();
		mbGame.getBatch().setProjectionMatrix(mbGame.getScreenCamera().combined);
		mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.BACKGROUND, 0), 0, 0);
		if (this.level.isLevelUnderWater()) {
			mbGame.getBatch().setShader(shaderProgram);
			float delta = Gdx.graphics.getDeltaTime();
			deltaTime += delta;
			shaderProgram.setUniformf("time", deltaTime % 40f);
		}
		mbGame.getBatch().draw(backgroundLayerTextureRegion, 5, 0, Constante.GAME_SCREEN_SIZE_X,
				Constante.GAME_SCREEN_SIZE_Y);
		mbGame.getBatch().draw(blocsLayerTextureRegion, 5, 0, Constante.GAME_SCREEN_SIZE_X,
				Constante.GAME_SCREEN_SIZE_Y);
		mbGame.getBatch().draw(bricksLayerTextureRegion, 5, 0, Constante.GAME_SCREEN_SIZE_X,
				Constante.GAME_SCREEN_SIZE_Y);
		mbGame.getBatch().draw(playerLayerTextureRegion, 5, 0, Constante.GAME_SCREEN_SIZE_X,
				Constante.GAME_SCREEN_SIZE_Y);
		mbGame.getBatch().draw(frontLayerTextureRegion, 5, 0, Constante.GAME_SCREEN_SIZE_X,
				Constante.GAME_SCREEN_SIZE_Y);
		mbGame.getBatch().draw(shadowLayerTextureRegion, 5, 0, Constante.GAME_SCREEN_SIZE_X,
				Constante.GAME_SCREEN_SIZE_Y);
		mbGame.getBatch().setShader(null);
		mbGame.getBatch().end();
	}

	private void drawScore() {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.setProjectionMatrix(mbGame.getBatch().getProjectionMatrix());
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0, 0, 0, 0.6f);
		Map<Integer, Player> pls = new HashMap<>();
		players.stream().forEach(p -> pls.put(p.getIndex(), p));

		for (int i = 0; i < 8; i++) {
			shapeRenderer.rect(3f + ((float) i * 36f), 339f, 33f, 18f);
		}
		for (int i = 0; i < 8; i++) {
			shapeRenderer.rect(352f + ((float) i * 36f), 339f, 33f, 18f);
		}
		shapeRenderer.rect(291, 339, 58, 18);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		mbGame.getBatch().begin();
		pls.entrySet().stream().forEach(e -> {
			float x = ((float) e.getKey() * 36f) + 3f;
			if (e.getKey() >= 8) {
				x += 58f;
			}
			if (e.getValue().isDead()) {
				mbGame.getBatch().draw(e.getValue().getMiniatureCry(), x, 340f, 18f, 16f);
			} else {
				mbGame.getBatch().draw(e.getValue().getMiniatureHappy(), x, 340f, 18f, 16f);
			}
			layout.setText(font, Integer.toString(this.score.get(e.getKey())));
			font.draw(mbGame.getBatch(), layout, x + 19f, 352);
		});
		layout.setText(font, "XXX");
		for (int i = 0; i < 8; i++) {
			if (!pls.containsKey(i)) {
				font.draw(mbGame.getBatch(), layout, 8f + ((float) i * 36f), 352f);
			}
		}
		for (int i = 0; i < 8; i++) {
			if (!pls.containsKey(i + 8)) {
				font.draw(mbGame.getBatch(), layout, 360f + ((float) i * 36f), 352f);
			}
		}
		if (Context.getTime() != TimeEnum.INF) {
			DecimalFormat df = new DecimalFormat("#.#");
			layout.setText(font, "" + df.format(this.gameCountDown));
		} else {
			layout.setText(font, "inf");
		}
		font.draw(mbGame.getBatch(), layout, (Constante.SCREEN_SIZE_X / 2.0f) - (layout.width / 2.0f), 352);
		mbGame.getBatch().end();
	}

	public void drawPlayerNumber() {
		this.players.stream().forEach(p -> {
			layout.setText(font, Integer.toString(p.getIndex()));
			if (p.isBadBomber()) {
				font.draw(mbGame.getBatch(), layout, p.getShipPixelX(), p.getShipPixelY());
			} else if (!p.isDead()) {
				font.draw(mbGame.getBatch(), layout, p.getPixelX(), p.getPixelY());
			}
		});
	}

	private void drawScoreOverlay() {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.setProjectionMatrix(mbGame.getBatch().getProjectionMatrix());
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0, 0, 0, 0.5f);
		shapeRenderer.rect(0, 0, 640, 360);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		mbGame.getBatch().begin();
		layout.setText(font, MessageService.getInstance().getMessage("game.game.end"));
		font.draw(mbGame.getBatch(), layout, (Constante.SCREEN_SIZE_X / 2f) - (layout.width / 2f), 210);
		mbGame.getBatch().end();
	}

	private void drawDrawGameOVerlay() {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.setProjectionMatrix(mbGame.getBatch().getProjectionMatrix());
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0, 0, 0, 0.5f);
		shapeRenderer.rect(0, 0, 640, 360);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		mbGame.getBatch().begin();
		layout.setText(font, MessageService.getInstance().getMessage("game.game.draw"));
		font.draw(mbGame.getBatch(), layout, (Constante.SCREEN_SIZE_X / 2f) - (layout.width / 2f), 210);
		mbGame.getBatch().end();
	}

	/*******************************************************************
	 * --- NETWORK PART ---
	 *******************************************************************/
	public void doNetworkStuff(GameScreenEnum state) {
		ByteBuffer bb = ByteBuffer.allocate(512000);

		// RAIL
		this.level.getRail().stream().forEach(r -> {
			SpriteGridDTO sg = new SpriteGridDTO(false, r.getDrawSprite(), r.getDrawIndex(), r.getGridIndex());
			bb.put(sg.getBuffer());
		});
		// INTERRUPTER
		this.level.getInterrupter().forEach(i -> {
			SpriteGridDTO sg = new SpriteGridDTO(false, i.getDrawSprite(), i.getDrawIndex(), i.getGridIndex());
			bb.put(sg.getBuffer());
		});
		// TELEPORTER
		this.level.getTeleporter().forEach(t -> {
			SpriteGridDTO sg = new SpriteGridDTO(false, t.getDrawSprite(), t.getDrawIndex(), t.getGridIndex());
			bb.put(sg.getBuffer());
		});
		// HOLE
		this.level.getHole().forEach(h -> {
			SpriteGridDTO sg = new SpriteGridDTO(false, h.getDrawSprite(), h.getDrawIndex(), h.getGridIndex());
			bb.put(sg.getBuffer());
		});

		// ENCODE BRICK DISPLAY
		bb.put(new BrickPositionDTO(this.level).getBuffer());
		// ENCODE BRICK BURN
		this.level.getBricks().stream().filter(Brick::isBurningOrBurned).forEach(b -> {
			SpriteGridDTO sg = new SpriteGridDTO(false, b.getDrawSprite(), b.getDrawIndex(), b.getGridIndex());
			bb.put(sg.getBuffer());
		});

		// SUDDENDEATHWALL BACK
		this.level.getSuddenDeathWall().stream().filter(s -> s.isTransformedInStrandardWall()).forEach(h -> {
			SpriteGridDTO sg = new SpriteGridDTO(false, h.getDrawSprite(), h.getDrawIndex(), h.getGridIndex());
			bb.put(sg.getBuffer());
		});

		// FIRE
		this.level.getFires().forEach(h -> {
			SpriteGridDTO sg = new SpriteGridDTO(false, h.getDrawSprite(), h.getDrawIndex(), h.getGridIndex());
			bb.put(sg.getBuffer());
		});
		// MINE
		this.level.getMine().forEach(h -> {
			SpriteGridDTO sg = new SpriteGridDTO(false, h.getDrawSprite(), h.getDrawIndex(), h.getGridIndex());
			bb.put(sg.getBuffer());
		});
		// TROLLEY
		this.level.getTrolley().stream().forEach(h -> {
			SpritePixelDTO sg = new SpritePixelDTO(false, h.getDrawSprite(), h.getDrawIndex(), h.getBodyX(),
					h.getBodyY());
			bb.put(sg.getBuffer());
		});

		// BOMBE
		this.level.getBombes().stream().filter(b -> !b.isExploded()).forEach(h -> {
			SpritePixelDTO sg = new SpritePixelDTO(false, h.getDrawSprite(), h.getDrawIndex(),
					(float) ((h.getBodyX() - 0.5f) * Constante.GRID_PIXELS_SIZE_X),
					(float) ((h.getBodyY() - 0.5f) * Constante.GRID_PIXELS_SIZE_Y) + h.getReboundOffset());
			bb.put(sg.getBuffer());
		});

		// BONUS
		this.level.getBonuss().forEach(h -> {
			SpriteGridDTO sg = new SpriteGridDTO(false, h.getDrawSprite(), h.getDrawIndex(), h.getGridIndex());
			bb.put(sg.getBuffer());
		});
		// PLAYER
		players.stream().forEach(p -> {
			PlayerPixelDTO ppd = new PlayerPixelDTO(p);
			bb.put(ppd.getBuffer());
		});
		// SUDDENDEATHWALL FRONT
		this.level.getSuddenDeathWall().stream().filter(s -> !s.isTransformedInStrandardWall()).forEach(h -> {
			SpritePixelDTO sg = new SpritePixelDTO(true, h.getDrawSprite(), h.getDrawIndex(),
					h.getX() * Constante.GRID_PIXELS_SIZE_X, h.getY() * Constante.GRID_PIXELS_SIZE_Y + h.getOffset());
			bb.put(sg.getBuffer());
		});

		// ENCODE STATE
		switch (state) {
		case MENU:
			bb.put(new MenuDTO().getBuffer());
			break;
		case PAUSE:
			bb.put(new PauseDTO().getBuffer());
			break;
		case GAME:
		default:
			break;
		}

		// ENCODE SCORE
		if (this.state == GameStepEnum.SCORE) {
			this.score.entrySet().stream().forEach(k -> bb.put(new ScoreDTO(k.getKey(), k.getValue()).getBuffer()));
		}

		// ENCODE TIME
		bb.put(new TimeDTO(this.gameCountDown).getBuffer());

		// ENCODE STRING
		byte[] e = new byte[bb.position()];
		int lengthToCopy = bb.position();
		bb.position(0);
		bb.get(e, 0, lengthToCopy);
		String encoded = Base64.getEncoder().encodeToString(e);
		Gdx.app.debug("GAME", encoded);
		String cmd = NetworkRequestEnum.GAME_SCREEN.name() + ":" + encoded;
		ServerContext.setGameScreenBuffer(cmd);
		mbGame.getNetworkService().sendToClient(cmd);
	}
}
