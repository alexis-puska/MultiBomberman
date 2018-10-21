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
import com.mygdx.constante.Constante;
import com.mygdx.dto.level.CustomTextureDTO;
import com.mygdx.dto.level.LevelDTO;
import com.mygdx.dto.level.LevelFileDTO;
import com.mygdx.dto.level.LevelGroupDTO;
import com.mygdx.dto.level.TextureDTO;
import com.mygdx.dto.level.WallDTO;
import com.mygdx.dto.level.common.DefaultTextureDTO;
import com.mygdx.dto.level.common.PositionableDTO;
import com.mygdx.dto.level.common.TextDTO;
import com.mygdx.enumeration.LocaleEnum;
import com.mygdx.enumeration.SpriteEnum;

public class LevelService2 {

	private static final Logger LOG = LogManager.getLogger(LevelService2.class);

	private static final String CHANGE_IT = "CHANGE !!!";

	private final ObjectMapper objectMapper;
	private LevelFileDTO levelFileDTO;

	private LevelGroupDTO currentLevelGroup;
	private LevelDTO currentLevel;

	public LevelService2() {
		this.objectMapper = new ObjectMapper();
	}

	/**
	 * Load JSON LevelGroup file and init map with all levelGroup and level
	 * 
	 * @param in inputStream of file
	 * @return LevelGroup to Edit
	 */
	public void load(InputStream in) {
		levelFileDTO = null;
		try {
			levelFileDTO = objectMapper.readValue(in, LevelFileDTO.class);
			if (!levelFileDTO.getLevelGroups().isEmpty()) {
				currentLevelGroup = levelFileDTO.getLevelGroups().get(0);
				if (!currentLevelGroup.getLevel().isEmpty()) {
					currentLevel = currentLevelGroup.getLevel().get(0);
				}
			} else {
				addLevelGroup();
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
			LOG.info("START Write levelGroup json file : " + file.getAbsolutePath());
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, levelFileDTO);
		} catch (JsonProcessingException e) {
			LOG.error("JsonProcessingException : " + e.getMessage());
		} catch (IOException e) {
			LOG.error("IOException : " + e.getMessage());
		}
		LOG.info("Write levelGroup json file : SUCCESS");
	}

	public LevelGroupDTO getCurrentLevelGroup() {
		return this.currentLevelGroup;
	}

