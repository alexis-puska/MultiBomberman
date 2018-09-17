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
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.game.MultiBombermanGame;
import com.mygdx.service.Context;
import com.mygdx.service.SpriteService;
import com.mygdx.service.input_processor.MenuListener;

public class ClientConnexionScreen implements Screen, MenuListener {

	private final MultiBombermanGame game;
	private final Cursor cursor;
	private final GlyphLayout layout;
	private final ShapeRenderer shapeRenderer;
	private BitmapFont font;
	private int cursorPos;

	public ClientConnexionScreen(final MultiBombermanGame game) {
		this.game = game;
		this.cursorPos = 0;
		this.cursor = new Cursor(198, 90);
		this.layout = new GlyphLayout();
		this.shapeRenderer = new ShapeRenderer();
		this.game.getPlayerService().setMenuListener(this);
		initFont();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		updateCursor();
		game.getScreenCamera().update();
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
		layout.setText(font, "connexion to server");
		font.draw(game.getBatch(), layout, (Constante.SCREEN_SIZE_X / 2) - (layout.width / 2), 210);
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
		game.getPlayerService().initControllerMap();
		if (game.getNetworkService().connectToServer(Context.getPort(), Context.getIp())) {
			game.getScreen().dispose();
			game.setScreen(new ClientViewScreen(game));
		}
	}

	@Override
	public void pressSelect() {
		game.getScreen().dispose();
		game.setScreen(new MainScreen(game));
	}

	@Override
	public void pressValide() {
		game.getScreen().dispose();
		game.setScreen(new WaitConnexionScreen(game));
	}

	@Override
	public void pressUp() {
		switch (cursorPos) {
		case 0:
			break;
		case 1:
			cursorPos--;
			break;
		case 2:
			Context.incIp(0, 100);
			break;
		case 3:
			Context.incIp(0, 10);
			break;
		case 4:
			Context.incIp(0, 1);
			break;
		case 5:
			Context.incIp(1, 100);
			break;
		case 6:
			Context.incIp(1, 10);
			break;
		case 7:
			Context.incIp(1, 1);
			break;
		case 8:
			Context.incIp(2, 100);
			break;
		case 9:
			Context.incIp(2, 10);
			break;
		case 10:
			Context.incIp(2, 1);
			break;
		case 11:
			Context.incIp(3, 100);
			break;
		case 12:
			Context.incIp(3, 10);
			break;
		case 13:
			Context.incIp(3, 1);
			break;
		}
	}

	@Override
	public void pressDown() {
		switch (cursorPos) {
		case 0:
			cursorPos++;
			break;
		case 1:
			cursorPos++;
			break;
		case 2:
			Context.decIp(0, 100);
			break;
		case 3:
			Context.decIp(0, 10);
			break;
		case 4:
			Context.decIp(0, 1);
			break;
		case 5:
			Context.decIp(1, 100);
			break;
		case 6:
			Context.decIp(1, 10);
			break;
		case 7:
			Context.decIp(1, 1);
			break;
		case 8:
			Context.decIp(2, 100);
			break;
		case 9:
			Context.decIp(2, 10);
			break;
		case 10:
			Context.decIp(2, 1);
			break;
		case 11:
			Context.decIp(3, 100);
			break;
		case 12:
			Context.decIp(3, 10);
			break;
		case 13:
			Context.decIp(3, 1);
			break;
		}
	}

	@Override
	public void pressLeft() {
		switch (cursorPos) {
		case 0:
			Context.decLocalPlayer();
			break;
		case 1:
			Context.decPort();
			break;
		case 2:
		case 3:
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
			cursorPos--;
			break;
		}
	}

	@Override
	public void pressRight() {
		switch (cursorPos) {
		case 0:
			Context.incLocalPlayer();
			break;
		case 1:
			Context.incPort();
			break;
		case 2:
		case 3:
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
			cursorPos++;
			break;
		}
	}

	private void updateCursor() {
		switch (cursorPos) {
		case 0:
			cursor.updateCursorPosition(100, 100);
			break;
		case 1:
			cursor.updateCursorPosition(100, 100);
			break;
		case 2:
			cursor.updateCursorPosition(100, 100);
			break;
		case 3:
			cursor.updateCursorPosition(100, 100);
			break;
		case 4:
			cursor.updateCursorPosition(100, 100);
			break;
		case 5:
			cursor.updateCursorPosition(100, 100);
			break;
		case 6:
			cursor.updateCursorPosition(100, 100);
			break;
		case 7:
			cursor.updateCursorPosition(100, 100);
			break;
		case 8:
			cursor.updateCursorPosition(100, 100);
			break;
		case 9:
			cursor.updateCursorPosition(100, 100);
			break;
		case 10:
			cursor.updateCursorPosition(100, 100);
			break;
		case 11:
			cursor.updateCursorPosition(100, 100);
			break;
		case 12:
			cursor.updateCursorPosition(100, 100);
			break;
		case 13:
			cursor.updateCursorPosition(100, 100);
			break;
		}
	}
}
