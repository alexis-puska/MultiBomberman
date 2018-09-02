package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.constante.Constante;
import com.mygdx.enumeration.MusicEnum;
import com.mygdx.service.NetworkService;
import com.mygdx.service.SoundService;
import com.mygdx.service.SpriteService;
import com.mygdx.service.input_processor.MenuInputProcessor;
import com.mygdx.view.SplashScreen;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MultiBombermanGame extends Game {
	private NetworkService networkService;
	private SpriteBatch batch;
	private OrthographicCamera screenCamera;
	private Viewport viewport;
	private MenuInputProcessor menuInputProcessor;

	@Override
	public void create() {
		Gdx.app.setLogLevel(Constante.LIBGDX_LOG_LEVEL);
		networkService = new NetworkService();
		networkService.initServer();
		SpriteService.getInstance();

		/****************************************
		 * Camera and viewport to draw fit image
		 ****************************************/
		screenCamera = new OrthographicCamera();
		screenCamera.position.set(Constante.SCREEN_SIZE_X / 2, Constante.SCREEN_SIZE_Y / 2, 0);
		screenCamera.update();
		viewport = new FitViewport(Constante.SCREEN_SIZE_X, Constante.SCREEN_SIZE_Y, screenCamera);
		viewport.apply();
		batch = new SpriteBatch();
		batch.setProjectionMatrix(screenCamera.combined);
		menuInputProcessor = new MenuInputProcessor();
		SoundService.getInstance().playMusic(MusicEnum.MENU);
		Gdx.input.setInputProcessor(menuInputProcessor);
		this.setScreen(new SplashScreen(this));
	}

	@Override
	public void dispose() {
		networkService.stopServer();
		batch.dispose();

	}

}
