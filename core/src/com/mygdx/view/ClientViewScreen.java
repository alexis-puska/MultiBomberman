package com.mygdx.view;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygdx.constante.Constante;
import com.mygdx.dto.level.LevelDTO;
import com.mygdx.enumeration.CharacterSpriteEnum;
import com.mygdx.enumeration.PlayerTypeEnum;
import com.mygdx.enumeration.SoundEnum;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.MessageService;
import com.mygdx.service.SoundService;
import com.mygdx.service.SpriteService;
import com.mygdx.service.input_processor.MenuListener;
import com.mygdx.service.network.dto.BrickPositionDTO;
import com.mygdx.service.network.dto.LevelScreenDTO;
import com.mygdx.service.network.dto.LightDTO;
import com.mygdx.service.network.dto.PlayerPixelDTO;
import com.mygdx.service.network.dto.RuleScreenDTO;
import com.mygdx.service.network.dto.ScoreDTO;
import com.mygdx.service.network.dto.SkinScreenDTO;
import com.mygdx.service.network.dto.SpriteGridDTO;
import com.mygdx.service.network.dto.SpritePixelDTO;
import com.mygdx.service.network.dto.TimeDTO;
import com.mygdx.service.network.dto.WaitScreenDTO;
import com.mygdx.service.network.enumeration.NetworkGameRequestEnum;
import com.mygdx.service.network.enumeration.NetworkRequestEnum;

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
	private List<PlayerPixelDTO> playerPixelsDTOs = new ArrayList<>();
	private List<SpriteGridDTO> spriteGridDTOs = new ArrayList<>();
	private List<SpritePixelDTO> spritePixelDTOs = new ArrayList<>();
	private List<LightDTO> lightDTOs = new ArrayList<>();
	private List<ScoreDTO> scoresDTO = new ArrayList<>();
	private TimeDTO timeDTO;
	private boolean pause = false;
	private boolean menu = false;

	public ClientViewScreen(final MultiBombermanGame mbGame) {
		this.mbGame = mbGame;
		this.layout = new GlyphLayout();
		this.shapeRenderer = new ShapeRenderer();
		this.objectMapper = new ObjectMapper();
		this.last = NetworkRequestEnum.WAIT_SCREEN;
		this.initFont();
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
			mbGame.setScreen(new ClientConnexionScreen(mbGame));
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
		mbGame.setScreen(new ClientConnexionScreen(mbGame));
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
	 * --- RECEIVE STRING AND DECODE ---
	 **********************************************/
	public void receiveWaitScreen(String line) {
		try {
			Gdx.app.log(CLASS_NAME, "receiveWaitScreen");
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
			Gdx.app.log(CLASS_NAME, "receiveSkinScreen");
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
			Gdx.app.log(CLASS_NAME, "receiveRuleScreen");
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
			Gdx.app.log(CLASS_NAME, "receiveLevelScreen");
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
			Gdx.app.log(CLASS_NAME, "receiveLevelDef");
			levelDTO = this.objectMapper.readValue(line.substring(line.indexOf(':') + 1), LevelDTO.class);
		} catch (JsonParseException e) {
			Gdx.app.error(CLASS_NAME, JSON_PARSE_EXCEPTION + e.getMessage());
		} catch (JsonMappingException e) {
			Gdx.app.error(CLASS_NAME, JSON_MAPPING_EXCEPTION + e.getMessage());
		} catch (IOException e) {
			Gdx.app.error(CLASS_NAME, IO_EXCEPTION + e.getMessage());
		}
	}

	public void receiveGame(String line) {
		Gdx.app.log(CLASS_NAME, "receiveGame");
		last = NetworkRequestEnum.GAME_SCREEN;
		ByteBuffer bbd = ByteBuffer.wrap(Base64.getDecoder().decode(line.substring(line.indexOf(':') + 1)));
		while (bbd.position() < bbd.capacity()) {
			int reqIndex = (int) bbd.get();
			NetworkGameRequestEnum req = NetworkGameRequestEnum.values()[reqIndex];
			switch (req) {
			case DRAW_GRID:
				this.spriteGridDTOs.add(new SpriteGridDTO(readRequest(bbd, req)));
				break;
			case DRAW_PIXEL:
				this.spritePixelDTOs.add(new SpritePixelDTO(readRequest(bbd, req)));
				break;
			case DRAW_PLAYER:
			case DRAW_PLAYER_ON_LOUIS:
				this.playerPixelsDTOs.add(new PlayerPixelDTO(readRequest(bbd, req)));
				break;
			case LIGHT:
				this.lightDTOs.add(new LightDTO(readRequest(bbd, req)));
				break;
			case TIME:
				this.timeDTO = new TimeDTO(readRequest(bbd, req));
				break;
			case MENU:
				menu = true;
				break;
			case PAUSE:
				pause = true;
				break;
			case SCORE:
				this.scoresDTO.add(new ScoreDTO(readRequest(bbd, req)));
				break;
			case BRICK_POSITION:
				new BrickPositionDTO(readRequest(bbd, req));
				break;
			default:
				break;
			}
		}
	}

	private byte[] readRequest(ByteBuffer bbd, NetworkGameRequestEnum req) {
		int lengthToCopy;
		byte[] tmp;
		tmp = new byte[req.getRequestLenght() - 1];
		lengthToCopy = req.getRequestLenght() - 1;
		bbd.get(tmp, 0, lengthToCopy);
		return tmp;
	}

	public void receiveSound(String line) {
		Gdx.app.log("CLASS_NAME", "receive sound : " + line);
		SoundService.getInstance().decodeSoundCommand(line);
	}

	/**********************************************
	 * --- DRAW PART ---
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
	 * --- GAME PART ---
	 *********************************************************/

	private void drawGameScreen() {
		mbGame.getBatch().begin();
		mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.BACKGROUND, 2), 0, 0);
		mbGame.getBatch().end();
	}

}
