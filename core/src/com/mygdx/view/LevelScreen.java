package com.mygdx.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygdx.constante.Constante;
import com.mygdx.domain.screen.Cursor;
import com.mygdx.enumeration.ServerStateEnum;
import com.mygdx.enumeration.SoundEnum;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.Context;
import com.mygdx.service.MessageService;
import com.mygdx.service.SoundService;
import com.mygdx.service.SpriteService;
import com.mygdx.service.input_processor.MenuListener;
import com.mygdx.service.network.dto.LevelScreenDTO;
import com.mygdx.service.network.enumeration.NetworkRequestEnum;
import com.mygdx.service.network.server.ServerContext;

public class LevelScreen implements Screen, MenuListener {

	private static final String CLASS_NAME = "LevelScreen.class";
	private static final int LEVEL_PREVIEW_X = 60;
	private static final int LEVEL_PREVIEW_Y = 75;
	private static final int START_GRID_X = 260;
	private static final int START_GRID_Y = 100;
	private static final int ROW_GRID_SPACE = 15;
	private static final int COLUMN_GRID_SPACE = 150;
	private static final int START_BONUS_X = 260;
	private static final int START_BONUS_Y = 60;
	private static final int BONUS_SPACE = 25;

	private final MultiBombermanGame mbGame;
	private final Cursor cursor;
	private final GlyphLayout layout;
	private final ShapeRenderer shapeRenderer;
	private BitmapFont font;
	private int cursorPosition;
	private ObjectMapper objectMapper;

