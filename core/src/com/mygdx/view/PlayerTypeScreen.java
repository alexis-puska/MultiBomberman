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
import com.mygdx.domain.Cursor;
import com.mygdx.enumeration.GameModeEnum;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.Context;
import com.mygdx.service.MessageService;
import com.mygdx.service.SpriteService;
import com.mygdx.service.input_processor.MenuListener;

public class PlayerTypeScreen implements Screen, MenuListener {

	private static final int START_X = 100;
	private static final int START_Y = 170;
	private static final int NB_COL = 4;
	private static final int COL_SIZE = 120;
	private static final int ROW_SIZE = 30;

	private final MultiBombermanGame mbGame;
	private final Cursor cursor;
	private final GlyphLayout layout;
	private final ShapeRenderer shapeRenderer;
	private BitmapFont fontRed;
	private BitmapFont fontGold;
	private BitmapFont fontGreen;
	private BitmapFont fontBlue;
	private int cursorPosition;

	public PlayerTypeScreen(final MultiBombermanGame mbGame) {
		this.mbGame = mbGame;
		this.cursor = new Cursor(198, 90);
		this.layout = new GlyphLayout();
		this.shapeRenderer = new ShapeRenderer();
		this.cursorPosition = 0;
		this.mbGame.getPlayerService().setMenuListener(this);
		initFont();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		mbGame.getScreenCamera().update();
		updateCursorPosition();
		mbGame.getBatch().begin();
		mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.BACKGROUND, 1), 0, 0);
		mbGame.getBatch().end();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.setProjectionMatrix(mbGame.getBatch().getProjectionMatrix());

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0, 0, 0, 0.5f);
		shapeRenderer.rect(10, 10, 620, 210);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		mbGame.getBatch().begin();
		layout.setText(fontRed, MessageService.getInstance().getMessage("game.menu.player.configuration"));
		fontRed.draw(mbGame.getBatch(), layout, (Constante.SCREEN_SIZE_X / 2) - (layout.width / 2), 210);
		for (int j = 0; j < 4; j++) {
			for (int i = 0; i < 4; i++) {
				int pos = i + j * 4;
				layout.setText(fontRed, MessageService.getInstance().getMessage("game.menu.player") + pos + " : ");
				fontRed.draw(mbGame.getBatch(), layout, START_X + (i * COL_SIZE), START_Y - (j * ROW_SIZE));
			}
		}

		for (int j = 0; j < 4; j++) {
			for (int i = 0; i < 4; i++) {
				int pos = i + j * 4;
				switch (mbGame.getPlayerService().getPlayerType(pos)) {
				case CPU:
					layout.setText(fontGreen, MessageService.getInstance()
							.getMessage(mbGame.getPlayerService().getPlayerType(pos).getKey()));
					fontGreen.draw(mbGame.getBatch(), layout, START_X + (i * COL_SIZE) + 65, START_Y - (j * ROW_SIZE));
					break;
				case HUMAN:
					layout.setText(fontBlue, MessageService.getInstance()
							.getMessage(mbGame.getPlayerService().getPlayerType(pos).getKey()));
					fontBlue.draw(mbGame.getBatch(), layout, START_X + (i * COL_SIZE) + 65, START_Y - (j * ROW_SIZE));
					break;
				case NET:
					layout.setText(fontGold, MessageService.getInstance()
							.getMessage(mbGame.getPlayerService().getPlayerType(pos).getKey()));
					fontGold.draw(mbGame.getBatch(), layout, START_X + (i * COL_SIZE) + 65, START_Y - (j * ROW_SIZE));
					break;
				case NONE:
				default:
					layout.setText(fontRed, MessageService.getInstance()
							.getMessage(mbGame.getPlayerService().getPlayerType(pos).getKey()));
					fontRed.draw(mbGame.getBatch(), layout, START_X + (i * COL_SIZE) + 65, START_Y - (j * ROW_SIZE));
					break;
				}
			}
		}
		cursor.draw(mbGame.getBatch());
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
		fontRed.dispose();
		fontGreen.dispose();
		fontGold.dispose();
		fontBlue.dispose();
	}

	public void initFont() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/font_gbboot.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 14;
		parameter.borderWidth = 0f;
		parameter.borderColor = new Color(255, 0, 0, 255);
		parameter.color = new Color(255, 0, 0, 255);
		fontRed = generator.generateFont(parameter);
		parameter.borderColor = new Color(0.4f, 1, 1, 1);
		parameter.color = new Color(0.4f, 255, 255, 255);
		fontBlue = generator.generateFont(parameter);
		parameter.borderColor = new Color(0, 255, 0, 255);
		parameter.color = new Color(0, 255, 0, 255);
		fontGreen = generator.generateFont(parameter);
		parameter.borderColor = new Color(255, 255, 0, 255);
		parameter.color = new Color(255, 255, 0, 255);
		fontGold = generator.generateFont(parameter);
		generator.dispose();
	}

	private void updateCursorPosition() {
		int col = cursorPosition % NB_COL;
		int row = cursorPosition / NB_COL;
		cursor.updateCursorPosition((START_X + (col * COL_SIZE)) - 20, (START_Y - (row * ROW_SIZE)) - 15);
	}

	@Override
	public void pressStart() {
		if (Context.getGameMode() == GameModeEnum.SERVER) {
			mbGame.getScreen().dispose();
			mbGame.setScreen(new ServerParamScreen(mbGame));
		} else if (Context.getGameMode() == GameModeEnum.LOCAL) {
			//mbGame.getPlayerService().validePlayerType();
			mbGame.getScreen().dispose();
			mbGame.setScreen(new SkinScreen(mbGame));
		}
	}

	@Override
	public void pressSelect() {
		mbGame.getScreen().dispose();
		mbGame.setScreen(new MainScreen(mbGame));
	}

	@Override
	public void pressA() {
		mbGame.getPlayerService().incPlayerType(cursorPosition);
	}
	
	@Override
	public void pressB() {
		mbGame.getPlayerService().decPlayerType(cursorPosition);
	}

	@Override
	public void pressUp() {
		cursorPosition -= 4;
		if (cursorPosition < 0) {
			cursorPosition = 16 + cursorPosition;
		}
	}

	@Override
	public void pressDown() {
		cursorPosition += 4;
		if (cursorPosition > 15) {
			cursorPosition = cursorPosition - 16;
		}
	}

	@Override
	public void pressLeft() {
		cursorPosition--;
		if (cursorPosition < 0) {
			cursorPosition = 15;
		}
	}

	@Override
	public void pressRight() {
		cursorPosition++;
		if (cursorPosition > 15) {
			cursorPosition = 0;
		}
	}

	@Override
	public void pressX() {
		//unused method
		
	}

	@Override
	public void pressY() {
		//unused method
		
	}

	@Override
	public void pressL() {
		//unused method
		
	}

	@Override
	public void pressR() {
		//unused method
		
	}
}
