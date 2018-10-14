package com.mygdx.game.editor.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygdx.dto.level.CustomTextureDTO;
import com.mygdx.dto.level.LevelDTO;
import com.mygdx.dto.level.LevelFileDTO;
import com.mygdx.dto.level.TextureDTO;
import com.mygdx.dto.level.VarianteDTO;
import com.mygdx.dto.level.WallDTO;
import com.mygdx.dto.level.common.DefaultTextureDTO;
import com.mygdx.dto.level.common.PositionableDTO;
import com.mygdx.dto.level.common.TextDTO;
import com.mygdx.enumeration.LocaleEnum;
import com.mygdx.enumeration.SpriteEnum;

public class LevelService2 {

	private static final Logger LOG = LogManager.getLogger(LevelService2.class);

	private final ObjectMapper objectMapper;
	private LevelFileDTO levelFileDTO;

	private LevelDTO currentLevel;
	private VarianteDTO currentVariante;

	public LevelService2() {
		this.objectMapper = new ObjectMapper();
	}

	/**
	 * Load JSON Level file and init map with all level and variante
	 * 
	 * @param in inputStream of file
	 * @return Level to Edit
	 */
	public void load(InputStream in) {
		levelFileDTO = null;
		try {
			levelFileDTO = objectMapper.readValue(in, LevelFileDTO.class);

			if (!levelFileDTO.getLevels().isEmpty()) {
				currentLevel = levelFileDTO.getLevels().get(0);
				if (!currentLevel.getVariante().isEmpty()) {
					currentVariante = currentLevel.getVariante().get(0);
				}
			} else {
				addLevel();
			}
		} catch (JsonParseException e) {
			LOG.error("JsonParseException : " + e.getMessage());
		} catch (JsonMappingException e) {
			LOG.error("JsonMappingException : " + e.getMessage());
		} catch (IOException e) {
			LOG.error("IOException : " + e.getMessage());
		}
	}

