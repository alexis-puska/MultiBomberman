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

public class ServerParamScreen implements Screen, MenuListener {

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
		this.game.getPlayerService().setMenuListener(this);
		initFont();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.getScreenCamera().update();
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

		layout.setText(font, Integer.toString(Context.getExternalPlayer()));
		font.draw(game.getBatch(), layout, COLUMN3, 190);
		layout.setText(font, Integer.toString(Context.getLocalPlayer()));
		font.draw(game.getBatch(), layout, COLUMN3, 170);
		layout.setText(font, Integer.toString(Context.getPort()));
		font.draw(game.getBatch(), layout, COLUMN3, 150);
		layout.setText(font, Context.isUseUpnp() ? MessageService.getInstance().getMessage("game.menu.yes")
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

	@Override
	public void pressStart() {
		if (game.getNetworkService().initServer()) {
			game.getScreen().dispose();
			game.setScreen(new WaitConnexionScreen(game));
		}
	}

	@Override
	public void pressSelect() {
		game.getScreen().dispose();
		game.setScreen(new PlayerTypeScreen(game));
	}

	@Override
	public void pressValide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pressUp() {
		cursorPosition--;
		if (cursorPosition < 0) {
			cursorPosition = 0;
		}
	}

	@Override
	public void pressDown() {
		cursorPosition++;
		if (cursorPosition > 3) {
			cursorPosition = 3;
		}
	}

	@Override
	public void pressLeft() {
		switch (cursorPosition) {
		case 0:
			Context.setExternalPlayer(Context.getExternalPlayer()-1);
			if (Context.getExternalPlayer() < 0) {
				Context.setExternalPlayer(0);
			}
			break;
		case 1:
			Context.setLocalPlayer(Context.getLocalPlayer()-1);
			if (Context.getLocalPlayer() < 0) {
				Context.setLocalPlayer(0);
			}
			break;
		case 2:
			Context.setPort(Context.getPort()-1);
			if (Context.getPort() < 0) {
				Context.setPort(0);
			}
			break;
		case 3:
			if (Context.isUseUpnp()) {
				Context.setUseUpnp(false);
			} else {
				Context.setUseUpnp(true);
			}
			break;
		}
	}

	@Override
	public void pressRight() {
		switch (cursorPosition) {
		case 0:
			if (Context.getLocalPlayer() + Context.getExternalPlayer() < 16) {
				Context.setExternalPlayer(Context.getExternalPlayer()+1);
				if (Context.getExternalPlayer() > 16) {
					Context.setExternalPlayer(16);
				}
			}
			break;
		case 1:
			if (Context.getLocalPlayer() + Context.getExternalPlayer() < 16) {
				Context.setLocalPlayer(Context.getLocalPlayer()+1);
				if (Context.getLocalPlayer() > 16) {
					Context.setLocalPlayer(16);
				}
			}
			break;
		case 2:
			Context.setPort(Context.getPort() + 1);
			if (Context.getPort() > 65535) {
				Context.setPort(65535);
			}
			break;
		case 3:
			if (Context.isUseUpnp()) {
				Context.setUseUpnp(false);
			} else {
				Context.setUseUpnp(true);
			}
			break;
		}

	}

}
