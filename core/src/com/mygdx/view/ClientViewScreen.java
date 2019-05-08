package com.mygdx.view;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygdx.dto.level.LevelDTO;
import com.mygdx.enumeration.SoundEnum;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.SoundService;
import com.mygdx.service.SpriteService;
import com.mygdx.service.input_processor.MenuListener;
import com.mygdx.service.network.dto.LevelScreenDTO;
import com.mygdx.service.network.dto.RuleScreenDTO;
import com.mygdx.service.network.dto.SkinScreenDTO;
import com.mygdx.service.network.enumeration.LastRequestEnum;

public class ClientViewScreen implements Screen, MenuListener {

	private static final String CLASS_NAME = "ClientViewScreen.class";

	final MultiBombermanGame mbGame;
	private ObjectMapper objectMapper;
	private SkinScreenDTO skinScreenDTO;
	private RuleScreenDTO ruleScreenDTO;
	private LevelScreenDTO levelScreenDTO;
	private LevelDTO levelDTO;
	private LastRequestEnum last;

	public ClientViewScreen(final MultiBombermanGame mbGame) {
		this.mbGame = mbGame;
		this.mbGame.getPlayerService().setMenuListener(this);
		this.mbGame.getNetworkService().setClientViewScreen(this);
		this.objectMapper = new ObjectMapper();
		this.last = LastRequestEnum.SKIN_SCREEN;
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
		switch (last) {
		case GAME_SCREEN:
			drawGameScreen();
			break;
		case LEVEL_SCREEN:
			drawLevelScreen();
			break;
		case RULE_SCREEN:
			drawRuleScreen();
			break;
		case SKIN_SCREEN:
			drawSkinScreen();
		default:
			break;
		}

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
		SoundService.getInstance().playSound(SoundEnum.CANCEL);
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

	@Override
	public void pressX() {
		// unused method
	}

	@Override
	public void pressY() {
		// unused method
	}

	@Override
	public void pressL() {
		// unused method
	}

	@Override
	public void pressR() {
		// unused method
	}

	/**********************************************
	 * --- RECEIVE STRING AND DECODE ---
	 **********************************************/
	public void receiveSkinScreen(String line) {
		try {
			skinScreenDTO = this.objectMapper.readValue(line.substring(line.indexOf(":") + 1), SkinScreenDTO.class);
			last = LastRequestEnum.SKIN_SCREEN;
		} catch (JsonParseException e) {
			Gdx.app.error(CLASS_NAME, "JsonParseException");
		} catch (JsonMappingException e) {
			Gdx.app.error(CLASS_NAME, "JsonMappingException");
		} catch (IOException e) {
			Gdx.app.error(CLASS_NAME, "IOException");
		}
	}

	public void receiveRuleScreen(String line) {
		try {
			ruleScreenDTO = this.objectMapper.readValue(line.substring(line.indexOf(":") + 1), RuleScreenDTO.class);
			last = LastRequestEnum.RULE_SCREEN;
		} catch (JsonParseException e) {
			Gdx.app.error(CLASS_NAME, "JsonParseException");
		} catch (JsonMappingException e) {
			Gdx.app.error(CLASS_NAME, "JsonMappingException");
		} catch (IOException e) {
			Gdx.app.error(CLASS_NAME, "IOException");
		}
	}

	public void receiveLevelScreen(String line) {
		try {
			levelScreenDTO = this.objectMapper.readValue(line.substring(line.indexOf(":") + 1), LevelScreenDTO.class);
			last = LastRequestEnum.LEVEL_SCREEN;
		} catch (JsonParseException e) {
			Gdx.app.error(CLASS_NAME, "JsonParseException");
		} catch (JsonMappingException e) {
			Gdx.app.error(CLASS_NAME, "JsonMappingException");
		} catch (IOException e) {
			Gdx.app.error(CLASS_NAME, "IOException");
		}
	}

	public void receiveLevelDef(String line) {
		try {
			levelDTO = this.objectMapper.readValue(line.substring(line.indexOf(":") + 1), LevelDTO.class);
		} catch (JsonParseException e) {
			Gdx.app.error(CLASS_NAME, "JsonParseException");
		} catch (JsonMappingException e) {
			Gdx.app.error(CLASS_NAME, "JsonMappingException");
		} catch (IOException e) {
			Gdx.app.error(CLASS_NAME, "IOException");
		}
	}

	public void receiveGame(String line) {
		last = LastRequestEnum.GAME_SCREEN;
	}

	public void receiveSound(String line) {

	}

	public void receiveMusique(String line) {

	}

	/**********************************************
	 * --- DRAW PART ---
	 **********************************************/

	private void drawSkinScreen() {
		Gdx.app.log(CLASS_NAME, "drawSkinScreen");
		mbGame.getBatch().begin();
		mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.BACKGROUND, 2), 0, 0);
		mbGame.getBatch().end();
	}

	private void drawRuleScreen() {
		Gdx.app.log(CLASS_NAME, "drawRuleScreen");
		mbGame.getBatch().begin();
		mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.BACKGROUND, 2), 0, 0);
		mbGame.getBatch().end();
	}

	private void drawLevelScreen() {
		Gdx.app.log(CLASS_NAME, "drawLevelScreen");
		mbGame.getBatch().begin();
		mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.BACKGROUND, 2), 0, 0);
		mbGame.getBatch().end();
	}

	private void drawGameScreen() {
		Gdx.app.log(CLASS_NAME, "drawGameScreen");
		mbGame.getBatch().begin();
		mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.BACKGROUND, 2), 0, 0);
		mbGame.getBatch().end();
	}
}
