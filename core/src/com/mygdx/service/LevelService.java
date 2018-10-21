package com.mygdx.service;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygdx.constante.Constante;
import com.mygdx.domain.level.LevelGroup;
import com.mygdx.domain.level.Level;
import com.mygdx.dto.level.LevelGroupDTO;
import com.mygdx.dto.level.LevelFileDTO;
import com.mygdx.dto.level.LevelDTO;
import com.mygdx.service.mapper.LevelGroupMapper;

public class LevelService {

	private static final String CLASS_NAME = "LevelService.class";
	private static final String SPRITE_JSON_FILE = "json/levels.json";

	private FileHandle levelJsonFile;
	private final ObjectMapper objectMapper;
	private final LevelGroupMapper levelGroupMapper;

	private int[] bonus;
	private LevelGroup levelGroup;
	private Level level;
	private LevelDTO currentLevel;
	private LevelGroupDTO currentLevelGroup;
	private LevelFileDTO levelFileDTO;

	public LevelService() {
		Gdx.app.debug(CLASS_NAME, "Init");
		this.levelGroupMapper = new LevelGroupMapper();
		bonus = new int[Constante.MAX_BONUS];

		objectMapper = new ObjectMapper();
		levelJsonFile = Gdx.files.internal(SPRITE_JSON_FILE);
		try {
			levelFileDTO = objectMapper.readValue(levelJsonFile.read(), LevelFileDTO.class);
			Gdx.app.log(CLASS_NAME, "Nb levelGroup found : " + levelFileDTO.getLevelGroups().size());

			levelGroup = levelGroupMapper.toEntity(levelFileDTO.getLevelGroups().get(0));
			level = levelGroup.getLevel().get(0);
			Gdx.app.log(CLASS_NAME, levelGroup.getName().get(0).getValue());
			Gdx.app.log(CLASS_NAME, level.getName().get(0).getValue());
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

	public void nextLevelGroup() {
		if (currentLevelGroup != null) {
			int pos = levelFileDTO.getLevelGroups().indexOf(currentLevelGroup);
			if (pos != levelFileDTO.getLevelGroups().size() - 1) {
				currentLevelGroup = levelFileDTO.getLevelGroups().get(pos + 1);
				if (currentLevelGroup.getLevel() != null && !currentLevelGroup.getLevel().isEmpty()) {
					currentLevel = currentLevelGroup.getLevel().get(0);
				} else {
					currentLevel = null;
				}
			} else {
				currentLevelGroup = levelFileDTO.getLevelGroups().get(0);
				if (currentLevelGroup.getLevel() != null && !currentLevelGroup.getLevel().isEmpty()) {
					currentLevel = currentLevelGroup.getLevel().get(0);
				} else {
					currentLevel = null;
				}
			}
		}
	}

	public void previousLevelGroup() {
		if (currentLevelGroup != null) {
			int pos = levelFileDTO.getLevelGroups().indexOf(currentLevelGroup);
			if (pos > 0) {
				currentLevelGroup = levelFileDTO.getLevelGroups().get(pos - 1);
				if (currentLevelGroup.getLevel() != null && !currentLevelGroup.getLevel().isEmpty()) {
					currentLevel = currentLevelGroup.getLevel().get(0);
				} else {
					currentLevel = null;
				}
			} else {
				currentLevelGroup = levelFileDTO.getLevelGroups().get(levelFileDTO.getLevelGroups().size() - 1);
				if (currentLevelGroup.getLevel() != null && !currentLevelGroup.getLevel().isEmpty()) {
					currentLevel = currentLevelGroup.getLevel().get(0);
				} else {
					currentLevel = null;
				}
			}
		}
	}

	public void nextLevel() {
		if (currentLevel != null) {
			int pos = currentLevelGroup.getLevel().indexOf(currentLevel);
			if (pos == currentLevelGroup.getLevel().size() - 1) {
				currentLevel = currentLevelGroup.getLevel().get(0);
			} else {
				currentLevel = currentLevelGroup.getLevel().get(pos + 1);
			}
		}
	}

	public void previousLevel() {
		if (currentLevel != null) {
			int pos = currentLevelGroup.getLevel().indexOf(currentLevel);
			if (pos == 0) {
				currentLevel = currentLevelGroup.getLevel().get(currentLevelGroup.getLevel().size() - 1);
			} else {
				currentLevel = currentLevelGroup.getLevel().get(pos - 1);
			}
		}
	}

	public LevelGroup loadLevelGroup() {
		return this.levelGroupMapper.toEntity(currentLevelGroup);
	}

}
