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
import com.mygdx.enumeration.LocaleEnum;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.game.MultiBombermanGame;
import com.mygdx.service.Context;
import com.mygdx.service.MessageService;
import com.mygdx.service.SpriteService;

public class LangueScreen implements Screen {

	private final MultiBombermanGame game;
	private final Cursor cursor;
	private final GlyphLayout layout;
	private final ShapeRenderer shapeRenderer;
	private BitmapFont font;

	public LangueScreen(final MultiBombermanGame game) {
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
		shapeRenderer.setProjectionMatrix(game.getBatch().getProjectionMatrix());
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0, 0, 0, 0.5f);
		shapeRenderer.rect(100, 75, 440, 150);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		game.getBatch().begin();
		game.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.FLAG, 0), 165, 120);
		game.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.FLAG, 1), 400, 120);
		layout.setText(font, MessageService.getInstance().getMessage("menu.lang.title"));
		font.draw(game.getBatch(), layout, (Constante.SCREEN_SIZE_X / 2) - (layout.width / 2), 200);
		switch (Context.getLocale()) {
		case ENGLISH:
			cursor.updateCursorPosition(430, 90);
			break;
		case FRENCH:
			cursor.updateCursorPosition(198, 90);
			break;
		default:
			cursor.updateCursorPosition(198, 90);
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
		if (game.getMenuInputProcessor().pressLeft()) {
			switch (Context.getLocale()) {
			case ENGLISH:
				Context.setLocale(LocaleEnum.FRENCH);
				break;
			case FRENCH:
				Context.setLocale(LocaleEnum.ENGLISH);
				break;
			default:
				Context.setLocale(LocaleEnum.ENGLISH);
				break;
			}
		}
		if (game.getMenuInputProcessor().pressRight()) {
			switch (Context.getLocale()) {
			case ENGLISH:
				Context.setLocale(LocaleEnum.FRENCH);
				break;
			case FRENCH:
				Context.setLocale(LocaleEnum.ENGLISH);
				break;
			default:
				Context.setLocale(LocaleEnum.ENGLISH);
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
