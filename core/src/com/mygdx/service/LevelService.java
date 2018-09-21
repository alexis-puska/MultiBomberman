package com.mygdx.service;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygdx.dto.level.LevelFileDTO;
import com.mygdx.game.MultiBombermanGame;

public class LevelService {

	private static final String CLASS_NAME = "LevelService.class";
	private static final String SPRITE_JSON_FILE = "json/levels.json";

	private final MultiBombermanGame game;
	private FileHandle levelJsonFile;
	private final ObjectMapper objectMapper;

	public LevelService(final MultiBombermanGame game) {
		Gdx.app.debug(CLASS_NAME, "Init");
		this.game = game;

		objectMapper = new ObjectMapper();
		levelJsonFile = Gdx.files.internal(SPRITE_JSON_FILE);
		LevelFileDTO levelFileContent = null;
		try {
			levelFileContent = objectMapper.readValue(levelJsonFile.read(), LevelFileDTO.class);

		} catch (JsonParseException e) {
			Gdx.app.error(CLASS_NAME, "JsonParseException : ", e);
		} catch (JsonMappingException e) {
			Gdx.app.error(CLASS_NAME, "JsonMappingException : ", e);
		} catch (IOException e) {
			Gdx.app.error(CLASS_NAME, "IOException : ", e);
		}
	}

	public void loadNextLevel() {

	}

	public void loadPreviousLevel() {

	}

}
