package com.mygdx.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.SpriteService;
import com.mygdx.service.input_processor.MenuListener;

public class ClientViewScreen implements Screen, MenuListener {

	private static final String CLASS_NAME = "ClientViewScreen.class";

	final MultiBombermanGame mbGame;

	public ClientViewScreen(final MultiBombermanGame mbGame) {
		this.mbGame = mbGame;
		this.mbGame.getPlayerService().setMenuListener(this);
		this.mbGame.getNetworkService().setClientViewScreen(this);
	}

	@Override
	public void render(float delta) {
		if (!mbGame.getNetworkService().getClient().isStatus()) {
			mbGame.getScreen().dispose();
			mbGame.setScreen(new ClientConnexionScreen(mbGame));
		}
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		mbGame.getScreenCamera().update();
		mbGame.getBatch().begin();
		mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.BACKGROUND, 2), 0, 0);
		mbGame.getBatch().end();

	}

	@Override
	public void show() {
		// unused method
	}

	@Override
	public void resize(int width, int height) {
		// unused method
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
		mbGame.getNetworkService().disconnectFromServer();
		mbGame.getScreen().dispose();
		mbGame.setScreen(new ClientConnexionScreen(mbGame));
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
