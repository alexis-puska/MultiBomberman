package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.constante.Constante;
import com.mygdx.enumeration.MusicEnum;
import com.mygdx.service.Context;
import com.mygdx.service.NetworkService;
import com.mygdx.service.SoundService;
import com.mygdx.service.SpriteService;
import com.mygdx.service.input_processor.ControllerAdapter;
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
	private ControllerAdapter controllerAdapter;

	@Override
	public void create() {
		Context.resetContext();
		Gdx.app.setLogLevel(Constante.LIBGDX_LOG_LEVEL);
		networkService = new NetworkService();
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

		SoundService.getInstance().playMusic(MusicEnum.MENU);
		menuInputProcessor = new MenuInputProcessor();
		Gdx.input.setInputProcessor(menuInputProcessor);

		Array<Controller> controllers = Controllers.getControllers();
		if (controllers != null && controllers.size > 0) {
			controllerAdapter = new ControllerAdapter(controllers.first(), true);
		}
		this.setScreen(new SplashScreen(this));
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	public void resize(int width, int height) {
		Gdx.app.log("MULTI", "RESIZE");
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1000);
					Array<Controller> controllers = Controllers.getControllers();
					controllers.forEach(c->{
						c.removeListener(controllerAdapter);
					});
					if (controllers != null && controllers.size > 0) {
						controllerAdapter.setController(controllers.first());
						controllers.first().addListener(controllerAdapter);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}).start();
	}
}
