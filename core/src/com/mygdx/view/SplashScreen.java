package com.mygdx.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.constante.Constante;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.game.MultiBombermanGame;
import com.mygdx.service.SpriteService;
import com.mygdx.utils.DrawUtils;

public class SplashScreen implements Screen {

	final MultiBombermanGame game;

	public SplashScreen(final MultiBombermanGame game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.getScreenCamera().update();

		if (game.getMenuInputProcessor().pressNext()) {
			game.getScreen().dispose();
			// game.setScreen(new SelectionLangScreen(game));
		}

		TextureRegion textureRegionTitle = SpriteService.getInstance().getSprite(SpriteEnum.BACKGROUND, 0);
		game.getBatch().begin();
		DrawUtils.fillBackground(game.getBatch(), SpriteEnum.BACKGROUND);
		game.getBatch().draw(textureRegionTitle, (420 / 2) - (textureRegionTitle.getRegionWidth() / 2),
				(Constante.SCREEN_SIZE_Y / 2));
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
