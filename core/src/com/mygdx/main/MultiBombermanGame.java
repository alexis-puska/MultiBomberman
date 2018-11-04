package com.mygdx.main;

import java.io.IOException;
import java.util.UUID;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygdx.constante.Constante;
import com.mygdx.dto.MultiBombermanDTO;
import com.mygdx.enumeration.MusicEnum;
import com.mygdx.service.Context;
import com.mygdx.service.LevelService;
import com.mygdx.service.PlayerService;
import com.mygdx.service.SoundService;
import com.mygdx.service.SpriteService;
import com.mygdx.service.input_processor.ControllerAdapter;
import com.mygdx.service.input_processor.MenuInputProcessor;
import com.mygdx.service.network.NetworkService;
import com.mygdx.view.SplashScreen;

import lombok.Getter;

@Getter
public class MultiBombermanGame extends Game {

	private static final String LOG_NAME = "MultiBombermanGame.class";

	private NetworkService networkService;
	private PlayerService playerService;
	private LevelService levelService;
	private SpriteBatch batch;
	private OrthographicCamera screenCamera;
	private Viewport viewport;
	private MenuInputProcessor menuInputProcessor;
	private ControllerAdapter controllerAdapter;

	@Override
	public void create() {
		Context.resetContext();
		retrieveGUID();
		Gdx.app.setLogLevel(Constante.LIBGDX_LOG_LEVEL);
		networkService = new NetworkService(this);
		playerService = new PlayerService(this);
		levelService = new LevelService();
		SpriteService.getInstance();

		/****************************************
		 * Camera and viewport to draw fit image
		 ****************************************/
		screenCamera = new OrthographicCamera();
		screenCamera.position.set(Constante.SCREEN_SIZE_X / 2, Constante.SCREEN_SIZE_Y / 2, 0);
		screenCamera.update();
		viewport = new StretchViewport(Constante.SCREEN_SIZE_X, Constante.SCREEN_SIZE_Y, screenCamera);
		viewport.apply();
		batch = new SpriteBatch();
		batch.setProjectionMatrix(screenCamera.combined);

		controllerAdapter = new ControllerAdapter(playerService);

		SoundService.getInstance().playMusic(MusicEnum.MENU);
		menuInputProcessor = new MenuInputProcessor(playerService);
		Gdx.input.setInputProcessor(menuInputProcessor);

		this.setScreen(new SplashScreen(this));
	}

	private void retrieveGUID() {
		ObjectMapper objectMapper = new ObjectMapper();
		FileHandle multiBombermanJson = Gdx.files.local("MultiBomberman.json");
		if (multiBombermanJson.exists()) {
			MultiBombermanDTO multiBomberman;
			try {
				multiBomberman = objectMapper.readValue(multiBombermanJson.read(), MultiBombermanDTO.class);
				Context.setUuid(multiBomberman.getUuid());
			} catch (JsonParseException e) {
				Gdx.app.error(LOG_NAME, "JsonParseException : ", e);
				initGUID(objectMapper);
			} catch (JsonMappingException e) {
				Gdx.app.error(LOG_NAME, "JsonMappingException : ", e);
				initGUID(objectMapper);
			} catch (IOException e) {
				Gdx.app.error(LOG_NAME, "IOException : ", e);
			}
		} else {
			initGUID(objectMapper);
		}
	}

	private void initGUID(ObjectMapper objectMapper) {
		String uuid = UUID.randomUUID().toString();
		FileHandle multiBombermanJson = Gdx.files.local("MultiBomberman.json");
		if (multiBombermanJson.exists()) {
			multiBombermanJson.delete();
		}
		MultiBombermanDTO multiBomberman = new MultiBombermanDTO();
		multiBomberman.setUuid(uuid);
		String json;
		try {
			json = objectMapper.writeValueAsString(multiBomberman);
			multiBombermanJson.writeString(json, false);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		Context.setUuid(uuid);
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	public void resize(int width, int height) {
//		this.viewport.update(width, height, true);
//		Gdx.app.log(LOG_NAME, "RESIZE");
//		try {
//			Thread.sleep(500);
//			Controllers.clearListeners();
//			Controllers.addListener(controllerAdapter);
//		} catch (InterruptedException e) {
//			Gdx.app.error("MultiBomberman", "Resize thread error");
//			Thread.currentThread().interrupt();
//		}
	}
}
