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
import com.mygdx.service.MessageService;
import com.mygdx.service.SpriteService;
import com.mygdx.service.input_processor.MenuListener;

public class ClientConnexionScreen implements Screen, MenuListener {

	private final MultiBombermanGame game;
	private final Cursor cursor;
	private final GlyphLayout layout;
	private final ShapeRenderer shapeRenderer;
	private BitmapFont font;
	private BitmapFont largeFont;
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
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.clientConnexionScreen"));
		font.draw(game.getBatch(), layout, (Constante.SCREEN_SIZE_X / 2) - (layout.width / 2), 210);

		layout.setText(font, MessageService.getInstance().getMessage("game.menu.clientConnexionScreen.player"));
		font.draw(game.getBatch(), layout, 200, 190);
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.clientConnexionScreen.port"));
		font.draw(game.getBatch(), layout, 200, 170);

		layout.setText(font, MessageService.getInstance().getMessage("game.menu.clientConnexionScreen.ip"));
		font.draw(game.getBatch(), layout, 200, 150);

		layout.setText(font, "" + Context.getLocalPlayer());
		font.draw(game.getBatch(), layout, 400, 190);

		layout.setText(font, "" + Context.getPort());
		font.draw(game.getBatch(), layout, 400, 170);

		for (int i = 0; i < 4; i++) {
			for (int j = 2; j > -1; j--) {
				layout.setText(largeFont, "" + Context.extractIp(i, j));
				largeFont.draw(game.getBatch(), layout, 200 + (i * 80) - (j * 20), 110);
			}
		}
		for (int i = 0; i < 3; i++) {
			layout.setText(largeFont, ".");
			largeFont.draw(game.getBatch(), layout, 225 + (i * 80), 110);
		}

		if (game.getNetworkService().getLastClientError() != null) {
			layout.setText(font, MessageService.getInstance()
					.getMessage("game.menu.clientConnexionScreen." + game.getNetworkService().getLastClientError()));
			font.draw(game.getBatch(), layout, (Constante.SCREEN_SIZE_X / 2) - (layout.width / 2), 50);
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
		shapeRenderer.dispose();
		font.dispose();
		largeFont.dispose();
	}

	public void initFont() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/font_gbboot.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 14;
		parameter.borderWidth = 0f;
		parameter.borderColor = new Color(255, 0, 0, 255);
		parameter.color = new Color(255, 0, 0, 255);
		font = generator.generateFont(parameter);
		parameter.size = 24;
		parameter.borderWidth = 0f;
		parameter.borderColor = new Color(255, 0, 0, 255);
		parameter.color = new Color(255, 0, 0, 255);
		largeFont = generator.generateFont(parameter);
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
		if (cursorPos > 13) {
			cursorPos = 13;
		}
	}

	private void updateCursor() {
		switch (cursorPos) {
		case 0:
			cursor.updateCursorPosition(370, 180);
			break;
		case 1:
			cursor.updateCursorPosition(370, 160);
			break;
		case 2:
			cursor.updateCursorPosition(160, 80);
			break;
		case 3:
			cursor.updateCursorPosition(180, 80);
			break;
		case 4:
			cursor.updateCursorPosition(200, 80);
			break;
		case 5:
			cursor.updateCursorPosition(240, 80);
			break;
		case 6:
			cursor.updateCursorPosition(260, 80);
			break;
		case 7:
			cursor.updateCursorPosition(280, 80);
			break;
		case 8:
			cursor.updateCursorPosition(320, 80);
			break;
		case 9:
			cursor.updateCursorPosition(340, 80);
			break;
		case 10:
			cursor.updateCursorPosition(360, 80);
			break;
		case 11:
			cursor.updateCursorPosition(400, 80);
			break;
		case 12:
			cursor.updateCursorPosition(420, 80);
			break;
		case 13:
			cursor.updateCursorPosition(440, 80);
			break;
		}
	}
}
