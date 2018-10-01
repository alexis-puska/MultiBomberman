package com.mygdx.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.game.MultiBombermanGame;
import com.mygdx.service.SpriteService;
import com.mygdx.service.input_processor.MenuListener;

public class ClientViewScreen implements Screen, MenuListener {

	private static final String CLASS_NAME = "ClientViewScreen.class";

	final MultiBombermanGame game;

	public ClientViewScreen(final MultiBombermanGame game) {
		this.game = game;
		this.game.getPlayerService().setMenuListener(this);
		this.game.getNetworkService().setClientViewScreen(this);
	}

	@Override
	public void render(float delta) {
		if (!game.getNetworkService().getClient().isStatus()) {
			game.getScreen().dispose();
			game.setScreen(new ClientConnexionScreen(game));
		}
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
		game.getNetworkService().disconnectFromServer();
		game.getScreen().dispose();
		game.setScreen(new ClientConnexionScreen(game));
	}

	@Override
	public void pressA() {
		// Unused method
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

	@Override
	public void pressB() {
		// unused method
	}

	public void receive(String line) {
		Gdx.app.log(CLASS_NAME, "received : " + line);
	}

	@Override
	public void pressX() {
		//unused method
		
	}

	@Override
	public void pressY() {
		//unused method
		
	}

	@Override
	public void pressL() {
		//unused method
		
	}

	@Override
	public void pressR() {
		//unused method
		
	}

}