	public LevelScreen(final MultiBombermanGame mbGame) {
		this.mbGame = mbGame;
		this.cursor = new Cursor(198, 90);
		this.layout = new GlyphLayout();
		this.shapeRenderer = new ShapeRenderer();
		this.mbGame.getPlayerService().setMenuListener(this);
		cursorPosition = 0;
		this.objectMapper = new ObjectMapper();
		initFont();
		ServerContext.setCurrentServerScreen(ServerStateEnum.LEVEL_SCREEN);
		levelChange();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		mbGame.getScreenCamera().update();
		mbGame.getBatch().begin();
		mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.BACKGROUND, 1), 0, 0);
		mbGame.getBatch().end();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setProjectionMatrix(mbGame.getBatch().getProjectionMatrix());
		updateCursorPosition();

		shapeRenderer.setColor(0, 0, 0, 0.5f);
		shapeRenderer.rect(10, 10, 620, 210);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		mbGame.getBatch().begin();
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.levelScreen"));
		font.draw(mbGame.getBatch(), layout, (Constante.SCREEN_SIZE_X / 2) - (layout.width / 2), 210);

		layout.setText(font, MessageService.getInstance().getMessage("game.menu.levelScreen.bombe"));
		font.draw(mbGame.getBatch(), layout, START_GRID_X, START_GRID_Y + (ROW_GRID_SPACE * 6));
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.levelScreen.strenght"));
		font.draw(mbGame.getBatch(), layout, START_GRID_X, START_GRID_Y + (ROW_GRID_SPACE * 5));
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.levelScreen.levelGroup"));
		font.draw(mbGame.getBatch(), layout, START_GRID_X, START_GRID_Y + (ROW_GRID_SPACE * 4));
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.levelScreen.level"));
		font.draw(mbGame.getBatch(), layout, START_GRID_X, START_GRID_Y + (ROW_GRID_SPACE * 3));
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.levelScreen.description"));
		font.draw(mbGame.getBatch(), layout, START_GRID_X, START_GRID_Y + (ROW_GRID_SPACE * 1));
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.levelScreen.bonus"));
		font.draw(mbGame.getBatch(), layout, START_GRID_X, START_GRID_Y);

		layout.setText(font, Integer.toString(Context.getBombe()));
		font.draw(mbGame.getBatch(), layout, START_GRID_X + COLUMN_GRID_SPACE, START_GRID_Y + (ROW_GRID_SPACE * 6));
		layout.setText(font, Integer.toString(Context.getStrength()));
		font.draw(mbGame.getBatch(), layout, START_GRID_X + COLUMN_GRID_SPACE, START_GRID_Y + (ROW_GRID_SPACE * 5));
		layout.setText(font, mbGame.getLevelService().getLevelGroupName());
		font.draw(mbGame.getBatch(), layout, START_GRID_X + COLUMN_GRID_SPACE, START_GRID_Y + (ROW_GRID_SPACE * 4));
		layout.setText(font, mbGame.getLevelService().getLevelName());
		font.draw(mbGame.getBatch(), layout, START_GRID_X + COLUMN_GRID_SPACE, START_GRID_Y + (ROW_GRID_SPACE * 3));
		layout.setText(font, mbGame.getLevelService().getLevelDescription());
		font.draw(mbGame.getBatch(), layout, START_GRID_X + COLUMN_GRID_SPACE, START_GRID_Y + (ROW_GRID_SPACE * 1));
		for (int i = 0; i < Constante.MAX_BONUS; i++) {
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.BONUS, i),
					START_BONUS_X + (i * BONUS_SPACE), START_BONUS_Y);
			layout.setText(font, Integer.toString(Context.getBonus(i)));
			font.draw(mbGame.getBatch(), layout, (START_BONUS_X + (i * BONUS_SPACE) + 8) - (layout.width / 2),
					START_BONUS_Y - 5);

		}
		cursor.draw(mbGame.getBatch());
		mbGame.getBatch().draw(
				SpriteService.getInstance().getSprite(SpriteEnum.PREVIEW, Context.getLevel().getIndexPreview()),
				LEVEL_PREVIEW_X, LEVEL_PREVIEW_Y);
		mbGame.getBatch().end();
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
		shapeRenderer.dispose();
		font.dispose();
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

	public void updateCursorPosition() {
		switch (cursorPosition) {
		case 0:
			cursor.updateCursorPosition(START_GRID_X - 20, START_GRID_Y + (ROW_GRID_SPACE * 6) - 10);
			break;
		case 1:
			cursor.updateCursorPosition(START_GRID_X - 20, START_GRID_Y + (ROW_GRID_SPACE * 5) - 10);
			break;
		case 2:
			cursor.updateCursorPosition(START_GRID_X - 20, START_GRID_Y + (ROW_GRID_SPACE * 4) - 10);
			break;
		case 3:
			cursor.updateCursorPosition(START_GRID_X - 20, START_GRID_Y + (ROW_GRID_SPACE * 3) - 10);
			break;
		case 4:
			cursor.updateCursorPosition(START_BONUS_X + (0 * BONUS_SPACE), START_BONUS_Y - 40);
			break;
		case 5:
			cursor.updateCursorPosition(START_BONUS_X + (1 * BONUS_SPACE), START_BONUS_Y - 40);
			break;
		case 6:
			cursor.updateCursorPosition(START_BONUS_X + (2 * BONUS_SPACE), START_BONUS_Y - 40);
			break;
		case 7:
			cursor.updateCursorPosition(START_BONUS_X + (3 * BONUS_SPACE), START_BONUS_Y - 40);
			break;
		case 8:
			cursor.updateCursorPosition(START_BONUS_X + (4 * BONUS_SPACE), START_BONUS_Y - 40);
			break;
		case 9:
			cursor.updateCursorPosition(START_BONUS_X + (5 * BONUS_SPACE), START_BONUS_Y - 40);
			break;
		case 10:
			cursor.updateCursorPosition(START_BONUS_X + (6 * BONUS_SPACE), START_BONUS_Y - 40);
			break;
		case 11:
			cursor.updateCursorPosition(START_BONUS_X + (7 * BONUS_SPACE), START_BONUS_Y - 40);
			break;
		case 12:
			cursor.updateCursorPosition(START_BONUS_X + (8 * BONUS_SPACE), START_BONUS_Y - 40);
			break;
		case 13:
			cursor.updateCursorPosition(START_BONUS_X + (9 * BONUS_SPACE), START_BONUS_Y - 40);
			break;
		case 14:
			cursor.updateCursorPosition(START_BONUS_X + (10 * BONUS_SPACE), START_BONUS_Y - 40);
			break;
		case 15:
			cursor.updateCursorPosition(START_BONUS_X + (11 * BONUS_SPACE), START_BONUS_Y - 40);
			break;
		case 16:
			cursor.updateCursorPosition(START_BONUS_X + (12 * BONUS_SPACE), START_BONUS_Y - 40);
			break;
		case 17:
			cursor.updateCursorPosition(START_BONUS_X + (13 * BONUS_SPACE), START_BONUS_Y - 40);
			break;
		case 18:
			cursor.updateCursorPosition(START_BONUS_X + (14 * BONUS_SPACE), START_BONUS_Y - 40);
			break;
		default:
			break;
		}
	}

	@Override
	public void pressStart() {
		sendLevelDefinitions();
		mbGame.getScreen().dispose();
		mbGame.setScreen(new GameScreen(mbGame));
	}

	@Override
	public void pressSelect() {
		SoundService.getInstance().playSound(SoundEnum.CANCEL);
		mbGame.getScreen().dispose();
		mbGame.setScreen(new RulesScreen(mbGame));
	}

	@Override
	public void pressUp() {
		SoundService.getInstance().playSound(SoundEnum.BIP);
		switch (cursorPosition) {
		case 0:
		case 1:
		case 2:
		case 3:
			cursorPosition--;
			break;
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
		case 12:
		case 13:
		case 14:
		case 15:
		case 16:
		case 17:
		case 18:
			cursorPosition = 3;
			break;
		default:
			break;
		}
		if (cursorPosition < 0) {
			cursorPosition = 0;
		}
		levelChange();
	}

	@Override
	public void pressDown() {
		SoundService.getInstance().playSound(SoundEnum.BIP);
		switch (cursorPosition) {
		case 0:
		case 1:
		case 2:
		case 3:
			cursorPosition++;
			break;
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
		case 12:
		case 13:
		case 14:
		case 15:
		case 16:
		case 17:
		case 18:
		default:

			break;
		}
		levelChange();
	}

	@Override
	public void pressLeft() {
		SoundService.getInstance().playSound(SoundEnum.BIP);
		switch (cursorPosition) {
		case 0:
		case 1:
		case 2:
		case 3:
			break;
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
		case 12:
		case 13:
		case 14:
		case 15:
		case 16:
		case 17:
		case 18:
			cursorPosition--;
			break;
		default:
			break;
		}
		levelChange();
	}

	@Override
	public void pressRight() {
		SoundService.getInstance().playSound(SoundEnum.BIP);
		switch (cursorPosition) {
		case 0:
		case 1:
		case 2:
		case 3:
			break;
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
		case 12:
		case 13:
		case 14:
		case 15:
		case 16:
		case 17:
		case 18:
			cursorPosition++;
			break;
		default:
			break;
		}
		if (cursorPosition > 18) {
			cursorPosition = 18;
		}
		levelChange();
	}

	@Override
	public void pressA() {
		SoundService.getInstance().playSound(SoundEnum.BIP);
		switch (cursorPosition) {
		case 0:
			Context.incBombe();
			break;
		case 1:
			Context.incStrength();
			break;
		case 2:
			mbGame.getLevelService().nextLevelGroup();
			break;
		case 3:
			mbGame.getLevelService().nextLevel();
			break;
		case 4:
			Context.incBonus(0, 1);
			break;
		case 5:
			Context.incBonus(1, 1);
			break;
		case 6:
			Context.incBonus(2, 1);
			break;
		case 7:
			Context.incBonus(3, 1);
			break;
		case 8:
			Context.incBonus(4, 1);
			break;
		case 9:
			Context.incBonus(5, 1);
			break;
		case 10:
			Context.incBonus(6, 1);
			break;
		case 11:
			Context.incBonus(7, 1);
			break;
		case 12:
			Context.incBonus(8, 1);
			break;
		case 13:
			Context.incBonus(9, 1);
			break;
		case 14:
			Context.incBonus(10, 1);
			break;
		case 15:
			Context.incBonus(11, 1);
			break;
		case 16:
			Context.incBonus(12, 1);
			break;
		case 17:
			Context.incBonus(13, 1);
			break;
		case 18:
			Context.incBonus(14, 1);
			break;
		default:
			break;
		}
		levelChange();
	}

	@Override
	public void pressB() {
		SoundService.getInstance().playSound(SoundEnum.BIP);
		switch (cursorPosition) {
		case 0:
			Context.decBombe();
			break;
		case 1:
			Context.decStrength();
			break;
		case 2:
			mbGame.getLevelService().previousLevelGroup();
			break;
		case 3:
			mbGame.getLevelService().previousLevel();
			break;
		case 4:
			Context.decBonus(0, 1);
			break;
		case 5:
			Context.decBonus(1, 1);
			break;
		case 6:
			Context.decBonus(2, 1);
			break;
		case 7:
			Context.decBonus(3, 1);
			break;
		case 8:
			Context.decBonus(4, 1);
			break;
		case 9:
			Context.decBonus(5, 1);
			break;
		case 10:
			Context.decBonus(6, 1);
			break;
		case 11:
			Context.decBonus(7, 1);
			break;
		case 12:
			Context.decBonus(8, 1);
			break;
		case 13:
			Context.decBonus(9, 1);
			break;
		case 14:
			Context.decBonus(10, 1);
			break;
		case 15:
			Context.decBonus(11, 1);
			break;
		case 16:
			Context.decBonus(12, 1);
			break;
		case 17:
			Context.decBonus(13, 1);
			break;
		case 18:
			Context.decBonus(14, 1);
			break;
		default:
			break;
		}
		levelChange();
	}

	@Override
	public void pressX() {
		SoundService.getInstance().playSound(SoundEnum.BIP);
		switch (cursorPosition) {
		case 0:
		case 1:
		case 2:
		case 3:
			break;
		case 4:
			Context.incBonus(0, 10);
			break;
		case 5:
			Context.incBonus(1, 10);
			break;
		case 6:
			Context.incBonus(2, 10);
			break;
		case 7:
			Context.incBonus(3, 10);
			break;
		case 8:
			Context.incBonus(4, 10);
			break;
		case 9:
			Context.incBonus(5, 10);
			break;
		case 10:
			Context.incBonus(6, 10);
			break;
		case 11:
			Context.incBonus(7, 10);
			break;
		case 12:
			Context.incBonus(8, 10);
			break;
		case 13:
			Context.incBonus(9, 10);
			break;
		case 14:
			Context.incBonus(10, 10);
			break;
		case 15:
			Context.incBonus(11, 10);
			break;
		case 16:
			Context.incBonus(12, 10);
			break;
		case 17:
			Context.incBonus(13, 10);
			break;
		case 18:
			Context.incBonus(14, 10);
			break;
		default:
			break;
		}
		levelChange();
	}

	@Override
	public void pressY() {
		SoundService.getInstance().playSound(SoundEnum.BIP);
		switch (cursorPosition) {
		case 0:
		case 1:
		case 2:
		case 3:
			break;
		case 4:
			Context.decBonus(0, 10);
			break;
		case 5:
			Context.decBonus(1, 10);
			break;
		case 6:
			Context.decBonus(2, 10);
			break;
		case 7:
			Context.decBonus(3, 10);
			break;
		case 8:
			Context.decBonus(4, 10);
			break;
		case 9:
			Context.decBonus(5, 10);
			break;
		case 10:
			Context.decBonus(6, 10);
			break;
		case 11:
			Context.decBonus(7, 10);
			break;
		case 12:
			Context.decBonus(8, 10);
			break;
		case 13:
			Context.decBonus(9, 10);
			break;
		case 14:
			Context.decBonus(10, 10);
			break;
		case 15:
			Context.decBonus(11, 10);
			break;
		case 16:
			Context.decBonus(12, 10);
			break;
		case 17:
			Context.decBonus(13, 10);
			break;
		case 18:
			Context.decBonus(14, 10);
			break;
		default:
			break;
		}
		levelChange();
	}

	@Override
	public void pressL() {
		// unused method
	}

	@Override
	public void pressR() {
		// unused method
	}

	public void levelChange() {
		LevelScreenDTO dto = new LevelScreenDTO();
		dto.setBombe(Context.getBombe());
		dto.setStrenght(Context.getStrength());
		dto.setGroup(mbGame.getLevelService().getLevelGroupName());
		dto.setName(mbGame.getLevelService().getLevelName());
		dto.setDescription(mbGame.getLevelService().getLevelDescription());
		dto.setIndexPreview(Context.getLevel().getIndexPreview());
		for (int i = 0; i < Constante.MAX_BONUS; i++) {
			dto.setBonus(i, Context.getBonus(i));
		}
		try {
			String request = 
					NetworkRequestEnum.LEVEL_SCREEN.name() + ":" + this.objectMapper.writeValueAsString(dto);
			ServerContext.setLevelScreenRequestBuffer(request);
			this.mbGame.getNetworkService().sendToClient(request);
		} catch (JsonProcessingException e) {
			Gdx.app.error(CLASS_NAME, "error send definitions to client");
		}
	}

	public void sendLevelDefinitions() {
		try {
			String request = "levelDefinition:" + this.objectMapper.writeValueAsString(Context.getLevel());
			ServerContext.setLevelDefinitionBuffer(request);
			this.mbGame.getNetworkService()
					.sendToClient(request);
		} catch (JsonProcessingException e) {
			Gdx.app.error(CLASS_NAME, "error send definitions to client");
		}
	}
}
