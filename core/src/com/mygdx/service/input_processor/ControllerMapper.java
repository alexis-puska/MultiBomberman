package com.mygdx.service.input_processor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygdx.dto.controller.AxisMappingDTO;
import com.mygdx.dto.controller.ControllerMappingFileDTO;
import com.mygdx.enumeration.ControllerButtonEnum;

public class ControllerMapper {

	private static final String CONTROLLER_JSON_FILE = "json/controller.json";
	private static final String CLASS_NAME = "ControllerMapper";
	private static final float detection = 0.79f;

	private Map<String, Map<Integer, ControllerButtonEnum>> mappingButton;
	private Map<Integer, ControllerButtonEnum> mappingDefault;
	private Map<String, Map<Integer, AxisMappingDTO>> mappingAxis;
	private Map<String, PovDirection> lastAxisDirection;
	private FileHandle controllerJsonFile;
	private final ObjectMapper objectMapper;

	public ControllerMapper() {
		mappingButton = new HashMap<>();
		mappingDefault = new HashMap<>();
		mappingAxis = new HashMap<>();
		lastAxisDirection = new HashMap<>();
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
					if (c.getAxis() != null) {
						Map<Integer, AxisMappingDTO> ax = new HashMap<>();
						c.getAxis().stream().forEach(a -> {
							ax.put(a.getAxis(), a);
						});
						mappingAxis.put(c.getName(), ax);
						lastAxisDirection.put(c.getName(), PovDirection.center);
					}
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
		mappingButton.keySet().stream().forEach(n -> System.out.println(n));
		System.out.println(controller.getName());
		if (mappingButton.containsKey(controller.getName())
				&& mappingButton.get(controller.getName()).containsKey(button)) {
			return mappingButton.get(controller.getName()).get(button);
		} else {
			if (mappingDefault.containsKey(button)) {
				return mappingDefault.get(button);
			}
		}
		return null;
	}

	public PovDirection getDirection(Controller controller, int axisCode, float value) {
		if (mappingAxis.containsKey(controller.getName())
				&& mappingAxis.get(controller.getName()).containsKey(axisCode)) {
			if (value > detection || (value < -detection && value >= -1.1f)) {
				AxisMappingDTO a = mappingAxis.get(controller.getName()).get(axisCode);
				if (a.isInverted()) {
					switch (a.getType()) {
					case HORIZONTAL:
						if (value > 0) {
							if (lastAxisDirection.containsKey(controller.getName())
									&& lastAxisDirection.get(controller.getName()) == PovDirection.center) {
								lastAxisDirection.put(controller.getName(), PovDirection.west);
								return PovDirection.west;
							}
						} else {
							if (lastAxisDirection.containsKey(controller.getName())
									&& lastAxisDirection.get(controller.getName()) == PovDirection.center) {
								lastAxisDirection.put(controller.getName(), PovDirection.east);
								return PovDirection.east;
							}
						}
					case VERTICAL:
						if (value > 0) {
							if (lastAxisDirection.containsKey(controller.getName())
									&& lastAxisDirection.get(controller.getName()) == PovDirection.center) {
								lastAxisDirection.put(controller.getName(), PovDirection.south);
								return PovDirection.south;
							}
						} else {
							if (lastAxisDirection.containsKey(controller.getName())
									&& lastAxisDirection.get(controller.getName()) == PovDirection.center) {
								lastAxisDirection.put(controller.getName(), PovDirection.north);
								return PovDirection.north;
							}
						}
					default:
						break;
					}
					return null;
				} else {
					switch (a.getType()) {
					case HORIZONTAL:
						if (value > 0) {
							if (lastAxisDirection.containsKey(controller.getName())
									&& lastAxisDirection.get(controller.getName()) == PovDirection.center) {
								lastAxisDirection.put(controller.getName(), PovDirection.east);
								return PovDirection.east;
							}
						} else {
							if (lastAxisDirection.containsKey(controller.getName())
									&& lastAxisDirection.get(controller.getName()) == PovDirection.center) {
								lastAxisDirection.put(controller.getName(), PovDirection.west);
								return PovDirection.west;
							}
						}
					case VERTICAL:
						if (value > 0) {
							if (lastAxisDirection.containsKey(controller.getName())
									&& lastAxisDirection.get(controller.getName()) == PovDirection.center) {
								lastAxisDirection.put(controller.getName(), PovDirection.north);
								return PovDirection.north;
							}
						} else {
							if (lastAxisDirection.containsKey(controller.getName())
									&& lastAxisDirection.get(controller.getName()) == PovDirection.center) {
								lastAxisDirection.put(controller.getName(), PovDirection.south);
								return PovDirection.south;
							}
						}
					default:
						break;
					}
					return null;
				}

			} else if (lastAxisDirection.containsKey(controller.getName())
					&& lastAxisDirection.get(controller.getName()) != PovDirection.center) {
				AxisMappingDTO a = mappingAxis.get(controller.getName()).get(axisCode);
				switch (a.getType()) {
				case HORIZONTAL:
					if (lastAxisDirection.containsKey(controller.getName())
							&& (lastAxisDirection.get(controller.getName()) == PovDirection.east
									|| lastAxisDirection.get(controller.getName()) == PovDirection.west)) {

						lastAxisDirection.put(controller.getName(), PovDirection.center);
						return PovDirection.center;
					}

					break;
				case VERTICAL:
					if (lastAxisDirection.containsKey(controller.getName())
							&& (lastAxisDirection.get(controller.getName()) == PovDirection.south
									|| lastAxisDirection.get(controller.getName()) == PovDirection.north)) {

						lastAxisDirection.put(controller.getName(), PovDirection.center);
						return PovDirection.center;
					}
					break;
				default:
					break;

				}

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
