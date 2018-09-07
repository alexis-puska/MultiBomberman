package com.mygdx.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.game.MultiBombermanGame;
import com.mygdx.service.SpriteService;
import com.mygdx.service.input_processor.MenuListener;

public class ClientViewScreen implements Screen, MenuListener {

	final MultiBombermanGame game;

	public ClientViewScreen(final MultiBombermanGame game) {
		this.game = game;
		this.game.getMenuInputProcessor().changeMenuListeners(this);
		this.game.getControllerAdapteur().changeMenuListeners(this);
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
		// unused method
	}

	@Override
	public void pressSelect() {
	}

	@Override
	public void pressValide() {
		game.getScreen().dispose();
		game.setScreen(new MainScreen(game));
	}

	@Override
	public void pressUp() {
		// unused method
	}

	@Override
	public void pressDown() {
		// unused method
	}

	@Override
	public void pressLeft() {
		// unused method
	}

	@Override
	public void pressRight() {
		// unused method
	}

}
