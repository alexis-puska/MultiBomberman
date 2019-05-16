package com.mygdx.view.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygdx.constante.Constante;
import com.mygdx.domain.PlayerDefinition;
import com.mygdx.domain.enumeration.BonusTypeEnum;
import com.mygdx.domain.game.BombeLight;
import com.mygdx.dto.level.LevelDTO;
import com.mygdx.enumeration.CharacterColorEnum;
import com.mygdx.enumeration.CharacterEnum;
import com.mygdx.enumeration.CharacterSpriteEnum;
import com.mygdx.enumeration.PlayerTypeEnum;
import com.mygdx.enumeration.SoundEnum;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.enumeration.TimeEnum;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.Context;
import com.mygdx.service.MessageService;
import com.mygdx.service.SoundService;
import com.mygdx.service.SpriteService;
import com.mygdx.service.input_processor.MenuListener;
import com.mygdx.service.network.dto.delta.BonusRevealedDTO;
import com.mygdx.service.network.dto.delta.FireAppareadDTO;
import com.mygdx.service.network.dto.delta.GenericDeltaDTO;
import com.mygdx.service.network.dto.draw.BombePixelDTO;
import com.mygdx.service.network.dto.draw.PlayerPixelDTO;
import com.mygdx.service.network.dto.draw.SpriteGridDTO;
import com.mygdx.service.network.dto.draw.SpritePixelDTO;
import com.mygdx.service.network.dto.other.ScoreDTO;
import com.mygdx.service.network.dto.other.TimeDTO;
import com.mygdx.service.network.dto.screen.LevelScreenDTO;
import com.mygdx.service.network.dto.screen.RuleScreenDTO;
import com.mygdx.service.network.dto.screen.SkinScreenDTO;
import com.mygdx.service.network.dto.screen.WaitScreenDTO;
import com.mygdx.service.network.dto.sync.BonusPositionDTO;
import com.mygdx.service.network.dto.sync.BrickPositionDTO;
import com.mygdx.service.network.enumeration.NetworkGameRequestEnum;
import com.mygdx.service.network.enumeration.NetworkPlayerStateEnum;
import com.mygdx.service.network.enumeration.NetworkRequestEnum;
import com.mygdx.view.client.animation.ClientAnimation;

public class ClientViewScreen implements Screen, MenuListener {

	private static final String CLASS_NAME = "ClientViewScreen.class";
	private static final String JSON_PARSE_EXCEPTION = "JsonParseException : ";
	private static final String JSON_MAPPING_EXCEPTION = "JsonMappingException : ";
	private static final String IO_EXCEPTION = "IOException : ";
	private static final float SKIN_START_X = 70f;
	private static final float SKIN_START_Y = 170f;
	private static final float SKIN_COL_SIZE = 140f;
	private static final float SKIN_ROW_SIZE = 40f;
	private static final float RULE_COLUMN_START_X = 200f;
	private static final float RULE_COLUMN_SPACE = 200f;
	private static final float RULE_ROW_START_Y = 120f;
	private static final float RULE_ROW_SPACE = 20f;
	private static final float LEVEL_SCREEN_LEVEL_PREVIEW_X = 60f;
	private static final float LEVEL_SCREEN_LEVEL_PREVIEW_Y = 75f;
	private static final float LEVEL_SCREEN_START_GRID_X = 260f;
	private static final float LEVEL_SCREEN_START_GRID_Y = 100f;
	private static final float LEVEL_SCREEN_ROW_GRID_SPACE = 15f;
	private static final float LEVEL_SCREEN_COLUMN_GRID_SPACE = 150f;
	private static final float LEVEL_SCREEN_START_BONUS_X = 260f;
	private static final float LEVEL_SCREEN_START_BONUS_Y = 60f;
	private static final float LEVEL_SCREEN_BONUS_SPACE = 25f;

	private final MultiBombermanGame mbGame;
	private final GlyphLayout layout;
	private final ShapeRenderer shapeRenderer;
	private BitmapFont font;

	private ObjectMapper objectMapper;
	private WaitScreenDTO waitScreenDTO;
	private SkinScreenDTO skinScreenDTO;
	private RuleScreenDTO ruleScreenDTO;
	private LevelScreenDTO levelScreenDTO;
	private LevelDTO levelDTO;
	private NetworkRequestEnum last;

	/******************************************
	 * --- GAME PART ---
	 ******************************************/
	List<PlayerPixelDTO> playerPixelBuf = new ArrayList<>();
	List<SpriteGridDTO> spriteGridBuf = new ArrayList<>();
	List<SpritePixelDTO> spritePixelBuf = new ArrayList<>();
	List<SpriteGridDTO> frontSpriteGridBuf = new ArrayList<>();
	List<SpritePixelDTO> frontSpritePixelBuf = new ArrayList<>();
	List<BombePixelDTO> bombePixelBuf = new ArrayList<>();
	List<Integer> additionalWall = new ArrayList<>();
	private List<ClientAnimation> animations = new ArrayList<>();
	private float footInWaterAnimationTime;
	private Animation<TextureRegion> footInWaterAnimation;
	private Map<Integer, Integer> scores;
	private float time;
	private boolean pause = false;
	private boolean menu = false;
	private BrickPositionDTO brickPosition;
	private BonusPositionDTO bonusPosition;

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

	/********************
	 * --- SHADER ---
	 *******************/
	private String vertexShader;
	private String fragmentShader;
	private ShaderProgram shaderProgram;
	private float deltaTime;

	private boolean lightOn;

