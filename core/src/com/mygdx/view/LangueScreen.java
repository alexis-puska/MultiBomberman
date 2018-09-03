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
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.game.MultiBombermanGame;
import com.mygdx.service.MessageService;
import com.mygdx.service.SpriteService;

public class LangueScreen implements Screen {

	final MultiBombermanGame game;
	private BitmapFont font;
	private GlyphLayout layout;
	private ShapeRenderer shapeRenderer;

	public LangueScreen(final MultiBombermanGame game) {
		this.game = game;
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
		shapeRenderer.rect(100, 75, 440, 150);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		game.getBatch().begin();
		game.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.FLAG, 0), 165, 120);
		game.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.FLAG, 1), 400, 120);
		layout.setText(font, MessageService.getInstance().getMessage("menu.lang.title"));
		font.draw(game.getBatch(), layout, (Constante.SCREEN_SIZE_X / 2) - (layout.width / 2), 200);
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
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
				Gdx.files.internal("font/font_gbboot.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 14;
		parameter.borderWidth = 0f;
		parameter.borderColor = new Color(255, 0, 0, 255);
		parameter.color = new Color(255, 0, 0, 255);
		font = generator.generateFont(parameter);
		generator.dispose();
	}

}
