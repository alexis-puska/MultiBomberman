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

public class MainScreen implements Screen {

	private final MultiBombermanGame game;
	private final Cursor cursor;
	private final GlyphLayout layout;
	private final ShapeRenderer shapeRenderer;
	private BitmapFont font;

	public MainScreen(final MultiBombermanGame game) {
		this.game = game;
		this.cursor = new Cursor(198, 90);
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
		shapeRenderer.setColor(0, 0, 0, 0.5f);
		shapeRenderer.rect(100, 10, 440, 210);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		game.getBatch().begin();
		game.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.NETWORK, 0), 110, 70);
		game.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.NETWORK, 1), 252, 70);
		game.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.NETWORK, 2), 394, 70);
		layout.setText(font, MessageService.getInstance().getMessage("menu.network"));
		font.draw(game.getBatch(), layout, (Constante.SCREEN_SIZE_X / 2) - (layout.width / 2), 210);
		layout.setText(font, MessageService.getInstance().getMessage("menu.network.local"));
		font.draw(game.getBatch(), layout, 176 - (layout.width / 2), 62);
		layout.setText(font, MessageService.getInstance().getMessage("menu.network.server"));
		font.draw(game.getBatch(), layout, (Constante.SCREEN_SIZE_X / 2) - (layout.width / 2), 62);
		layout.setText(font, MessageService.getInstance().getMessage("menu.network.client"));
		font.draw(game.getBatch(), layout, 460 - (layout.width / 2), 62);
		switch (Context.getGameMode()) {
		case LOCAL:
			cursor.updateCursorPosition(166, 20);
			break;
		case SERVER:
			cursor.updateCursorPosition(310, 20);
			break;
		case CLIENT:
			cursor.updateCursorPosition(450, 20);
			break;
		default:
			cursor.updateCursorPosition(166, 20);
			break;
		}
		cursor.draw(game.getBatch());
		game.getBatch().end();
	}

	private void treatInput() {
		if (game.getMenuInputProcessor().pressNext()) {
			game.getScreen().dispose();
			game.setScreen(new MainScreen(game));
		}
		if (game.getMenuInputProcessor().pressPrevious()) {
			game.getScreen().dispose();
			game.setScreen(new SplashScreen(game));
		}
		if (game.getMenuInputProcessor().pressRight()) {
			switch (Context.getGameMode()) {
			case LOCAL:
				Context.setGameMode(GameModeEnum.SERVER);
				break;
			case SERVER:
				Context.setGameMode(GameModeEnum.CLIENT);
				break;
			case CLIENT:
				Context.setGameMode(GameModeEnum.LOCAL);
				break;
			default:
				Context.setGameMode(GameModeEnum.LOCAL);
				break;

			}
		}
		if (game.getMenuInputProcessor().pressLeft()) {
			switch (Context.getGameMode()) {
			case LOCAL:
				Context.setGameMode(GameModeEnum.CLIENT);
				break;
			case SERVER:
				Context.setGameMode(GameModeEnum.LOCAL);
				break;
			case CLIENT:
				Context.setGameMode(GameModeEnum.SERVER);
				break;
			default:
				Context.setGameMode(GameModeEnum.LOCAL);
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