	public ClientViewScreen(final MultiBombermanGame mbGame) {
		this.mbGame = mbGame;
		this.vertexShader = Gdx.files.internal("shader/vertex.glsl").readString();
		this.fragmentShader = Gdx.files.internal("shader/fragment.glsl").readString();
		this.shaderProgram = new ShaderProgram(vertexShader, fragmentShader);
		this.layout = new GlyphLayout();
		this.shapeRenderer = new ShapeRenderer();
		this.objectMapper = new ObjectMapper();
		this.last = NetworkRequestEnum.WAIT_SCREEN;
		this.initFont();
		this.gameCamera = new OrthographicCamera(Constante.GAME_SCREEN_SIZE_X, Constante.GAME_SCREEN_SIZE_Y);
		this.gameCamera.position.set(Constante.GAME_SCREEN_SIZE_X / 2f, Constante.GAME_SCREEN_SIZE_Y / 2f, 0);
		this.gameCamera.update();
		this.lightOn = false;
		this.scores = new HashMap<>();
		footInWaterAnimationTime = 0f;
		footInWaterAnimation = new Animation<>(SpriteEnum.UNDERWATER.getFrameAnimationTime(),
				SpriteService.getInstance().getSpriteForAnimation(SpriteEnum.UNDERWATER));
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

	}

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

	@Override
	public void render(float delta) {
		if (!mbGame.getNetworkService().getClient().isStatus()) {
			mbGame.getScreen().dispose();
			switch (Context.getClientConnexionMethod()) {
			case INTERNET:
				mbGame.setScreen(new ClientInternetConnexionScreen(mbGame));
				break;
			case LOCAL:
				mbGame.setScreen(new ClientLocalConnexionScreen(mbGame));
				break;
			case IP:
			default:
				mbGame.setScreen(new ClientIPConnexionScreen(mbGame));
				break;
			}
		}
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		mbGame.getScreenCamera().update();
		switch (last) {
		case GAME_SCREEN:
			drawGameScreen();
			break;
		case LEVEL_SCREEN:
			drawLevelScreen();
			break;
		case RULE_SCREEN:
			drawRuleScreen();
			break;
		case SKIN_SCREEN:
			drawSkinScreen();
			break;
		case WAIT_SCREEN:
		default:
			drawWaitScreen();
			break;
		}

	}

	@Override
	public void show() {
		// unused method
	}

	@Override
	public void resize(int width, int height) {
		// unused method
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
		// unused method
	}

	@Override
	public void pressStart() {
		// unused method
	}

	@Override
	public void pressSelect() {
		SoundService.getInstance().playSound(SoundEnum.CANCEL);
		mbGame.getNetworkService().disconnectFromServer();
		mbGame.getScreen().dispose();
		switch (Context.getClientConnexionMethod()) {
		case INTERNET:
			mbGame.setScreen(new ClientInternetConnexionScreen(mbGame));
			break;
		case LOCAL:
			mbGame.setScreen(new ClientLocalConnexionScreen(mbGame));
			break;
		case IP:
		default:
			mbGame.setScreen(new ClientIPConnexionScreen(mbGame));
			break;
		}
	}

	@Override
	public void pressA() {
		// Unused method
	}

	@Override
	public void pressUp() {
		// unused method
	}

	@Override
	public void pressDown() {
		// unused method
	}

	@Override
	public void pressLeft() {
		// unused method
	}

	@Override
	public void pressRight() {
		// unused method
	}

	@Override
	public void pressB() {
		// unused method
	}

	@Override
	public void pressX() {
		// unused method
	}

	@Override
	public void pressY() {
		// unused method
	}

	@Override
	public void pressL() {
		// unused method
	}

	@Override
	public void pressR() {
		// unused method
	}

	/**********************************************
	 * --- RECEIVE STRING AND DECODE : MENU ---
	 **********************************************/
	public void receiveWaitScreen(String line) {
		try {
			waitScreenDTO = this.objectMapper.readValue(line.substring(line.indexOf(':') + 1), WaitScreenDTO.class);
			last = NetworkRequestEnum.WAIT_SCREEN;
		} catch (JsonParseException e) {
			Gdx.app.error(CLASS_NAME, JSON_PARSE_EXCEPTION + e.getMessage());
		} catch (JsonMappingException e) {
			Gdx.app.error(CLASS_NAME, JSON_MAPPING_EXCEPTION + e.getMessage());
		} catch (IOException e) {
			Gdx.app.error(CLASS_NAME, IO_EXCEPTION + e.getMessage());
		}
	}

	public void receiveSkinScreen(String line) {
		try {
			skinScreenDTO = this.objectMapper.readValue(line.substring(line.indexOf(':') + 1), SkinScreenDTO.class);
			last = NetworkRequestEnum.SKIN_SCREEN;
		} catch (JsonParseException e) {
			Gdx.app.error(CLASS_NAME, JSON_PARSE_EXCEPTION + e.getMessage());
		} catch (JsonMappingException e) {
			Gdx.app.error(CLASS_NAME, JSON_MAPPING_EXCEPTION + e.getMessage());
		} catch (IOException e) {
			Gdx.app.error(CLASS_NAME, IO_EXCEPTION + e.getMessage());
		}
	}

	public void receiveRuleScreen(String line) {
		try {
			ruleScreenDTO = this.objectMapper.readValue(line.substring(line.indexOf(':') + 1), RuleScreenDTO.class);
			last = NetworkRequestEnum.RULE_SCREEN;
		} catch (JsonParseException e) {
			Gdx.app.error(CLASS_NAME, JSON_PARSE_EXCEPTION + e.getMessage());
		} catch (JsonMappingException e) {
			Gdx.app.error(CLASS_NAME, JSON_MAPPING_EXCEPTION + e.getMessage());
		} catch (IOException e) {
			Gdx.app.error(CLASS_NAME, IO_EXCEPTION + e.getMessage());
		}
	}

	public void receiveLevelScreen(String line) {
		try {
			levelScreenDTO = this.objectMapper.readValue(line.substring(line.indexOf(':') + 1), LevelScreenDTO.class);
			last = NetworkRequestEnum.LEVEL_SCREEN;
		} catch (JsonParseException e) {
			Gdx.app.error(CLASS_NAME, JSON_PARSE_EXCEPTION + e.getMessage());
		} catch (JsonMappingException e) {
			Gdx.app.error(CLASS_NAME, JSON_MAPPING_EXCEPTION + e.getMessage());
		} catch (IOException e) {
			Gdx.app.error(CLASS_NAME, IO_EXCEPTION + e.getMessage());
		}
	}

