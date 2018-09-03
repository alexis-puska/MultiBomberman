package com.mygdx.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.game.MultiBombermanGame;
import com.mygdx.service.SpriteService;

public class GameScreen implements Screen {

	final MultiBombermanGame game;

	public GameScreen(final MultiBombermanGame game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.getScreenCamera().update();

		if (game.getMenuInputProcessor().pressNext()) {
			game.getScreen().dispose();
			game.setScreen(new MainScreen(game));
		}

		game.getBatch().begin();
		game.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.BACKGROUND, 2), 0, 0);
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

}
