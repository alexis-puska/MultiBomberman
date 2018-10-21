package com.mygdx.service;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygdx.dto.level.LevelDTO;
import com.mygdx.dto.level.LevelFileDTO;
import com.mygdx.dto.level.LevelGroupDTO;
import com.mygdx.dto.level.common.TextDTO;

public class LevelService {

	private static final String CLASS_NAME = "LevelService.class";
	private static final String SPRITE_JSON_FILE = "json/levels.json";

	private FileHandle levelJsonFile;
	private final ObjectMapper objectMapper;

	private LevelDTO currentLevelDTO;
	private LevelGroupDTO currentLevelGroupDTO;
	private LevelFileDTO levelFileDTO;

	public LevelService() {
		Gdx.app.debug(CLASS_NAME, "Init");

		objectMapper = new ObjectMapper();
		levelJsonFile = Gdx.files.internal(SPRITE_JSON_FILE);
		try {
			levelFileDTO = objectMapper.readValue(levelJsonFile.read(), LevelFileDTO.class);
			Gdx.app.log(CLASS_NAME, "Nb levelGroup found : " + levelFileDTO.getLevelGroups().size());

			currentLevelGroupDTO = levelFileDTO.getLevelGroups().get(0);
			currentLevelDTO = currentLevelGroupDTO.getLevel().get(0);
			Context.setLevel(new LevelDTO(currentLevelDTO));
			Gdx.app.log(CLASS_NAME, currentLevelGroupDTO.getName().get(0).getValue());
			Gdx.app.log(CLASS_NAME, currentLevelDTO.getName().get(0).getValue());
		} catch (JsonParseException e) {
			Gdx.app.error(CLASS_NAME, "JsonParseException : ", e);
		} catch (JsonMappingException e) {
			Gdx.app.error(CLASS_NAME, "JsonMappingException : ", e);
		} catch (IOException e) {
			Gdx.app.error(CLASS_NAME, "IOException : ", e);
		}
	}

	public void nextLevelGroup() {
		if (currentLevelGroupDTO != null) {
			int pos = levelFileDTO.getLevelGroups().indexOf(currentLevelGroupDTO);
			if (pos != levelFileDTO.getLevelGroups().size() - 1) {
				currentLevelGroupDTO = levelFileDTO.getLevelGroups().get(pos + 1);
				if (currentLevelGroupDTO.getLevel() != null && !currentLevelGroupDTO.getLevel().isEmpty()) {
					currentLevelDTO = currentLevelGroupDTO.getLevel().get(0);
					Context.setLevel(new LevelDTO(currentLevelDTO));
				} else {
					currentLevelDTO = null;
				}
			} else {
				currentLevelGroupDTO = levelFileDTO.getLevelGroups().get(0);
				if (currentLevelGroupDTO.getLevel() != null && !currentLevelGroupDTO.getLevel().isEmpty()) {
					currentLevelDTO = currentLevelGroupDTO.getLevel().get(0);
					Context.setLevel(new LevelDTO(currentLevelDTO));
				} else {
					currentLevelDTO = null;
				}
			}
		}
	}

	public void previousLevelGroup() {
		if (currentLevelGroupDTO != null) {
			int pos = levelFileDTO.getLevelGroups().indexOf(currentLevelGroupDTO);
			if (pos > 0) {
				currentLevelGroupDTO = levelFileDTO.getLevelGroups().get(pos - 1);
				if (currentLevelGroupDTO.getLevel() != null && !currentLevelGroupDTO.getLevel().isEmpty()) {
					currentLevelDTO = currentLevelGroupDTO.getLevel().get(0);
					Context.setLevel(new LevelDTO(currentLevelDTO));
				} else {
					currentLevelDTO = null;
				}
			} else {
				currentLevelGroupDTO = levelFileDTO.getLevelGroups().get(levelFileDTO.getLevelGroups().size() - 1);
				if (currentLevelGroupDTO.getLevel() != null && !currentLevelGroupDTO.getLevel().isEmpty()) {
					currentLevelDTO = currentLevelGroupDTO.getLevel().get(0);
					Context.setLevel(new LevelDTO(currentLevelDTO));
				} else {
					currentLevelDTO = null;
				}
			}
		}
	}

	public void nextLevel() {
		if (currentLevelDTO != null) {
			int pos = currentLevelGroupDTO.getLevel().indexOf(currentLevelDTO);
			if (pos == currentLevelGroupDTO.getLevel().size() - 1) {
				currentLevelDTO = currentLevelGroupDTO.getLevel().get(0);
			} else {
				currentLevelDTO = currentLevelGroupDTO.getLevel().get(pos + 1);
			}
			Context.setLevel(new LevelDTO(currentLevelDTO));
		}
	}

	public void previousLevel() {
		if (currentLevelDTO != null) {
			int pos = currentLevelGroupDTO.getLevel().indexOf(currentLevelDTO);
			if (pos == 0) {
				currentLevelDTO = currentLevelGroupDTO.getLevel().get(currentLevelGroupDTO.getLevel().size() - 1);
			} else {
				currentLevelDTO = currentLevelGroupDTO.getLevel().get(pos - 1);
			}
			Context.setLevel(new LevelDTO(currentLevelDTO));
		}
	}

	public String getLevelGroupName() {
		for (TextDTO trad : this.currentLevelGroupDTO.getName()) {
			if (trad.getLang() == Context.getLocale()) {
				return trad.getValue();
			}
		}
		return "";
	}

	public String getLevelName() {
		for (TextDTO trad : this.currentLevelDTO.getName()) {
			if (trad.getLang() == Context.getLocale()) {
				return trad.getValue();
			}
		}
		return "";
	}

	public String getLevelDescription() {
		for (TextDTO trad : this.currentLevelDTO.getDescription()) {
			if (trad.getLang() == Context.getLocale()) {
				return trad.getValue();
			}
		}
		return "";
	}
}
