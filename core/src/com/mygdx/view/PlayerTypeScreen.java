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
import com.mygdx.game.MultiBombermanGame;
import com.mygdx.service.Context;
import com.mygdx.service.MessageService;
import com.mygdx.service.SpriteService;
import com.mygdx.service.input_processor.MenuListener;

public class PlayerTypeScreen implements Screen, MenuListener {

	private final static int START_X = 100;
	private final static int START_Y = 170;
	private final static int NB_COL = 4;
	private final static int COL_SIZE = 120;
	private final static int ROW_SIZE = 30;

	private final MultiBombermanGame game;
	private final Cursor cursor;
	private final GlyphLayout layout;
	private final ShapeRenderer shapeRenderer;
	private BitmapFont font;
	private int cursorPosition;

	public PlayerTypeScreen(final MultiBombermanGame game) {
		this.game = game;
		this.cursor = new Cursor(198, 90);
		this.layout = new GlyphLayout();
		this.shapeRenderer = new ShapeRenderer();
		this.cursorPosition = 0;
		this.game.getMenuInputProcessor().changeMenuListeners(this);
		this.game.getControllerAdapter().changeMenuListeners(this);
		initFont();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.getScreenCamera().update();
		updateCursorPosition();
		game.getBatch().begin();
		game.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.BACKGROUND, 1), 0, 0);
		game.getBatch().end();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.setProjectionMatrix(game.getBatch().getProjectionMatrix());

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0, 0, 0, 0.5f);
		shapeRenderer.rect(10, 10, 620, 210);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		game.getBatch().begin();
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.player.configuration"));
		font.draw(game.getBatch(), layout, (Constante.SCREEN_SIZE_X / 2) - (layout.width / 2), 210);
		for (int j = 0; j < 4; j++) {
			for (int i = 0; i < 4; i++) {
				int pos = i + j * 4;
				layout.setText(font, MessageService.getInstance().getMessage("game.menu.player") + pos + " : "
						+ game.getPlayerService().getPlayerType(pos));
				font.draw(game.getBatch(), layout, START_X + (i * COL_SIZE), START_Y - (j * ROW_SIZE));
			}
		}

		cursor.draw(game.getBatch());
		game.getBatch().end();
	}

	@Override
	public void show() {
		// unused method
	}

	@Override
	public void resize(int width, int height) {
		game.getViewport().update(width, height, true);
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

	private void updateCursorPosition() {
		int col = cursorPosition % NB_COL;
		int row = cursorPosition / NB_COL;
		cursor.updateCursorPosition((START_X + (col * COL_SIZE)) - 20, (START_Y - (row * ROW_SIZE)) - 15);
	}

	@Override
	public void pressStart() {
		game.getScreen().dispose();
		game.setScreen(new SkinScreen(game));
	}

	@Override
	public void pressSelect() {
		if (Context.gameMode == GameModeEnum.SERVER) {
			game.getNetworkService().stopServer();
			game.setScreen(new ServerParamScreen(game));
		} else if (Context.gameMode == GameModeEnum.LOCAL) {
			game.setScreen(new MainScreen(game));
		}
	}

	@Override
	public void pressValide() {
		game.getPlayerService().incPlayerType(cursorPosition);
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
}