	public void save(File file) {
		try {
			LOG.info("START Write level json file : " + file.getAbsolutePath());
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, levelFileDTO);
		} catch (JsonProcessingException e) {
			LOG.error("JsonProcessingException : " + e.getMessage());
		} catch (IOException e) {
			LOG.error("IOException : " + e.getMessage());
		}
		LOG.info("Write level json file : SUCCESS");
	}

	public LevelDTO getCurrentLevel() {
		return this.currentLevel;
	}

	public VarianteDTO getCurrentVariante() {
		return this.currentVariante;
	}

	public void nextLevel() {
		int pos = levelFileDTO.getLevels().indexOf(currentLevel);
		if (pos == levelFileDTO.getLevels().size() - 1) {
			currentLevel = levelFileDTO.getLevels().get(0);
		} else {
			currentLevel = levelFileDTO.getLevels().get(pos + 1);
		}
	}

	public void previousLevel() {
		int pos = levelFileDTO.getLevels().indexOf(currentLevel);
		if (pos == 0) {
			currentLevel = levelFileDTO.getLevels().get(levelFileDTO.getLevels().size() - 1);
		} else {
			currentLevel = levelFileDTO.getLevels().get(pos - 1);
		}
	}

	public void addLevel() {
		levelFileDTO.getLevels().add(createNewLevel());
		currentLevel = levelFileDTO.getLevels().get(levelFileDTO.getLevels().size() - 1);
		currentVariante = currentLevel.getVariante().get(0);
	}

	public void deleteLevel() {
		if (levelFileDTO.getLevels().size() > 1) {
			levelFileDTO.getLevels().remove(currentLevel);
			levelFileDTO.getLevels().get(0);
		}
	}

	private LevelDTO createNewLevel() {
		LevelDTO level = new LevelDTO();
		List<TextDTO> names = new ArrayList<>();
		for (LocaleEnum val : LocaleEnum.values()) {
			names.add(new TextDTO(val.getCode(), ""));
		}
		level.setName(names);
		level.getVariante().add(createNewVariante());
		return level;
	}

	public void nextVariante() {
		int pos = currentLevel.getVariante().indexOf(currentVariante);
		if (pos == currentLevel.getVariante().size() - 1) {
			currentVariante = currentLevel.getVariante().get(0);
		} else {
			currentVariante = currentLevel.getVariante().get(pos + 1);
		}
	}

	public void previousVariante() {
		int pos = currentLevel.getVariante().indexOf(currentVariante);
		if (pos == 0) {
			currentVariante = currentLevel.getVariante().get(currentLevel.getVariante().size() - 1);
		} else {
			currentVariante = currentLevel.getVariante().get(pos - 1);
		}
	}

	public void addVariante() {
		currentLevel.getVariante().add(createNewVariante());
		currentVariante = currentLevel.getVariante().get(currentLevel.getVariante().size() - 1);
	}

	public void deleteVariante() {
		if (currentLevel.getVariante().size() > 1) {
			currentLevel.getVariante().remove(currentVariante);
			currentLevel.getVariante().get(0);
		}
	}

	private VarianteDTO createNewVariante() {
		VarianteDTO variante = new VarianteDTO();
		List<TextDTO> names = new ArrayList<>();
		for (LocaleEnum val : LocaleEnum.values()) {
			names.add(new TextDTO(val.getCode(), "names"));
		}
		variante.setName(names);
		List<TextDTO> descriptions = new ArrayList<>();
		for (LocaleEnum val : LocaleEnum.values()) {
			descriptions.add(new TextDTO(val.getCode(), "descriptions"));
		}
		variante.setName(descriptions);
		variante.setBombe(2);
		variante.setBonus(new Integer[15]);
		variante.setShadow(0f);
		variante.setDefaultBackground(new DefaultTextureDTO(SpriteEnum.LEVEL, 0));
		variante.setDefaultBrickAnimation(SpriteEnum.LEVEL1);
		variante.setDefaultWall(new DefaultTextureDTO(SpriteEnum.LEVEL, 0));
		return variante;
	}

	public void updateLevelName(LocaleEnum locale, String newValue) {

	}

	public void addHole(int x, int y) {
		if (currentVariante != null) {
			currentVariante.getHole().add(new PositionableDTO(x, y));
		}
	}

	public void addRail(int x, int y) {
		if (currentVariante != null) {
			currentVariante.getRail().add(new PositionableDTO(x, y));
		}
	}

	public void addTrolley(int x, int y) {
		if (currentVariante != null) {
			currentVariante.getTrolley().add(new PositionableDTO(x, y));
		}
	}

	public void addInterrupter(int x, int y) {
		if (currentVariante != null) {
			currentVariante.getInterrupter().add(new PositionableDTO(x, y));
		}
	}

	public void addMine(int x, int y) {
		if (currentVariante != null) {
			currentVariante.getMine().add(new PositionableDTO(x, y));
		}
	}

	public void addTeleporter(int x, int y) {
		if (currentVariante != null) {
			currentVariante.getTeleporter().add(new PositionableDTO(x, y));
		}
	}

	public void addWall(int x, int y, boolean draw, int textureIndex) {
		if (currentVariante != null) {
			currentVariante.getWall().add(new WallDTO(x, y, draw, new TextureDTO(SpriteEnum.BACKGROUND, textureIndex)));
		}
	}

	public void addCustomBackgroundTexture(int x, int y, int index) {
		if (currentVariante != null) {
			currentVariante.getCustomBackgroundTexture().add(new CustomTextureDTO(x, y, SpriteEnum.LEVEL, index));
		}
	}

	public void addCustomForegroundTexture(int x, int y, int index) {
		if (currentVariante != null) {
			currentVariante.getCustomForegroundTexture().add(new CustomTextureDTO(x, y, SpriteEnum.SKY, index));
		}
	}

	public void addStartPlayer(int x, int y) {
		if (currentVariante != null) {
			currentVariante.getStartPlayer().add(new PositionableDTO(x, y));
		}
	}

	public void removeHole(int x, int y) {
		if (currentVariante != null) {
			currentVariante.getHole().removeIf(el -> el.getX() == x && el.getY() == y);
		}
	}

	public void removeRail(int x, int y) {
		if (currentVariante != null) {
			currentVariante.getRail().removeIf(el -> el.getX() == x && el.getY() == y);
		}
	}

	public void removeTrolley(int x, int y) {
		if (currentVariante != null) {
			currentVariante.getTrolley().removeIf(el -> el.getX() == x && el.getY() == y);
		}
	}

	public void removeInterrupter(int x, int y) {
		if (currentVariante != null) {
			currentVariante.getInterrupter().removeIf(el -> el.getX() == x && el.getY() == y);
		}
	}

	public void removeMine(int x, int y) {
		if (currentVariante != null) {
			currentVariante.getMine().removeIf(el -> el.getX() == x && el.getY() == y);
		}
	}

	public void removeTeleporter(int x, int y) {
		if (currentVariante != null) {
			currentVariante.getTeleporter().removeIf(el -> el.getX() == x && el.getY() == y);
		}
	}

	public void removeWall(int x, int y, boolean draw, TextureDTO extureDTO) {
		if (currentVariante != null) {
			currentVariante.getWall().removeIf(el -> el.getX() == x && el.getY() == y);
		}
	}

	public void removeCustomBackgroundTexture(int x, int y) {
		if (currentVariante != null) {
			currentVariante.getCustomBackgroundTexture().removeIf(el -> el.getX() == x && el.getY() == y);
		}
	}

	public void removeCustomForegroundTexture(int x, int y) {
		if (currentVariante != null) {
			currentVariante.getCustomForegroundTexture().removeIf(el -> el.getX() == x && el.getY() == y);
		}
	}

	public void removeStartPlayer(int x, int y) {
		if (currentVariante != null) {
			currentVariante.getStartPlayer().removeIf(el -> el.getX() == x && el.getY() == y);
		}
	}
}