	public LevelDTO getCurrentLevel() {
		return this.currentLevel;
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

	public void addLevelGroup() {
		if (levelFileDTO == null) {
			createLevelFileDTO();
		}
		levelFileDTO.getLevelGroups().add(createNewLevelGroup());
		currentLevelGroup = levelFileDTO.getLevelGroups().get(levelFileDTO.getLevelGroups().size() - 1);
		currentLevel = currentLevelGroup.getLevel().get(0);
	}

	public void deleteLevelGroup() {
		if (currentLevelGroup != null) {
			levelFileDTO.getLevelGroups().remove(currentLevelGroup);
			if (levelFileDTO.getLevelGroups().size() > 0) {
				currentLevelGroup = levelFileDTO.getLevelGroups().get(0);
			} else {
				currentLevelGroup = null;
			}
			if (currentLevelGroup != null && currentLevelGroup.getLevel() != null
					&& !currentLevelGroup.getLevel().isEmpty()) {
				currentLevel = currentLevelGroup.getLevel().get(0);
			} else {
				currentLevel = null;
			}
		}
	}

	public void copyLevelGroupAndSelectIt() {
		if (currentLevelGroup != null) {
			LevelGroupDTO copy = new LevelGroupDTO(currentLevelGroup);
			levelFileDTO.getLevelGroups().add(copy);
			currentLevelGroup = levelFileDTO.getLevelGroups().get(levelFileDTO.getLevelGroups().size() - 1);
			currentLevel = currentLevelGroup.getLevel().get(0);
		}
	}

	public void copyLevelAndSelectIt() {
		if (currentLevelGroup != null) {
			LevelDTO copy = new LevelDTO(currentLevel);
			currentLevelGroup.getLevel().add(copy);
			currentLevel = currentLevelGroup.getLevel().get(currentLevelGroup.getLevel().size() - 1);
		}
	}

	private void createLevelFileDTO() {
		this.levelFileDTO = new LevelFileDTO();
		this.levelFileDTO.setLevelGroups(new ArrayList<>());
	}

	private LevelGroupDTO createNewLevelGroup() {
		LevelGroupDTO levelGroup = new LevelGroupDTO();
		List<TextDTO> names = new ArrayList<>();
		for (LocaleEnum val : LocaleEnum.values()) {
			names.add(new TextDTO(val, CHANGE_IT));
		}
		levelGroup.setName(names);
		levelGroup.setLevel(new ArrayList<>());
		levelGroup.getLevel().add(createNewLevel());
		return levelGroup;
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

	public void addLevel() {
		if (currentLevelGroup != null) {
			currentLevelGroup.getLevel().add(createNewLevel());
			currentLevel = currentLevelGroup.getLevel().get(currentLevelGroup.getLevel().size() - 1);
		}
	}

	public void deleteLevel() {
		if (currentLevel != null) {
			currentLevelGroup.getLevel().remove(currentLevel);
			if (currentLevelGroup.getLevel().size() > 0) {
				currentLevel = currentLevelGroup.getLevel().get(0);
			}
		}
	}

	private LevelDTO createNewLevel() {
		LevelDTO level = new LevelDTO();
		List<TextDTO> names = new ArrayList<>();
		for (LocaleEnum val : LocaleEnum.values()) {
			names.add(new TextDTO(val, "names"));
		}
		level.setName(names);
		List<TextDTO> descriptions = new ArrayList<>();
		for (LocaleEnum val : LocaleEnum.values()) {
			descriptions.add(new TextDTO(val, CHANGE_IT));
		}
		level.setDescription(descriptions);
		level.setBombe(2);
		Integer[] bonus = new Integer[Constante.MAX_BONUS];
		for (int i = 0; i < Constante.MAX_BONUS; i++) {
			bonus[i] = 0;
		}
		level.setBonus(bonus);
		level.setShadow(0f);
		level.setDefaultBackground(new DefaultTextureDTO(SpriteEnum.LEVEL, 0));
		level.setDefaultBrickAnimation(SpriteEnum.LEVEL1);
		level.setDefaultWall(new DefaultTextureDTO(SpriteEnum.LEVEL, 0));

		level.setCustomBackgroundTexture(new ArrayList<>());
		level.setCustomForegroundTexture(new ArrayList<>());
		level.setHole(new ArrayList<>());
		level.setInterrupter(new ArrayList<>());
		level.setMine(new ArrayList<>());
		level.setRail(new ArrayList<>());
		level.setStartPlayer(new ArrayList<>());
		level.setTeleporter(new ArrayList<>());
		level.setTrolley(new ArrayList<>());
		level.setWall(new ArrayList<>());
		for (int i = 0; i < 35; i++) {
			level.getWall().add(new WallDTO(i, 0));
			level.getWall().add(new WallDTO(i, 20));
		}
		for (int i = 1; i < 20; i++) {
			level.getWall().add(new WallDTO(0, i));
			level.getWall().add(new WallDTO(34, i));
		}

		for (int i = 0; i < 34; i++) {
			level.getWall().add(new WallDTO(i, 0));
			level.getWall().add(new WallDTO(i, 20));
		}
		for (int i = 1; i < 19; i++) {
			for (int j = 1; j < 34; j++) {
				if (i % 2 == 0 && j % 2 == 0) {
					level.getWall().add(new WallDTO(j, i));
				}
			}
		}
		return level;
	}

	public String getLevelGroupPosition() {
		if (currentLevelGroup != null) {
			int pos = levelFileDTO.getLevelGroups().indexOf(currentLevelGroup);
			return (pos + 1) + "/" + levelFileDTO.getLevelGroups().size();
		} else {
			return "0 / 0";
		}
	}

	public String getLevelPosition() {
		if (currentLevelGroup != null && currentLevel != null) {
			int pos = currentLevelGroup.getLevel().indexOf(currentLevel);
			return (pos + 1) + "/" + currentLevelGroup.getLevel().size();
		} else {
			return "0 / 0";
		}
	}

	public String getLevelGroupName(LocaleEnum lang) {
		if (this.currentLevelGroup != null) {
			if (this.currentLevelGroup.getName() != null) {
				for (TextDTO ln : this.currentLevelGroup.getName()) {
					if (ln.getLang().equals(lang)) {
						return ln.getValue();
					}
				}
				this.currentLevelGroup.getName().add(new TextDTO(lang, CHANGE_IT));
				return CHANGE_IT;
			} else {
				this.currentLevelGroup.setName(new ArrayList<>());
				this.currentLevelGroup.getName().add(new TextDTO(LocaleEnum.ENGLISH, ""));
				this.currentLevelGroup.getName().add(new TextDTO(LocaleEnum.FRENCH, ""));
			}
		}
		return "";
	}

	public void setLevelGroupName(LocaleEnum lang, String name) {
		if (this.currentLevelGroup != null) {
			TextDTO tmp = null;
			for (TextDTO ln : this.currentLevelGroup.getName()) {
				if (ln.getLang().equals(lang)) {
					tmp = ln;
					break;
				}
			}
			if (tmp != null) {
				this.currentLevelGroup.getName().remove(tmp);
				tmp.setValue(name);
				this.currentLevelGroup.getName().add(tmp);
			} else {
				tmp = new TextDTO(lang, name);
				this.currentLevelGroup.getName().add(tmp);
			}
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
				this.currentLevel.getName().add(new TextDTO(lang, CHANGE_IT));
				return CHANGE_IT;
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

	public String getLevelDescription(LocaleEnum lang) {
		if (this.currentLevel != null) {
			if (this.currentLevel.getDescription() != null) {
				for (TextDTO ln : this.currentLevel.getDescription()) {
					if (ln.getLang().equals(lang)) {
						return ln.getValue();
					}
				}
				this.currentLevel.getDescription().add(new TextDTO(lang, CHANGE_IT));
				return CHANGE_IT;
			} else {
				this.currentLevel.setDescription(new ArrayList<>());
				this.currentLevel.getDescription().add(new TextDTO(LocaleEnum.ENGLISH, ""));
				this.currentLevel.getDescription().add(new TextDTO(LocaleEnum.FRENCH, ""));
			}
		}
		return "";
	}

	public void setLevelDescription(LocaleEnum lang, String name) {
		if (this.currentLevel != null) {
			TextDTO tmp = null;
			for (TextDTO ln : this.currentLevel.getDescription()) {
				if (ln.getLang().equals(lang)) {
					tmp = ln;
					break;
				}
			}
			if (tmp != null) {
				this.currentLevel.getDescription().remove(tmp);
				tmp.setValue(name);
				this.currentLevel.getDescription().add(tmp);
			} else {
				tmp = new TextDTO(lang, name);
				this.currentLevel.getDescription().add(tmp);
			}
		}
	}

	public float getShadow() {
		if (this.currentLevel != null) {
			return this.currentLevel.getShadow();
		}
		return 0f;
	}

	public void setShadow(Float value) {
		if (this.currentLevel != null) {
			this.currentLevel.setShadow(value);
		}
	}

	public int getBombe() {
		if (this.currentLevel != null && this.currentLevel.getBombe() >= 1) {
			return this.currentLevel.getBombe();
		}
		return 0;
	}

	public void setBombe(int value) {
		if (this.currentLevel != null) {
			this.currentLevel.setBombe(value);
		}
	}

	public int getStrenght() {
		if (this.currentLevel != null && this.currentLevel.getStrenght() >= 1) {
			return this.currentLevel.getStrenght();
		}
		return 1;
	}

	public void setStrenght(int value) {
		if (this.currentLevel != null) {
			this.currentLevel.setStrenght(value);
		}
	}

	private boolean positionContainsWall(int x, int y) {
		return currentLevel.getWall().contains(new PositionableDTO(x, y));
	}

	private boolean canSetWall(int x, int y) {
		PositionableDTO tmp = new PositionableDTO(x, y);
		if (currentLevel.getHole().contains(tmp)) {
			return false;
		}
		if (currentLevel.getInterrupter().contains(tmp)) {
			return false;
		}
		if (currentLevel.getMine().contains(tmp)) {
			return false;
		}
		if (currentLevel.getRail().contains(tmp)) {
			return false;
		}
		if (currentLevel.getStartPlayer().contains(tmp)) {
			return false;
		}
		if (currentLevel.getTeleporter().contains(tmp)) {
			return false;
		}
		return !currentLevel.getTrolley().contains(tmp);
	}

	public void addHole(int x, int y) {
		if (currentLevel != null) {
			PositionableDTO t = new PositionableDTO(x, y);
			if (!currentLevel.getRail().contains(t) && !positionContainsWall(x, y)) {
				currentLevel.getHole().add(t);
			}
		}
	}

	public void addRail(int x, int y) {
		if (currentLevel != null) {
			PositionableDTO t = new PositionableDTO(x, y);
			if (!currentLevel.getRail().contains(t) && !positionContainsWall(x, y)) {
				currentLevel.getRail().add(t);
			}
		}
	}

	public void addTrolley(int x, int y) {
		if (currentLevel != null) {
			PositionableDTO t = new PositionableDTO(x, y);
			if (!currentLevel.getTrolley().contains(t) && !positionContainsWall(x, y)) {
				currentLevel.getTrolley().add(new PositionableDTO(x, y));
			}
		}
	}

	public void addInterrupter(int x, int y) {
		if (currentLevel != null) {
			PositionableDTO t = new PositionableDTO(x, y);
			if (!currentLevel.getInterrupter().contains(t) && !positionContainsWall(x, y)) {
				currentLevel.getInterrupter().add(new PositionableDTO(x, y));
			}
		}
	}

	public void addMine(int x, int y) {
		if (currentLevel != null) {
			PositionableDTO t = new PositionableDTO(x, y);
			if (!currentLevel.getMine().contains(t) && !positionContainsWall(x, y)) {
				currentLevel.getMine().add(new PositionableDTO(x, y));
			}
		}
	}

	public void addTeleporter(int x, int y) {
		if (currentLevel != null) {
			PositionableDTO t = new PositionableDTO(x, y);
			if (!currentLevel.getTeleporter().contains(t) && !positionContainsWall(x, y)) {
				currentLevel.getTeleporter().add(new PositionableDTO(x, y));
			}
		}
	}

	public void addWall(int x, int y) {
		if (currentLevel != null) {
			PositionableDTO t = new PositionableDTO(x, y);
			if (!currentLevel.getWall().contains(t) && canSetWall(x, y)) {
				currentLevel.getWall().add(new WallDTO(x, y));
			}
		}
	}

	public void customizeWall(int x, int y, int textureIndex) {
		if (currentLevel != null) {
			currentLevel.getWall().stream().forEach(obj -> {
				if (obj.getX() == x && obj.getY() == y) {
					obj.setDraw(true);
					obj.setTexture(new TextureDTO(SpriteEnum.LEVEL, textureIndex));
				}
			});
		}
	}

	public void addCustomBackgroundTexture(int x, int y, int index) {
		if (currentLevel != null) {
			PositionableDTO t = new PositionableDTO(x, y);
			if (!currentLevel.getCustomBackgroundTexture().contains(t)) {
				currentLevel.getCustomBackgroundTexture().add(new CustomTextureDTO(x, y, SpriteEnum.LEVEL, index));
			}
		}
	}

	public void addCustomForegroundTexture(int x, int y, int index) {
		if (currentLevel != null) {
			PositionableDTO t = new PositionableDTO(x, y);
			if (!currentLevel.getCustomForegroundTexture().contains(t)) {
				currentLevel.getCustomForegroundTexture().add(new CustomTextureDTO(x, y, SpriteEnum.SKY, index));
			}
		}
	}

	public void addStartPlayer(int x, int y) {
		if (currentLevel != null) {
			PositionableDTO t = new PositionableDTO(x, y);
			if (!currentLevel.getStartPlayer().contains(t) && !positionContainsWall(x, y)
					&& currentLevel.getStartPlayer().size() < 16) {
				currentLevel.getStartPlayer().add(new PositionableDTO(x, y));
			}
		}
	}

	public void removeHole(int x, int y) {
		if (currentLevel != null) {
			currentLevel.getHole().removeIf(el -> el.getX() == x && el.getY() == y);
		}
	}

	public void removeRail(int x, int y) {
		if (currentLevel != null) {
			currentLevel.getRail().removeIf(el -> el.getX() == x && el.getY() == y);
		}
	}

	public void removeTrolley(int x, int y) {
		if (currentLevel != null) {
			currentLevel.getTrolley().removeIf(el -> el.getX() == x && el.getY() == y);
		}
	}

	public void removeInterrupter(int x, int y) {
		if (currentLevel != null) {
			currentLevel.getInterrupter().removeIf(el -> el.getX() == x && el.getY() == y);
		}
	}

	public void removeMine(int x, int y) {
		if (currentLevel != null) {
			currentLevel.getMine().removeIf(el -> el.getX() == x && el.getY() == y);
		}
	}

	public void removeTeleporter(int x, int y) {
		if (currentLevel != null) {
			currentLevel.getTeleporter().removeIf(el -> el.getX() == x && el.getY() == y);
		}
	}

	public void removeWall(int x, int y) {
		if (currentLevel != null) {
			currentLevel.getWall().removeIf(el -> el.getX() == x && el.getY() == y);
		}
	}

	public void removeCustomBackgroundTexture(int x, int y) {
		if (currentLevel != null) {
			currentLevel.getCustomBackgroundTexture().removeIf(el -> el.getX() == x && el.getY() == y);
		}
	}

	public void removeCustomForegroundTexture(int x, int y) {
		if (currentLevel != null) {
			currentLevel.getCustomForegroundTexture().removeIf(el -> el.getX() == x && el.getY() == y);
		}
	}

	public void removeStartPlayer(int x, int y) {
		if (currentLevel != null) {
			currentLevel.getStartPlayer().removeIf(el -> el.getX() == x && el.getY() == y);
		}
	}

	public void removeCustomizationWall(int posX, int posY) {
		if (currentLevel != null) {
			currentLevel.getWall().stream().forEach(obj -> {
				if (obj.getX() == posX && obj.getY() == posY) {
					obj.setDraw(true);
					obj.setTexture(null);
				}
			});
		}
	}

	public void setWallTransparent(int posX, int posY) {
		if (currentLevel != null) {
			currentLevel.getWall().stream().forEach(obj -> {
				if (obj.getX() == posX && obj.getY() == posY) {
					obj.setDraw(false);
					obj.setTexture(null);
				}
			});
		}
	}

	public void setFillWithBrick(boolean selected) {
		if (currentLevel != null) {
			currentLevel.setFillWithBrick(selected);
		}
	}

	public boolean isFillWithBrick() {
		if (currentLevel != null) {
			return currentLevel.isFillWithBrick();
		}
		return false;
	}

	public void setDefaultBackgroungTexture(int index) {
		if (currentLevel != null) {
			currentLevel.setDefaultBackground(new DefaultTextureDTO(SpriteEnum.LEVEL, index));
		}
	}

	public void setDefaultWallTexture(int index) {
		if (currentLevel != null) {
			currentLevel.setDefaultWall(new DefaultTextureDTO(SpriteEnum.LEVEL, index));
		}
	}

	public void setBonus(int i, int intValue) {
		if (currentLevel != null) {
			if (currentLevel.getBonus() == null) {
				currentLevel.setBonus(new Integer[Constante.MAX_BONUS]);
			}
			Integer[] bonus = currentLevel.getBonus();
			bonus[i] = intValue;
			currentLevel.setBonus(bonus);
		}
	}

	public int getBonus(int i) {
		if (currentLevel != null && currentLevel.getBonus() != null) {
			return currentLevel.getBonus()[i];
		}
		return 0;
	}

	public void setDefaultBrickAnimtion(SpriteEnum se) {
		if (currentLevel != null) {
			currentLevel.setDefaultBrickAnimation(se);
		}
	}

	public SpriteEnum getDefaultBrickAnimation() {
		if (currentLevel != null) {
			return currentLevel.getDefaultBrickAnimation();
		}
		return null;
	}

	public void setPreviewIndex(int index) {
		if (currentLevel != null) {
			currentLevel.setIndexPreview(index);
		}
	}

	public int getPreviewIndex() {
		if (currentLevel != null) {
			return currentLevel.getIndexPreview();
		}
		return 0;
	}
}
