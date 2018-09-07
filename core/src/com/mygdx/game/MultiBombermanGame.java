package com.mygdx.game;

import java.util.Arrays;

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
import com.mygdx.service.input_processor.ControllerAdapteur;
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
	private ControllerAdapteur controllerAdapteur;

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
		controllerAdapteur = new ControllerAdapteur();
		initController();
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
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				initController();
			};
		}).start();
	}

	private void initController() {
		Array<Controller> libgdxControllers = Controllers.getControllers();
		if (libgdxControllers != null && libgdxControllers.size != 0) {
			if (controllerAdapteur == null) {
				controllerAdapteur = new ControllerAdapteur();
			}
			Arrays.asList(libgdxControllers.toArray()).stream().forEach(c -> {
				c.removeListener(controllerAdapteur);
			});
			Controllers.clearListeners();
			libgdxControllers.get(0).addListener(controllerAdapteur);
			controllerAdapteur.setController(libgdxControllers.get(0));
		}
	}
}
