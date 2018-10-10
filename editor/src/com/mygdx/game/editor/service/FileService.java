package com.mygdx.game.editor.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygdx.dto.level.LevelFileDTO;
import com.mygdx.dto.sprite.SpriteFileContent;
import com.mygdx.game.editor.EditorLauncher;

public class FileService {

	private static final Logger LOG = LogManager.getLogger(EditorLauncher.class);

	private final ObjectMapper objectMapper;

	public FileService() {
		this.objectMapper = new ObjectMapper();
	}

	public LevelFileDTO readJsonFile(InputStream in) {
		LevelFileDTO levelFile = null;
		try {
			levelFile = objectMapper.readValue(in, LevelFileDTO.class);
		} catch (JsonParseException e) {
			LOG.error("JsonParseException : " + e.getMessage());
		} catch (JsonMappingException e) {
			LOG.error("JsonMappingException : " + e.getMessage());
		} catch (IOException e) {
			LOG.error("IOException : " + e.getMessage());
		}
		return levelFile;
	}

	public void writeJson(LevelFileDTO levelFile, File file) {
		try {
			LOG.info("START Write level json file : " + file.getAbsolutePath());
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, levelFile);
		} catch (JsonProcessingException e) {
			LOG.error("JsonProcessingException : " + e.getMessage());
		} catch (IOException e) {
			LOG.error("IOException : " + e.getMessage());
		}
		LOG.info("Write level json file : SUCCESS");
	}

	public SpriteFileContent readJsonSpriteFile(InputStream in) {
		SpriteFileContent list = null;
		try {
			list = objectMapper.readValue(in, SpriteFileContent.class);
		} catch (JsonParseException e) {
			LOG.error("JsonParseException : " + e.getMessage());
		} catch (JsonMappingException e) {
			LOG.error("JsonMappingException : " + e.getMessage());
		} catch (IOException e) {
			LOG.error("IOException : " + e.getMessage());
		}
		return list;
	}
}