	public void receiveLevelDef(String line) {
		try {
			levelDTO = this.objectMapper.readValue(line.substring(line.indexOf(':') + 1), LevelDTO.class);
			additionalWall = new ArrayList<>();
		} catch (JsonParseException e) {
			Gdx.app.error(CLASS_NAME, JSON_PARSE_EXCEPTION + e.getMessage());
		} catch (JsonMappingException e) {
			Gdx.app.error(CLASS_NAME, JSON_MAPPING_EXCEPTION + e.getMessage());
		} catch (IOException e) {
			Gdx.app.error(CLASS_NAME, IO_EXCEPTION + e.getMessage());
		}
	}

	public void receiveSound(String line) {
		SoundService.getInstance().decodeSoundCommand(line);
	}

	/**********************************************
	 * --- DRAW PART : MENU---
	 **********************************************/

	public void drawWaitScreen() {
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		mbGame.getScreenCamera().update();
		mbGame.getBatch().begin();
		mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.BACKGROUND, 1), 0, 0);
		mbGame.getBatch().end();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setProjectionMatrix(mbGame.getBatch().getProjectionMatrix());
		shapeRenderer.setColor(0, 0, 0, 0.5f);
		shapeRenderer.rect(10, 10, 620, 210);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		mbGame.getBatch().begin();
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.waitConnexion"));
		font.draw(mbGame.getBatch(), layout, ((float) Constante.SCREEN_SIZE_X / 2) - (layout.width / 2), 210);
		if (waitScreenDTO != null) {
			layout.setText(font, MessageService.getInstance().getMessage("game.menu.waitConnexion.client"));
			font.draw(mbGame.getBatch(), layout, ((float) Constante.SCREEN_SIZE_X / 2) - (layout.width / 2), 190);
			layout.setText(font, MessageService.getInstance().getMessage("game.menu.waitConnexion.human"));
			font.draw(mbGame.getBatch(), layout, ((float) Constante.SCREEN_SIZE_X / 2) - (layout.width / 2), 150);
			layout.setText(font, Integer.toString(this.waitScreenDTO.getNbClient()));
			font.draw(mbGame.getBatch(), layout, ((float) Constante.SCREEN_SIZE_X / 2) - (layout.width / 2), 170);
			layout.setText(font, Integer.toString(this.waitScreenDTO.getNbHumainPlayer()));
			font.draw(mbGame.getBatch(), layout, ((float) Constante.SCREEN_SIZE_X / 2) - (layout.width / 2), 130);
		}
		mbGame.getBatch().end();
	}

	private void drawSkinScreen() {
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		mbGame.getScreenCamera().update();
		mbGame.getBatch().begin();
		mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.BACKGROUND, 1), 0, 0);
		mbGame.getBatch().end();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setProjectionMatrix(mbGame.getBatch().getProjectionMatrix());
		shapeRenderer.setColor(0, 0, 0, 0.5f);
		shapeRenderer.rect(10, 10, 620, 210);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		mbGame.getBatch().begin();
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.skinScreen"));
		font.draw(mbGame.getBatch(), layout, ((float) Constante.SCREEN_SIZE_X / 2) - (layout.width / 2), 210);
		for (int j = 0; j < 4; j++) {
			for (int i = 0; i < 4; i++) {
				int pos = i + j * 4;
				layout.setText(font, MessageService.getInstance().getMessage("game.menu.player") + pos + " : ");
				font.draw(mbGame.getBatch(), layout, SKIN_START_X + (i * SKIN_COL_SIZE),
						SKIN_START_Y - (j * SKIN_ROW_SIZE));
				if (skinScreenDTO.getDefinitions().get(pos).getPlayerType() != PlayerTypeEnum.NONE) {
					mbGame.getBatch()
							.draw(SpriteService.getInstance().getSprite(CharacterSpriteEnum.WALK_DOWN,
									skinScreenDTO.getDefinitions().get(pos).getColor(),
									skinScreenDTO.getDefinitions().get(pos).getCharacter(), 0),
									SKIN_START_X + (i * SKIN_COL_SIZE) + 60, SKIN_START_Y - (j * SKIN_ROW_SIZE) - 10f);
				}
			}
		}
		mbGame.getBatch().end();
	}

	private void drawRuleScreen() {
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		mbGame.getScreenCamera().update();
		mbGame.getBatch().begin();
		mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.BACKGROUND, 1), 0, 0);
		mbGame.getBatch().end();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setProjectionMatrix(mbGame.getBatch().getProjectionMatrix());

		shapeRenderer.setColor(0, 0, 0, 0.5f);
		shapeRenderer.rect(10, 10, 620, 210);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		mbGame.getBatch().begin();
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.ruleScreen"));
		font.draw(mbGame.getBatch(), layout, ((float) Constante.SCREEN_SIZE_X / 2) - (layout.width / 2), 210);

		layout.setText(font, MessageService.getInstance().getMessage("game.menu.ruleScreen.suddenDeath"));
		font.draw(mbGame.getBatch(), layout, RULE_COLUMN_START_X, RULE_ROW_START_Y + (RULE_ROW_SPACE * 3));
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.ruleScreen.badBomber"));
		font.draw(mbGame.getBatch(), layout, RULE_COLUMN_START_X, RULE_ROW_START_Y + (RULE_ROW_SPACE * 2));
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.ruleScreen.iaLevel"));
		font.draw(mbGame.getBatch(), layout, RULE_COLUMN_START_X, RULE_ROW_START_Y + (RULE_ROW_SPACE * 1));
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.ruleScreen.time"));
		font.draw(mbGame.getBatch(), layout, RULE_COLUMN_START_X, RULE_ROW_START_Y + (RULE_ROW_SPACE * 0));

		layout.setText(font, ruleScreenDTO.isSuddenDeath() ? MessageService.getInstance().getMessage("game.menu.yes")
				: MessageService.getInstance().getMessage("game.menu.no"));
		font.draw(mbGame.getBatch(), layout, RULE_COLUMN_START_X + RULE_COLUMN_SPACE,
				RULE_ROW_START_Y + (RULE_ROW_SPACE * 3));
		layout.setText(font, ruleScreenDTO.isBadBomber() ? MessageService.getInstance().getMessage("game.menu.yes")
				: MessageService.getInstance().getMessage("game.menu.no"));
		font.draw(mbGame.getBatch(), layout, RULE_COLUMN_START_X + RULE_COLUMN_SPACE,
				RULE_ROW_START_Y + (RULE_ROW_SPACE * 2));
		layout.setText(font, Integer.toString(ruleScreenDTO.getIaLevel()));
		font.draw(mbGame.getBatch(), layout, RULE_COLUMN_START_X + RULE_COLUMN_SPACE,
				RULE_ROW_START_Y + (RULE_ROW_SPACE * 1));
		layout.setText(font, MessageService.getInstance().getMessage(ruleScreenDTO.getTime().getKey()));
		font.draw(mbGame.getBatch(), layout, RULE_COLUMN_START_X + RULE_COLUMN_SPACE,
				RULE_ROW_START_Y + (RULE_ROW_SPACE * 0));
		mbGame.getBatch().end();
	}

	private void drawLevelScreen() {
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		mbGame.getScreenCamera().update();
		mbGame.getBatch().begin();
		mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.BACKGROUND, 1), 0, 0);
		mbGame.getBatch().end();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setProjectionMatrix(mbGame.getBatch().getProjectionMatrix());

		shapeRenderer.setColor(0, 0, 0, 0.5f);
		shapeRenderer.rect(10, 10, 620, 210);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		mbGame.getBatch().begin();
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.levelScreen"));
		font.draw(mbGame.getBatch(), layout, ((float) Constante.SCREEN_SIZE_X / 2) - (layout.width / 2), 210);

		layout.setText(font, MessageService.getInstance().getMessage("game.menu.levelScreen.bombe"));
		font.draw(mbGame.getBatch(), layout, LEVEL_SCREEN_START_GRID_X,
				LEVEL_SCREEN_START_GRID_Y + (LEVEL_SCREEN_ROW_GRID_SPACE * 6));
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.levelScreen.strenght"));
		font.draw(mbGame.getBatch(), layout, LEVEL_SCREEN_START_GRID_X,
				LEVEL_SCREEN_START_GRID_Y + (LEVEL_SCREEN_ROW_GRID_SPACE * 5));
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.levelScreen.levelGroup"));
		font.draw(mbGame.getBatch(), layout, LEVEL_SCREEN_START_GRID_X,
				LEVEL_SCREEN_START_GRID_Y + (LEVEL_SCREEN_ROW_GRID_SPACE * 4));
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.levelScreen.level"));
		font.draw(mbGame.getBatch(), layout, LEVEL_SCREEN_START_GRID_X,
				LEVEL_SCREEN_START_GRID_Y + (LEVEL_SCREEN_ROW_GRID_SPACE * 3));
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.levelScreen.description"));
		font.draw(mbGame.getBatch(), layout, LEVEL_SCREEN_START_GRID_X,
				LEVEL_SCREEN_START_GRID_Y + (LEVEL_SCREEN_ROW_GRID_SPACE * 1));
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.levelScreen.bonus"));
		font.draw(mbGame.getBatch(), layout, LEVEL_SCREEN_START_GRID_X, LEVEL_SCREEN_START_GRID_Y);

		layout.setText(font, Integer.toString(levelScreenDTO.getBombe()));
		font.draw(mbGame.getBatch(), layout, LEVEL_SCREEN_START_GRID_X + LEVEL_SCREEN_COLUMN_GRID_SPACE,
				LEVEL_SCREEN_START_GRID_Y + (LEVEL_SCREEN_ROW_GRID_SPACE * 6));
		layout.setText(font, Integer.toString(levelScreenDTO.getStrenght()));
		font.draw(mbGame.getBatch(), layout, LEVEL_SCREEN_START_GRID_X + LEVEL_SCREEN_COLUMN_GRID_SPACE,
				LEVEL_SCREEN_START_GRID_Y + (LEVEL_SCREEN_ROW_GRID_SPACE * 5));
		layout.setText(font, levelScreenDTO.getGroup());
		font.draw(mbGame.getBatch(), layout, LEVEL_SCREEN_START_GRID_X + LEVEL_SCREEN_COLUMN_GRID_SPACE,
				LEVEL_SCREEN_START_GRID_Y + (LEVEL_SCREEN_ROW_GRID_SPACE * 4));
		layout.setText(font, levelScreenDTO.getName());
		font.draw(mbGame.getBatch(), layout, LEVEL_SCREEN_START_GRID_X + LEVEL_SCREEN_COLUMN_GRID_SPACE,
				LEVEL_SCREEN_START_GRID_Y + (LEVEL_SCREEN_ROW_GRID_SPACE * 3));
		layout.setText(font, levelScreenDTO.getDescription());
		font.draw(mbGame.getBatch(), layout, LEVEL_SCREEN_START_GRID_X + LEVEL_SCREEN_COLUMN_GRID_SPACE,
				LEVEL_SCREEN_START_GRID_Y + (LEVEL_SCREEN_ROW_GRID_SPACE * 1));
		for (int i = 0; i < Constante.MAX_BONUS; i++) {
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.BONUS, i),
					LEVEL_SCREEN_START_BONUS_X + (i * LEVEL_SCREEN_BONUS_SPACE), LEVEL_SCREEN_START_BONUS_Y);
			layout.setText(font, Integer.toString(levelScreenDTO.getBonus()[i]));
			font.draw(mbGame.getBatch(), layout,
					(LEVEL_SCREEN_START_BONUS_X + (i * LEVEL_SCREEN_BONUS_SPACE) + 8) - (layout.width / 2),
					LEVEL_SCREEN_START_BONUS_Y - 5);
		}
		mbGame.getBatch().draw(
				SpriteService.getInstance().getSprite(SpriteEnum.PREVIEW, levelScreenDTO.getIndexPreview()),
				LEVEL_SCREEN_LEVEL_PREVIEW_X, LEVEL_SCREEN_LEVEL_PREVIEW_Y);
		mbGame.getBatch().end();
	}

	/********************************************************
	 * --- RECEIVE STRING AND DECODE : GAME PART ---
	 *********************************************************/
	private byte[] readRequest(ByteBuffer bbd, NetworkGameRequestEnum req) {
		int lengthToCopy;
		byte[] tmp;
		tmp = new byte[req.getRequestLenght() - 1];
		lengthToCopy = req.getRequestLenght() - 1;
		bbd.get(tmp, 0, lengthToCopy);
		return tmp;
	}

	public void receiveGame(String line) {
		this.last = NetworkRequestEnum.GAME_SCREEN;
		boolean pauseReceive = false;
		boolean menuReceive = false;
		List<PlayerPixelDTO> playerPixel = new ArrayList<>();
		List<SpriteGridDTO> spriteGrid = new ArrayList<>();
		List<SpritePixelDTO> spritePixel = new ArrayList<>();
		List<SpriteGridDTO> frontSpriteGrid = new ArrayList<>();
		List<SpritePixelDTO> frontSpritePixel = new ArrayList<>();
		List<BombePixelDTO> bombePixel = new ArrayList<>();
		int decodedIndex = 0;

		ByteBuffer bbd = ByteBuffer.wrap(Base64.getDecoder().decode(line.substring(line.indexOf(':') + 1)));
		while (bbd.position() < bbd.capacity()) {
			int reqIndex = (int) bbd.get();
			NetworkGameRequestEnum req = NetworkGameRequestEnum.values()[reqIndex];
			switch (req) {
			case RESET_ADDITIONAL_WALL:
				this.footInWaterAnimationTime = 0f;
				lightOn = false;
				this.additionalWall.clear();
				break;
			case ADD_WALL:
				decodedIndex = GenericDeltaDTO.getIndex(readRequest(bbd, req));
				this.additionalWall.add(decodedIndex);
				break;
			case BONUS_BURN:
				decodedIndex = GenericDeltaDTO.getIndex(readRequest(bbd, req));
				bonusPosition.removeBonus(decodedIndex);
				animations.add(new ClientAnimation(mbGame, SpriteEnum.BONUS_BURN, decodedIndex));
				break;
			case BONUS_POSITION:
				bonusPosition = new BonusPositionDTO(readRequest(bbd, req));
				break;
			case BONUS_REMOVE:
				bonusPosition.removeBonus(GenericDeltaDTO.getIndex(readRequest(bbd, req)));
				break;
			case BONUS_REVEALED:
				BonusRevealedDTO br = new BonusRevealedDTO(readRequest(bbd, req));
				bonusPosition.bonusAppeared(br.getGridIndex(), br.getType());
				break;
			case BONUS_TAKED:
				bonusPosition.removeBonus(GenericDeltaDTO.getIndex(readRequest(bbd, req)));
				break;
			case BRICK_BURN:
				decodedIndex = GenericDeltaDTO.getIndex(readRequest(bbd, req));
				brickPosition.removeBrick(decodedIndex);
				animations.add(new ClientAnimation(mbGame, this.levelDTO.getDefaultBrickAnimation(), decodedIndex));
				break;
			case BRICK_POSITION:
				brickPosition = new BrickPositionDTO(readRequest(bbd, req));
				break;
			case BRICK_REMOVE:
				brickPosition.removeBrick(GenericDeltaDTO.getIndex(readRequest(bbd, req)));
				break;
			case DRAW_BOMBE:
				bombePixel.add(new BombePixelDTO(readRequest(bbd, req)));
				break;
			case DRAW_FRONT_GRID:
				frontSpriteGrid.add(new SpriteGridDTO(readRequest(bbd, req)));
				break;
			case DRAW_FRONT_PIXEL:
				frontSpritePixel.add(new SpritePixelDTO(readRequest(bbd, req)));
				break;
			case DRAW_GRID:
				spriteGrid.add(new SpriteGridDTO(readRequest(bbd, req)));
				break;
			case DRAW_PIXEL:
				spritePixel.add(new SpritePixelDTO(readRequest(bbd, req)));
				break;
			case DRAW_PLAYER:
			case DRAW_PLAYER_ON_LOUIS:
				playerPixel.add(new PlayerPixelDTO(readRequest(bbd, req)));
				break;
			case FIRE_APPEARED:
				FireAppareadDTO fd = new FireAppareadDTO(readRequest(bbd, req));
				animations.add(new ClientAnimation(mbGame, fd.getFireEnum().getSpriteEnum(), fd.getGridIndex()));
				break;
			case MENU_OVERLAY:
				menuReceive = true;
				break;
			case PAUSE_OVERLAY:
				pauseReceive = true;
				break;
			case DRAW_GAME_OVERLAY:
				pauseReceive = true;
				break;
			case SCORE_OVERLAY:
				pauseReceive = true;
				break;
			case SCORE:
				ScoreDTO dto = new ScoreDTO(readRequest(bbd, req));
				if (scores != null) {
					this.scores.put(dto.getPlayerIndex(), dto.getScore());
				}
				break;
			case TIME:
				this.time = new TimeDTO(readRequest(bbd, req)).getTime();
				break;
			case TURN_LIGHT_ON:
				this.lightOn = true;
				break;
			default:
				break;

			}
		}
		this.menu = menuReceive;
		this.pause = pauseReceive;
		this.playerPixelBuf = playerPixel;
		this.bombePixelBuf = bombePixel;
		this.frontSpriteGridBuf = frontSpriteGrid;
		this.frontSpritePixelBuf = frontSpritePixel;
		this.spriteGridBuf = spriteGrid;
		this.spritePixelBuf = spritePixel;
	}

	/********************************************************
	 * --- RECEIVE STRING AND DECODE : GAME PART ---
	 *********************************************************/

	private void drawGameScreen() {

		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.gameCamera.update();
		this.mbGame.getScreenCamera().update();
		drawBackgroundLayer();
		drawWallLayer();
		drawBricksLayer();
		drawPlayerLayer();
		drawFrontLayer();
		drawShadowLayer();
		mergeLayer();
		drawScore();
		if (pause) {
			drawPause();
		} else if (menu) {
			drawMenu();
		}
		cleanUpGameElement();
	}

	private void drawBackgroundLayer() {
		backgroundLayer.begin();
		mbGame.getBatch().begin();
		mbGame.getBatch().setProjectionMatrix(gameCamera.combined);
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (levelDTO != null) {
			for (int x = 0; x < Constante.GRID_SIZE_X; x++) {
				for (int y = 0; y < Constante.GRID_SIZE_Y; y++) {
					mbGame.getBatch().draw(
							SpriteService.getInstance().getSprite(this.levelDTO.getDefaultBackground().getAnimation(),
									this.levelDTO.getDefaultBackground().getIndex()),
							x * Constante.GRID_PIXELS_SIZE_X, y * Constante.GRID_PIXELS_SIZE_Y);
				}
			}
			this.levelDTO.getCustomBackgroundTexture().stream()
					.forEach(cbt -> mbGame.getBatch().draw(
							SpriteService.getInstance().getSprite(cbt.getAnimation(), cbt.getIndex()),
							cbt.getX() * Constante.GRID_PIXELS_SIZE_X, cbt.getY() * Constante.GRID_PIXELS_SIZE_Y));
		}
		mbGame.getBatch().end();
		backgroundLayer.end();
		backgroundLayerTextureRegion = new TextureRegion(backgroundLayerTexture);
		backgroundLayerTextureRegion.flip(false, true);
	}

	private void drawWallLayer() {
		blocsLayer.begin();
		mbGame.getBatch().begin();
		mbGame.getBatch().setProjectionMatrix(gameCamera.combined);
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (levelDTO != null) {
			levelDTO.getWall().stream().forEach(w -> {
				if (w.isDraw()) {
					if (w.getTexture() != null) {
						mbGame.getBatch()
								.draw(SpriteService.getInstance().getSprite(w.getTexture().getAnimation(),
										w.getTexture().getIndex()), w.getX() * Constante.GRID_PIXELS_SIZE_X,
										w.getY() * Constante.GRID_PIXELS_SIZE_Y);
					} else {
						mbGame.getBatch().draw(
								SpriteService.getInstance().getSprite(this.levelDTO.getDefaultWall().getAnimation(),
										this.levelDTO.getDefaultWall().getIndex()),
								w.getX() * Constante.GRID_PIXELS_SIZE_X, w.getY() * Constante.GRID_PIXELS_SIZE_Y);
					}
				}
			});
			if (this.additionalWall != null) {
				this.additionalWall.stream().forEach(w -> {
					int x = w % Constante.GRID_SIZE_X;
					int y = Math.floorDiv(w, Constante.GRID_SIZE_X);
					mbGame.getBatch()
							.draw(SpriteService.getInstance().getSprite(this.levelDTO.getDefaultWall().getAnimation(),
									this.levelDTO.getDefaultWall().getIndex()), x * Constante.GRID_PIXELS_SIZE_X,
									y * Constante.GRID_PIXELS_SIZE_Y);
				});
			}
		}
		mbGame.getBatch().end();
		blocsLayerTextureRegion = new TextureRegion(blocsLayerTexture);
		blocsLayerTextureRegion.flip(false, true);
		blocsLayer.end();
	}

	private void drawBricksLayer() {
		bricksLayer.begin();
		mbGame.getBatch().begin();
		mbGame.getBatch().setProjectionMatrix(gameCamera.combined);
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		for (int i = 0; i < Constante.GRID_SIZE_X * Constante.GRID_SIZE_Y; i++) {
			if (this.brickPosition != null && this.brickPosition.hasBrick(i)) {
				int x = i % Constante.GRID_SIZE_X;
				int y = Math.floorDiv(i, Constante.GRID_SIZE_X);
				mbGame.getBatch().draw(
						SpriteService.getInstance().getSprite(this.levelDTO.getDefaultBrickAnimation(), 0),
						x * Constante.GRID_PIXELS_SIZE_X, (y * Constante.GRID_PIXELS_SIZE_Y));
			}
		}
		mbGame.getBatch().end();
		bricksLayerTextureRegion = new TextureRegion(bricksLayerTexture);
		bricksLayerTextureRegion.flip(false, true);
		bricksLayer.end();
	}

	private void drawPlayerLayer() {
		playerLayer.begin();
		mbGame.getBatch().begin();
		mbGame.getBatch().setProjectionMatrix(gameCamera.combined);
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		animations.stream().forEach(ClientAnimation::drawIt);
		drawBackSpriteGrid();
		drawBackSpritePixels();
		drawBombe();
		drawBonus();
		drawPlayer();
		drawFrontSpriteGrid();
		drawFrontSpritePixels();
		mbGame.getBatch().end();
		playerLayerTextureRegion = new TextureRegion(playerLayerTexture);
		playerLayerTextureRegion.flip(false, true);
		playerLayer.end();
	}

	private void drawBackSpriteGrid() {
		this.spriteGridBuf.stream().forEach(e -> {
			int x = e.getGridIndex() % Constante.GRID_SIZE_X;
			int y = Math.floorDiv(e.getGridIndex(), Constante.GRID_SIZE_X);
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(e.getSprite(), e.getIndex()),
					x * Constante.GRID_PIXELS_SIZE_X, y * Constante.GRID_PIXELS_SIZE_Y);
		});
	}

	private void drawBackSpritePixels() {
		this.spritePixelBuf.stream().forEach(e -> {
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(e.getSprite(), e.getIndex()), e.getX(),
					e.getY());
		});
	}

	private void drawFrontSpriteGrid() {
		this.frontSpriteGridBuf.stream().forEach(e -> {
			int x = e.getGridIndex() % Constante.GRID_SIZE_X;
			int y = Math.floorDiv(e.getGridIndex(), Constante.GRID_SIZE_X);
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(e.getSprite(), e.getIndex()),
					x * Constante.GRID_PIXELS_SIZE_X, y * Constante.GRID_PIXELS_SIZE_Y);
		});
	}

	private void drawFrontSpritePixels() {
		this.frontSpritePixelBuf.stream().forEach(e -> {
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(e.getSprite(), e.getIndex()), e.getX(),
					e.getY());
		});
	}

	private void drawBonus() {
		if (this.bonusPosition != null) {
			for (int i = 0; i < Constante.GRID_SIZE_X * Constante.GRID_SIZE_Y; i++) {
				BonusTypeEnum type = bonusPosition.get(i);
				if (type != BonusTypeEnum.NONE) {
					int x = i % Constante.GRID_SIZE_X;
					int y = Math.floorDiv(i, Constante.GRID_SIZE_X);
					mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.BONUS, type.ordinal()),
							x * Constante.GRID_PIXELS_SIZE_X, y * Constante.GRID_PIXELS_SIZE_Y);
				}
			}
		}
	}

	private void drawBombe() {
		this.bombePixelBuf.stream().forEach(b -> {
			mbGame.getBatch().draw(
					SpriteService.getInstance().getSprite(b.getBombeTypeEnum().getSpriteEnum(), b.getSpriteIndex()),
					b.getX(), b.getY());
		});
	}

	private void drawPlayer() {
		footInWaterAnimationTime += Gdx.app.getGraphics().getDeltaTime();
		this.playerPixelBuf.stream().filter(p -> p.getNetworkPlayerStateEnum() != NetworkPlayerStateEnum.DEAD)
				.forEach(p -> {
					PlayerDefinition def = this.skinScreenDTO.getDefinitions().get(p.getPlayerIndex());

					switch (p.getNetworkPlayerStateEnum()) {
					case LOUIS:
						if (p.getCharacterSpriteEnum() == CharacterSpriteEnum.ON_LOUIS_DOWN) {
							mbGame.getBatch().draw(
									SpriteService.getInstance().getSprite(p.getCharacterSpriteEnum(), def.getColor(),
											def.getCharacter(), 0),
									p.getX() * Constante.GRID_PIXELS_SIZE_X - 15f,
									p.getY() * Constante.GRID_PIXELS_SIZE_Y - 5f);
							mbGame.getBatch().draw(
									SpriteService.getInstance().getSprite(p.getLouisSpriteEnum(), p.getLouisColorEnum(),
											p.getLouisSpriteIndex()),
									p.getX() * Constante.GRID_PIXELS_SIZE_X - 15f,
									p.getY() * Constante.GRID_PIXELS_SIZE_Y - 5f);
						} else {
							mbGame.getBatch().draw(
									SpriteService.getInstance().getSprite(p.getLouisSpriteEnum(), p.getLouisColorEnum(),
											p.getLouisSpriteIndex()),
									p.getX() * Constante.GRID_PIXELS_SIZE_X - 15f,
									p.getY() * Constante.GRID_PIXELS_SIZE_Y - 5f);
							mbGame.getBatch().draw(
									SpriteService.getInstance().getSprite(p.getCharacterSpriteEnum(), def.getColor(),
											def.getCharacter(), 0),
									p.getX() * Constante.GRID_PIXELS_SIZE_X - 15f,
									p.getY() * Constante.GRID_PIXELS_SIZE_Y - 5f);
						}
						break;
					case BADBOMBER:
						mbGame.getBatch()
								.draw(SpriteService.getInstance().getSprite(p.getCharacterSpriteEnum(), def.getColor(),
										def.getCharacter(), p.getCharacterSpriteIndex()),
										p.getX() * Constante.GRID_PIXELS_SIZE_X - 15f,
										p.getY() * Constante.GRID_PIXELS_SIZE_Y - 20f);
						mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.SPACESHIP, 0),
								p.getX() * Constante.GRID_PIXELS_SIZE_X - 15f,
								p.getY() * Constante.GRID_PIXELS_SIZE_Y - 20f);
						break;
					case OTHER:
					case TROLLEY:
						mbGame.getBatch()
								.draw(SpriteService.getInstance().getSprite(p.getCharacterSpriteEnum(), def.getColor(),
										def.getCharacter(), p.getCharacterSpriteIndex()),
										p.getX() * Constante.GRID_PIXELS_SIZE_X - 15f,
										p.getY() * Constante.GRID_PIXELS_SIZE_Y - 5f);
						break;
					case DEAD:
					default:
						break;
					}
					if (this.levelDTO != null && this.levelDTO.isFootInWater()
							&& (p.getNetworkPlayerStateEnum() == NetworkPlayerStateEnum.OTHER
									|| p.getNetworkPlayerStateEnum() == NetworkPlayerStateEnum.LOUIS)) {
						mbGame.getBatch().draw(footInWaterAnimation.getKeyFrame(footInWaterAnimationTime, true),
								p.getX() * Constante.GRID_PIXELS_SIZE_X - 9f,
								p.getY() * Constante.GRID_PIXELS_SIZE_Y - 5f);
					}
				});

	}

	private void drawFrontLayer() {
		frontLayer.begin();
		mbGame.getBatch().begin();
		mbGame.getBatch().setProjectionMatrix(gameCamera.combined);
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (this.levelDTO != null) {
			this.levelDTO.getCustomForegroundTexture().stream()
					.forEach(cft -> mbGame.getBatch().draw(
							SpriteService.getInstance().getSprite(cft.getAnimation(), cft.getIndex()),
							(cft.getX() * Constante.GRID_PIXELS_SIZE_X) - Constante.GRID_PIXELS_SIZE_X,
							(cft.getY() * Constante.GRID_PIXELS_SIZE_Y) - Constante.GRID_PIXELS_SIZE_Y));
		}
		mbGame.getBatch().end();
		frontLayerTextureRegion = new TextureRegion(frontLayerTexture);
		frontLayerTextureRegion.flip(false, true);
		frontLayer.end();
	}

	private void drawShadowLayer() {
		shadowLayer.begin();
		mbGame.getBatch().begin();
		mbGame.getBatch().setProjectionMatrix(gameCamera.combined);
		Gdx.gl.glClearColor(0f, 0f, 0f, this.levelDTO.getShadow());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glColorMask(false, false, false, true);
		shapeRenderer.setProjectionMatrix(gameCamera.combined);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(new Color(0f, 0f, 0f, 0f));
		if (lightOn) {
			Collections.sort(this.playerPixelBuf);
			this.playerPixelBuf.stream().forEach(p -> {
				if (p.getNetworkPlayerStateEnum() != NetworkPlayerStateEnum.DEAD) {
					shapeRenderer.circle(p.getX() * Constante.GRID_PIXELS_SIZE_X,
							p.getY() * Constante.GRID_PIXELS_SIZE_Y, 24);
				}
			});
			this.animations.stream()
					.filter(a -> a.getSpriteEnum() != SpriteEnum.LEVEL1 && a.getSpriteEnum() != SpriteEnum.LEVEL2
							&& a.getSpriteEnum() != SpriteEnum.LEVEL3 && a.getSpriteEnum() != SpriteEnum.LEVEL4
							&& a.getSpriteEnum() != SpriteEnum.LEVEL5 && a.getSpriteEnum() != SpriteEnum.LEVEL6
							&& a.getSpriteEnum() != SpriteEnum.LEVEL7 && a.getSpriteEnum() != SpriteEnum.LEVEL8
							&& a.getSpriteEnum() != SpriteEnum.LEVEL9 && a.getSpriteEnum() != SpriteEnum.LEVEL10)
					.forEach(a -> {
						shapeRenderer.circle(a.getX() * Constante.GRID_PIXELS_SIZE_X,
								a.getY() * Constante.GRID_PIXELS_SIZE_Y, 24);
					});
			this.bombePixelBuf.stream().forEach(b -> {
				shapeRenderer.setColor(new Color(0f, 0f, 0f, 0f));
				BombeLight light = b.getBombeTypeEnum().getOffsetLight().get(b.getSpriteIndex());
				shapeRenderer.circle((float) (b.getX() + 9.0f + light.getX()), b.getY() + 8.0f + (float) light.getY(),
						(float) light.getRadius());
			});
		}
		shapeRenderer.end();
		Gdx.gl.glColorMask(true, true, true, true);
		mbGame.getBatch().end();
		shadowLayerTextureRegion = new TextureRegion(shadowLayerTexture);
		shadowLayerTextureRegion.flip(false, true);
		shadowLayer.end();
	}

	private void mergeLayer() {
		mbGame.getBatch().begin();
		mbGame.getBatch().setProjectionMatrix(mbGame.getScreenCamera().combined);
		mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.BACKGROUND, 0), 0, 0);
		if (this.levelDTO.isLevelUnderWater()) {
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

	public TextureRegion getMiniatureHappy(CharacterColorEnum color, CharacterEnum character) {
		return SpriteService.getInstance().getSprite(CharacterSpriteEnum.SCORE_HAPPY, color, character, 0);
	}

	public TextureRegion getMiniatureCry(CharacterColorEnum color, CharacterEnum character) {
		return SpriteService.getInstance().getSprite(CharacterSpriteEnum.SCORE_CRY, color, character, 0);
	}

	private void drawScore() {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.setProjectionMatrix(mbGame.getBatch().getProjectionMatrix());
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0, 0, 0, 0.6f);
		Map<Integer, PlayerPixelDTO> pls = new HashMap<>();
		this.playerPixelBuf.stream().forEach(p -> pls.put(p.getPlayerIndex(), p));

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
			PlayerDefinition def = this.skinScreenDTO.getDefinitions().get(e.getValue().getPlayerIndex());
			float x = ((float) e.getKey() * 36f) + 3f;
			if (e.getKey() >= 8) {
				x += 58f;
			}
			if (e.getValue().getNetworkPlayerStateEnum() == NetworkPlayerStateEnum.DEAD) {
				mbGame.getBatch().draw(getMiniatureCry(def.getColor(), def.getCharacter()), x, 340f, 18f, 16f);
			} else {
				mbGame.getBatch().draw(getMiniatureHappy(def.getColor(), def.getCharacter()), x, 340f, 18f, 16f);
			}
			if (scores.containsKey(e.getKey())) {
				layout.setText(font, Integer.toString(this.scores.get(e.getKey())));
			} else {
				layout.setText(font, Integer.toString(0));
			}
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
			layout.setText(font, "" + df.format(this.time));
		} else {
			layout.setText(font, "inf");
		}
		font.draw(mbGame.getBatch(), layout, (Constante.SCREEN_SIZE_X / 2.0f) - (layout.width / 2.0f), 352);
		mbGame.getBatch().end();
	}

	private void drawPause() {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.setProjectionMatrix(mbGame.getBatch().getProjectionMatrix());
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0, 0, 0, 0.5f);
		shapeRenderer.rect(0, 0, 640, 360);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		mbGame.getBatch().begin();
		drawPlayerNumber();
		layout.setText(font, MessageService.getInstance().getMessage("game.pause.libelle"));
		font.draw(mbGame.getBatch(), layout, (Constante.SCREEN_SIZE_X / 2f) - (layout.width / 2f), 210);
		mbGame.getBatch().end();
	}

	public void drawPlayerNumber() {
		if (!playerPixelBuf.isEmpty()) {
			playerPixelBuf.stream().forEach(p -> {
				layout.setText(font, Integer.toString(p.getPlayerIndex()));
				if (p.getNetworkPlayerStateEnum() != NetworkPlayerStateEnum.DEAD) {
					font.draw(mbGame.getBatch(), layout, p.getX() * Constante.GRID_PIXELS_SIZE_X,
							p.getY() * Constante.GRID_PIXELS_SIZE_Y);
				}
			});
		}
	}

	private void drawMenu() {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.setProjectionMatrix(mbGame.getBatch().getProjectionMatrix());
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0, 0, 0, 0.5f);
		shapeRenderer.rect(0, 0, 640, 360);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		mbGame.getBatch().begin();
		layout.setText(font, MessageService.getInstance().getMessage("game.game.menu.libelle"));
		font.draw(mbGame.getBatch(), layout, (Constante.SCREEN_SIZE_X / 2f) - (layout.width / 2f), 210);
		mbGame.getBatch().end();
	}

	private void cleanUpGameElement() {
		this.animations = this.animations.stream().filter(a -> !a.canBeRemove()).collect(Collectors.toList());
	}

}
