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
import com.mygdx.constante.Constante;
import com.mygdx.domain.screen.Cursor;
import com.mygdx.enumeration.SoundEnum;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.Context;
import com.mygdx.service.MessageService;
import com.mygdx.service.SoundService;
import com.mygdx.service.SpriteService;
import com.mygdx.service.input_processor.MenuListener;

public class RulesScreen implements Screen, MenuListener {

	private static final int COLUMN_START_X = 200;
	private static final int COLUMN_SPACE = 200;
	private static final int ROW_START_Y = 120;
	private static final int ROW_SPACE = 20;

	private final MultiBombermanGame mbGame;
	private final Cursor cursor;
	private final GlyphLayout layout;
	private final ShapeRenderer shapeRenderer;
	private int cursorPosition;
	private BitmapFont font;

	public RulesScreen(final MultiBombermanGame mbGame) {
		this.mbGame = mbGame;
		this.cursor = new Cursor(198, 90);
		this.layout = new GlyphLayout();
		this.shapeRenderer = new ShapeRenderer();
		this.mbGame.getPlayerService().setMenuListener(this);
		this.cursorPosition = 0;
		initFont();
		rulesChange();
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

		shapeRenderer.setColor(0, 0, 0, 0.5f);
		shapeRenderer.rect(10, 10, 620, 210);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		mbGame.getBatch().begin();
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.ruleScreen"));
		font.draw(mbGame.getBatch(), layout, (Constante.SCREEN_SIZE_X / 2) - (layout.width / 2), 210);

		layout.setText(font, MessageService.getInstance().getMessage("game.menu.ruleScreen.suddenDeath"));
		font.draw(mbGame.getBatch(), layout, COLUMN_START_X, ROW_START_Y + (ROW_SPACE * 3));
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.ruleScreen.badBomber"));
		font.draw(mbGame.getBatch(), layout, COLUMN_START_X, ROW_START_Y + (ROW_SPACE * 2));
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.ruleScreen.iaLevel"));
		font.draw(mbGame.getBatch(), layout, COLUMN_START_X, ROW_START_Y + (ROW_SPACE * 1));
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.ruleScreen.time"));
		font.draw(mbGame.getBatch(), layout, COLUMN_START_X, ROW_START_Y + (ROW_SPACE * 0));

		layout.setText(font, Context.isSuddenDeath() ? MessageService.getInstance().getMessage("game.menu.yes")
				: MessageService.getInstance().getMessage("game.menu.no"));
		font.draw(mbGame.getBatch(), layout, COLUMN_START_X + COLUMN_SPACE, ROW_START_Y + (ROW_SPACE * 3));
		layout.setText(font, Context.isBadBomber() ? MessageService.getInstance().getMessage("game.menu.yes")
				: MessageService.getInstance().getMessage("game.menu.no"));
		font.draw(mbGame.getBatch(), layout, COLUMN_START_X + COLUMN_SPACE, ROW_START_Y + (ROW_SPACE * 2));
		layout.setText(font, Integer.toString(Context.getIaLevel()));
		font.draw(mbGame.getBatch(), layout, COLUMN_START_X + COLUMN_SPACE, ROW_START_Y + (ROW_SPACE * 1));
		layout.setText(font, MessageService.getInstance().getMessage(Context.getTime().getKey()));
		font.draw(mbGame.getBatch(), layout, COLUMN_START_X + COLUMN_SPACE, ROW_START_Y + (ROW_SPACE * 0));

		this.updateCursorPosition();

		cursor.draw(mbGame.getBatch());
		mbGame.getBatch().end();
	}

	private void updateCursorPosition() {
		switch (cursorPosition) {
		case 0:
			this.cursor.updateCursorPosition(COLUMN_START_X - 20, ROW_START_Y + (ROW_SPACE * 3) - 10);
			break;
		case 1:
			this.cursor.updateCursorPosition(COLUMN_START_X - 20, ROW_START_Y + (ROW_SPACE * 2) - 10);
			break;
		case 2:
			this.cursor.updateCursorPosition(COLUMN_START_X - 20, ROW_START_Y + (ROW_SPACE * 1) - 10);
			break;
		case 3:
		default:
			this.cursor.updateCursorPosition(COLUMN_START_X - 20, ROW_START_Y + (ROW_SPACE * 0) - 10);
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

	@Override
	public void pressStart() {
		SoundService.getInstance().playSound(SoundEnum.VALIDE);
		mbGame.getScreen().dispose();
		mbGame.setScreen(new LevelScreen(mbGame));
	}

	@Override
	public void pressSelect() {
		SoundService.getInstance().playSound(SoundEnum.CANCEL);
		mbGame.getScreen().dispose();
		mbGame.setScreen(new SkinScreen(mbGame));
	}

	@Override
	public void pressUp() {
		SoundService.getInstance().playSound(SoundEnum.BIP);
		cursorPosition--;
		if (cursorPosition < 0) {
			cursorPosition = 0;
		}
		rulesChange();
	}

	@Override
	public void pressDown() {
		SoundService.getInstance().playSound(SoundEnum.BIP);
		cursorPosition++;
		if (cursorPosition > 3) {
			cursorPosition = 3;
		}
		rulesChange();
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
	public void pressX() {
		// unused method
	}

	@Override
	public void pressY() {
		// unused method
	}

	@Override
	public void pressA() {
		SoundService.getInstance().playSound(SoundEnum.BIP);
		switch (cursorPosition) {
		case 0:
			Context.toogleSuddenDeath();
			break;
		case 1:
			Context.toogleBadBomber();
			break;
		case 2:
			Context.decIaLevel();
			break;
		case 3:
		default:
			Context.decTime();
			break;
		}
		rulesChange();
	}

	@Override
	public void pressB() {
		SoundService.getInstance().playSound(SoundEnum.BIP);
		switch (cursorPosition) {
		case 0:
			Context.toogleSuddenDeath();
			break;
		case 1:
			Context.toogleBadBomber();
			break;
		case 2:
			Context.incIaLevel();
			break;
		case 3:
		default:
			Context.incTime();
			break;
		}
		rulesChange();
	}

	@Override
	public void pressL() {
		// unused method

	}

	@Override
	public void pressR() {
		// unused method

	}

	public void rulesChange() {
		Gdx.app.log("RULE SCREEN", "change to send to client");
	}
}
