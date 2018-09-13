package com.mygdx.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.game.MultiBombermanGame;
import com.mygdx.service.SpriteService;
import com.mygdx.service.input_processor.MenuListener;

public class SplashScreen implements MenuListener, Screen {

	final MultiBombermanGame game;

	public SplashScreen(final MultiBombermanGame game) {
		this.game = game;
		this.game.getPlayerService().setMenuListener(this);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.getScreenCamera().update();
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

	@Override
	public void pressStart() {
		game.getScreen().dispose();
		game.setScreen(new LangueScreen(game));
	}

	@Override
	public void pressSelect() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pressValide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pressUp() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pressDown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pressLeft() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pressRight() {
		// TODO Auto-generated method stub

	}
}
