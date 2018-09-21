package com.mygdx.service.input_processor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygdx.dto.controller.ControllerMappingFileDTO;
import com.mygdx.enumeration.ControllerButtonEnum;

public class ControllerMapper {

	private static final String CONTROLLER_JSON_FILE = "json/controller.json";
	private static final String CLASS_NAME = "ControllerMapper";

	private Map<String, Map<Integer, ControllerButtonEnum>> mappingButton;
	private Map<Integer, ControllerButtonEnum> mappingDefault;
	private FileHandle controllerJsonFile;
	private final ObjectMapper objectMapper;

	public ControllerMapper() {
		mappingButton = new HashMap<>();
		mappingDefault = new HashMap<>();
		objectMapper = new ObjectMapper();
		controllerJsonFile = Gdx.files.internal(CONTROLLER_JSON_FILE);
		ControllerMappingFileDTO controllerMappingFileContent;
		try {
			controllerMappingFileContent = objectMapper.readValue(controllerJsonFile.read(),
					ControllerMappingFileDTO.class);
			if (controllerMappingFileContent != null) {
				controllerMappingFileContent.getControllers().stream().forEach(c -> {
					Map<Integer, ControllerButtonEnum> buttons = new HashMap<>();
					c.getButtons().stream().forEach(b -> {
						buttons.put(b.getButton(), b.getMapped());
					});
					mappingButton.put(c.getName(), buttons);
				});
			}
			buildDefault();
		} catch (JsonParseException e) {
			Gdx.app.error(CLASS_NAME, "JsonParseException : ", e);
		} catch (JsonMappingException e) {
			Gdx.app.error(CLASS_NAME, "JsonMappingException : ", e);
		} catch (IOException e) {
			Gdx.app.error(CLASS_NAME, "IOException : ", e);
		}
	}

	public ControllerButtonEnum getButton(Controller controller, int button) {
		if (mappingButton.containsKey(controller.getName())
				&& mappingButton.get(controller.getName()).containsKey(button)) {
			return mappingButton.get(controller.getName()).get(button);
		} else {
			if (mappingDefault.containsKey(button)) {
				mappingDefault.get(button);
			}
		}
		return null;
	}

	public void buildDefault() {
		mappingDefault.put(0, ControllerButtonEnum.A);
		mappingDefault.put(1, ControllerButtonEnum.B);
		mappingDefault.put(2, ControllerButtonEnum.X);
		mappingDefault.put(3, ControllerButtonEnum.Y);
		mappingDefault.put(4, ControllerButtonEnum.R1);
		mappingDefault.put(5, ControllerButtonEnum.R2);
		mappingDefault.put(6, ControllerButtonEnum.L1);
		mappingDefault.put(7, ControllerButtonEnum.L2);
		mappingDefault.put(8, ControllerButtonEnum.SELECT);
		mappingDefault.put(9, ControllerButtonEnum.START);
	}
}
