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

public class ServerParamScreen implements Screen {

	private static final int COLUMN1 = 40;
	private static final int COLUMN2 = 310;
	private static final int COLUMN3 = 340;
	
	private final MultiBombermanGame game;
	private final Cursor cursor;
	private final GlyphLayout layout;
	private final ShapeRenderer shapeRenderer;
	private BitmapFont font;
	private int cursorPosition = 0;

	public ServerParamScreen(final MultiBombermanGame game) {
		this.game = game;
		this.cursor = new Cursor(COLUMN2, 180);
		this.layout = new GlyphLayout();
		this.shapeRenderer = new ShapeRenderer();
		initFont();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.getScreenCamera().update();
		treatInput();
		game.getBatch().begin();
		game.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.BACKGROUND, 1), 0, 0);
		game.getBatch().end();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setProjectionMatrix(game.getBatch().getProjectionMatrix());
		shapeRenderer.setColor(0, 0, 0, 0.5f);
		shapeRenderer.rect(10, 10, 620, 210);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		game.getBatch().begin();
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.network.server.params"));
		font.draw(game.getBatch(), layout, (Constante.SCREEN_SIZE_X / 2) - (layout.width / 2), 210);
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.network.server.player.external"));
		font.draw(game.getBatch(), layout, COLUMN1, 190);
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.network.server.player.local"));
		font.draw(game.getBatch(), layout, COLUMN1, 170);
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.network.server.port"));
		font.draw(game.getBatch(), layout, COLUMN1, 150);
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.network.server.upnp"));
		font.draw(game.getBatch(), layout, COLUMN1, 130);
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.network.server.ip.external"));
		font.draw(game.getBatch(), layout, COLUMN1, 110);
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.network.server.hostname"));
		font.draw(game.getBatch(), layout, COLUMN1, 90);
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.network.server.ip.internet"));
		font.draw(game.getBatch(), layout, COLUMN1, 70);

		layout.setText(font, Integer.toString(Context.externalPlayer));
		font.draw(game.getBatch(), layout, COLUMN3, 190);
		layout.setText(font, Integer.toString(Context.localPlayer));
		font.draw(game.getBatch(), layout, COLUMN3, 170);
		layout.setText(font, Integer.toString(Context.port));
		font.draw(game.getBatch(), layout, COLUMN3, 150);
		layout.setText(font, Context.useUpnp ? MessageService.getInstance().getMessage("game.menu.yes")
				: MessageService.getInstance().getMessage("game.menu.no"));
		font.draw(game.getBatch(), layout, COLUMN3, 130);
		layout.setText(font, game.getNetworkService().getExternalIp());
		font.draw(game.getBatch(), layout, COLUMN3, 110);
		layout.setText(font, game.getNetworkService().getHostName());
		font.draw(game.getBatch(), layout, COLUMN3, 90);
		layout.setText(font, game.getNetworkService().getInternetIp());
		font.draw(game.getBatch(), layout, COLUMN3, 70);
		switch (cursorPosition) {
		case 0:
			cursor.updateCursorPosition(COLUMN2, 180);
			break;
		case 1:
			cursor.updateCursorPosition(COLUMN2, 160);
			break;
		case 2:
			cursor.updateCursorPosition(COLUMN2, 140);
			break;
		case 3:
			cursor.updateCursorPosition(COLUMN2, 120);
			break;
		}

		cursor.draw(game.getBatch());
		game.getBatch().end();
	}

	private void treatInput() {
		if (game.getMenuInputProcessor().pressNext()) {
			if (game.getNetworkService().initServer()) {
				game.getScreen().dispose();
				game.setScreen(new WaitConnexionScreen(game));
			}
		}
		if (game.getMenuInputProcessor().pressPrevious()) {
			game.getScreen().dispose();
			game.setScreen(new MainScreen(game));
		}
		if (game.getMenuInputProcessor().pressUp()) {
			cursorPosition--;
			if (cursorPosition < 0) {
				cursorPosition = 0;
			}
		}
		if (game.getMenuInputProcessor().pressDown()) {
			cursorPosition++;
			if (cursorPosition > 3) {
				cursorPosition = 3;
			}
		}
		if (game.getMenuInputProcessor().pressLeft()) {
			switch (cursorPosition) {
			case 0:
				Context.externalPlayer--;
				if (Context.externalPlayer < 0) {
					Context.externalPlayer = 0;
				}
				break;
			case 1:
				Context.localPlayer--;
				if (Context.localPlayer < 0) {
					Context.localPlayer = 0;
				}
				break;
			case 2:
				Context.port--;
				if (Context.port < 0) {
					Context.port = 0;
				}
				break;
			case 3:
				if (Context.useUpnp) {
					Context.useUpnp = false;
				} else {
					Context.useUpnp = true;
				}
				break;
			}
		}
		if (game.getMenuInputProcessor().pressRight()) {
			switch (cursorPosition) {
			case 0:
				if (Context.localPlayer + Context.externalPlayer < 16) {
					Context.externalPlayer++;
					if (Context.externalPlayer > 16) {
						Context.externalPlayer = 16;
					}
				}
				break;
			case 1:
				if (Context.localPlayer + Context.externalPlayer < 16) {
					Context.localPlayer++;
					if (Context.localPlayer > 16) {
						Context.localPlayer = 16;
					}
				}
				break;
			case 2:
				Context.port++;
				if (Context.port > 65535) {
					Context.port = 65535;
				}
				break;
			case 3:
				if (Context.useUpnp) {
					Context.useUpnp = false;
				} else {
					Context.useUpnp = true;
				}
				break;
			}
		}

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

}
