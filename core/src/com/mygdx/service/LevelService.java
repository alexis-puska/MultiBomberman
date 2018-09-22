package com.mygdx.service;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygdx.constante.Constante;
import com.mygdx.dto.level.LevelFileDTO;
import com.mygdx.game.MultiBombermanGame;

public class LevelService {

	private static final String CLASS_NAME = "LevelService.class";
	private static final String SPRITE_JSON_FILE = "json/levels.json";

	private final MultiBombermanGame game;
	private FileHandle levelJsonFile;
	private final ObjectMapper objectMapper;

	private int[] bonus;
	private int level;
	private int variante;

	public LevelService(final MultiBombermanGame game) {
		Gdx.app.debug(CLASS_NAME, "Init");
		this.game = game;
		bonus = new int[Constante.MAX_BONUS];

		objectMapper = new ObjectMapper();
		levelJsonFile = Gdx.files.internal(SPRITE_JSON_FILE);
		LevelFileDTO levelFileContent = null;
		try {
			levelFileContent = objectMapper.readValue(levelJsonFile.read(), LevelFileDTO.class);
			Gdx.app.log(CLASS_NAME, "Nb level found : " + levelFileContent.getLevels().size());

		} catch (JsonParseException e) {
			Gdx.app.error(CLASS_NAME, "JsonParseException : ", e);
		} catch (JsonMappingException e) {
			Gdx.app.error(CLASS_NAME, "JsonMappingException : ", e);
		} catch (IOException e) {
			Gdx.app.error(CLASS_NAME, "IOException : ", e);
		}
	}

	public void incBonus(int i, int n) {
		int nb = 0;
		for (int j = 0; j < Constante.MAX_BONUS; j++) {
			nb += this.bonus[j];
		}

		if ((nb + n) < Constante.MAX_BONUS_PER_LEVEL) {
			this.bonus[i] += n;
		} else {
			this.bonus[i] += Constante.MAX_BONUS_PER_LEVEL - nb;
		}

	}

	public void decBonus(int i, int n) {
		this.bonus[i] -= n;
		if (this.bonus[i] < 0) {
			this.bonus[i] = 0;
		}
	}

	public int getBonus(int i) {
		return this.bonus[i];
	}

	public void previousLevel() {
		// make
	}

	public void nextLevel() {
		// make
	}

	public void previousVariante() {
		// make
	}

	public void nextVariante() {
		// make
	}

	public void loadLevel() {
		// make
	}

}
