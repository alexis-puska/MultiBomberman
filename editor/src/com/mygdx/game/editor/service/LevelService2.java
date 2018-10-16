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
		if (currentLevel != null) {
			int pos = levelFileDTO.getLevels().indexOf(currentLevel);
			if (pos != levelFileDTO.getLevels().size() - 1) {
				currentLevel = levelFileDTO.getLevels().get(pos + 1);
				if (currentLevel.getVariante() != null && !currentLevel.getVariante().isEmpty()) {
					currentVariante = currentLevel.getVariante().get(0);
				} else {
					currentVariante = null;
				}
			} else {
				currentLevel = levelFileDTO.getLevels().get(0);
				if (currentLevel.getVariante() != null && !currentLevel.getVariante().isEmpty()) {
					currentVariante = currentLevel.getVariante().get(0);
				} else {
					currentVariante = null;
				}
			}
		}
	}

	public void previousLevel() {
		if (currentLevel != null) {
			int pos = levelFileDTO.getLevels().indexOf(currentLevel);
			if (pos > 0) {
				currentLevel = levelFileDTO.getLevels().get(pos - 1);
				if (currentLevel.getVariante() != null && !currentLevel.getVariante().isEmpty()) {
					currentVariante = currentLevel.getVariante().get(0);
				} else {
					currentVariante = null;
				}
			} else {
				currentLevel = levelFileDTO.getLevels().get(levelFileDTO.getLevels().size() - 1);
				if (currentLevel.getVariante() != null && !currentLevel.getVariante().isEmpty()) {
					currentVariante = currentLevel.getVariante().get(0);
				} else {
					currentVariante = null;
				}
			}
		}
	}

	public void addLevel() {
		if (levelFileDTO == null) {
			createLevelFileDTO();
		}
		levelFileDTO.getLevels().add(createNewLevel());
		currentLevel = levelFileDTO.getLevels().get(levelFileDTO.getLevels().size() - 1);
		currentVariante = currentLevel.getVariante().get(0);
	}

	public void deleteLevel() {
		if (currentLevel != null) {
			levelFileDTO.getLevels().remove(currentLevel);
			if (levelFileDTO.getLevels().size() > 0) {
				currentLevel = levelFileDTO.getLevels().get(0);
			} else {
				currentLevel = null;
			}
			if (currentLevel != null && currentLevel.getVariante() != null && !currentLevel.getVariante().isEmpty()) {
				currentVariante = currentLevel.getVariante().get(0);
			} else {
				currentVariante = null;
			}
		}
	}

	private void createLevelFileDTO() {
		this.levelFileDTO = new LevelFileDTO();
		this.levelFileDTO.setLevels(new ArrayList<>());
	}

	private LevelDTO createNewLevel() {
		LevelDTO level = new LevelDTO();
		List<TextDTO> names = new ArrayList<>();
		for (LocaleEnum val : LocaleEnum.values()) {
			names.add(new TextDTO(val, "CHANGE !"));
		}
		level.setName(names);
		level.setVariante(new ArrayList<>());
		level.getVariante().add(createNewVariante());
		return level;
	}

	public void nextVariante() {
		if (currentVariante != null) {
			int pos = currentLevel.getVariante().indexOf(currentVariante);
			if (pos == currentLevel.getVariante().size() - 1) {
				currentVariante = currentLevel.getVariante().get(0);
			} else {
				currentVariante = currentLevel.getVariante().get(pos + 1);
			}
		}
	}

	public void previousVariante() {
		if (currentVariante != null) {
			int pos = currentLevel.getVariante().indexOf(currentVariante);
			if (pos == 0) {
				currentVariante = currentLevel.getVariante().get(currentLevel.getVariante().size() - 1);
			} else {
				currentVariante = currentLevel.getVariante().get(pos - 1);
			}
		}
	}

	public void addVariante() {
		if (currentLevel != null) {
			currentLevel.getVariante().add(createNewVariante());
			currentVariante = currentLevel.getVariante().get(currentLevel.getVariante().size() - 1);
		}
	}

	public void deleteVariante() {
		if (currentVariante != null) {
			currentLevel.getVariante().remove(currentVariante);
			if (currentLevel.getVariante().size() > 0) {
				currentVariante = currentLevel.getVariante().get(0);
			}
		}
	}

	private VarianteDTO createNewVariante() {
		VarianteDTO variante = new VarianteDTO();
		List<TextDTO> names = new ArrayList<>();
		for (LocaleEnum val : LocaleEnum.values()) {
			names.add(new TextDTO(val, "names"));
		}
		variante.setName(names);
		List<TextDTO> descriptions = new ArrayList<>();
		for (LocaleEnum val : LocaleEnum.values()) {
			descriptions.add(new TextDTO(val, "CHANGE IT !"));
		}
		variante.setDescription(descriptions);
		variante.setBombe(2);
		variante.setBonus(new Integer[15]);
		variante.setShadow(0f);
		variante.setDefaultBackground(new DefaultTextureDTO(SpriteEnum.LEVEL, 0));
		variante.setDefaultBrickAnimation(SpriteEnum.LEVEL1);
		variante.setDefaultWall(new DefaultTextureDTO(SpriteEnum.LEVEL, 0));

		variante.setCustomBackgroundTexture(new ArrayList<>());
		variante.setCustomForegroundTexture(new ArrayList<>());
		variante.setHole(new ArrayList<>());
		variante.setInterrupter(new ArrayList<>());
		variante.setMine(new ArrayList<>());
		variante.setRail(new ArrayList<>());
		variante.setStartPlayer(new ArrayList<>());
		variante.setTeleporter(new ArrayList<>());
		variante.setTrolley(new ArrayList<>());
		variante.setWall(new ArrayList<>());
		for (int i = 0; i < 35; i++) {
			variante.getWall().add(new WallDTO(i, 0));
			variante.getWall().add(new WallDTO(i, 20));
		}
		for (int i = 1; i < 20; i++) {
			variante.getWall().add(new WallDTO(0, i));
			variante.getWall().add(new WallDTO(34, i));
		}

		for (int i = 0; i < 34; i++) {
			variante.getWall().add(new WallDTO(i, 0));
			variante.getWall().add(new WallDTO(i, 20));
		}
		for (int i = 1; i < 19; i++) {
			for (int j = 1; j < 34; j++) {
				if (i % 2 == 0 && j % 2 == 0) {
					variante.getWall().add(new WallDTO(j, i));
				}
			}
		}
		return variante;
	}

	public String getLevelPosition() {
		if (currentLevel != null) {
			int pos = levelFileDTO.getLevels().indexOf(currentLevel);
			return (pos + 1) + "/" + levelFileDTO.getLevels().size();
		} else {
			return "0 / 0";
		}
	}

	public String getVariantePosition() {
		if (currentLevel != null && currentVariante != null) {
			int pos = currentLevel.getVariante().indexOf(currentVariante);
			return (pos + 1) + "/" + currentLevel.getVariante().size();
		} else {
			return "0 / 0";
		}
	}

	public String getLevelName(LocaleEnum lang) {
		if (this.currentLevel != null) {
			if (this.currentLevel.getName() != null) {
				for (TextDTO ln : this.currentLevel.getName()) {
					if (ln.getLang().equals(lang)) {
						return ln.getValue();
					}
				}
				this.currentLevel.getName().add(new TextDTO(lang, "CHANGE IT !"));
				return "CHANGE IT !";
			} else {
				this.currentLevel.setName(new ArrayList<>());
				this.currentLevel.getName().add(new TextDTO(LocaleEnum.ENGLISH, ""));
				this.currentLevel.getName().add(new TextDTO(LocaleEnum.FRENCH, ""));
			}
		}
		return "";
	}

	public void setLevelName(LocaleEnum lang, String name) {
		if (this.currentLevel != null) {
			TextDTO tmp = null;
			for (TextDTO ln : this.currentLevel.getName()) {
				if (ln.getLang().equals(lang)) {
					tmp = ln;
					break;
				}
			}
			if (tmp != null) {
				this.currentLevel.getName().remove(tmp);
				tmp.setValue(name);
				this.currentLevel.getName().add(tmp);
			} else {
				tmp = new TextDTO(lang, name);
				this.currentLevel.getName().add(tmp);
			}
		}
	}

	public String getVarianteName(LocaleEnum lang) {
		if (this.currentVariante != null) {
			if (this.currentVariante.getName() != null) {
				for (TextDTO ln : this.currentVariante.getName()) {
					if (ln.getLang().equals(lang)) {
						return ln.getValue();
					}
				}
				this.currentVariante.getName().add(new TextDTO(lang, "CHANGE IT !"));
				return "CHANGE IT !";
			} else {
				this.currentVariante.setName(new ArrayList<>());
				this.currentVariante.getName().add(new TextDTO(LocaleEnum.ENGLISH, ""));
				this.currentVariante.getName().add(new TextDTO(LocaleEnum.FRENCH, ""));
			}
		}
		return "";
	}

	public void setVarianteName(LocaleEnum lang, String name) {
		if (this.currentVariante != null) {
			TextDTO tmp = null;
			for (TextDTO ln : this.currentVariante.getName()) {
				if (ln.getLang().equals(lang)) {
					tmp = ln;
					break;
				}
			}
			if (tmp != null) {
				this.currentVariante.getName().remove(tmp);
				tmp.setValue(name);
				this.currentVariante.getName().add(tmp);
			} else {
				tmp = new TextDTO(lang, name);
				this.currentVariante.getName().add(tmp);
			}
		}
	}

	public String getVarianteDescription(LocaleEnum lang) {
		if (this.currentVariante != null) {
			if (this.currentVariante.getDescription() != null) {
				for (TextDTO ln : this.currentVariante.getDescription()) {
					if (ln.getLang().equals(lang)) {
						return ln.getValue();
					}
				}
				this.currentVariante.getDescription().add(new TextDTO(lang, "CHANGE IT !"));
				return "CHANGE IT !";
			} else {
				this.currentVariante.setDescription(new ArrayList<>());
				this.currentVariante.getDescription().add(new TextDTO(LocaleEnum.ENGLISH, ""));
				this.currentVariante.getDescription().add(new TextDTO(LocaleEnum.FRENCH, ""));
			}
		}
		return "";
	}

	public void setVarianteDescription(LocaleEnum lang, String name) {
		if (this.currentVariante != null) {
			TextDTO tmp = null;
			for (TextDTO ln : this.currentVariante.getDescription()) {
				if (ln.getLang().equals(lang)) {
					tmp = ln;
					break;
				}
			}
			if (tmp != null) {
				this.currentVariante.getDescription().remove(tmp);
				tmp.setValue(name);
				this.currentVariante.getDescription().add(tmp);
			} else {
				tmp = new TextDTO(lang, name);
				this.currentVariante.getDescription().add(tmp);
			}
		}
	}

	public float getShadow() {
		if (this.currentVariante != null) {
			return this.currentVariante.getShadow();
		}
		return 0f;
	}

	public void setShadow(Float value) {
		if (this.currentVariante != null) {
			this.currentVariante.setShadow(value);
		}
	}

	public int getBombe() {
		if (this.currentVariante != null) {
			if (this.currentVariante.getBombe() >= 1) {
				return this.currentVariante.getBombe();
			}
		}
		return 0;
	}

	public void setBombe(int value) {
		if (this.currentVariante != null) {
			this.currentVariante.setBombe(value);
		}
	}

	public int getStrenght() {
		if (this.currentVariante != null) {
			if (this.currentVariante.getStrenght() >= 1) {
				return this.currentVariante.getStrenght();
			}
		}
		return 1;
	}

	public void setStrenght(int value) {
		if (this.currentVariante != null) {
			this.currentVariante.setStrenght(value);
		}
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

	public void removeWall(int x, int y) {
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

	public void setFillWithBrick(boolean selected) {
		if (currentVariante != null) {
			currentVariante.setFillWithBrick(selected);
		}
	}

	public boolean isFillWithBrick() {
		if (currentVariante != null) {
			return currentVariante.isFillWithBrick();
		}
		return false;
	}

	public void setDefaultBackgroungTexture(int index) {
		if (currentVariante != null) {
			currentVariante.setDefaultBackground(new DefaultTextureDTO(SpriteEnum.LEVEL, index));
		}
	}

	public void setDefaultWallTexture(int index) {
		if (currentVariante != null) {
			currentVariante.setDefaultWall(new DefaultTextureDTO(SpriteEnum.LEVEL, index));
		}
	}

}
