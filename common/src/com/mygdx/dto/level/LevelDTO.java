package com.mygdx.dto.level;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mygdx.constante.Constante;
import com.mygdx.dto.level.common.DefaultTextureDTO;
import com.mygdx.dto.level.common.PositionableDTO;
import com.mygdx.dto.level.common.TextDTO;
import com.mygdx.enumeration.SpriteEnum;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class LevelDTO implements Serializable {

	private static final long serialVersionUID = -2962754568139006753L;

	private List<TextDTO> name;
	private List<TextDTO> description;

	private int indexPreview;
	private int bombe;
	private int strenght;
	private float shadow;
	private Integer[] bonus;
	private boolean fillWithBrick;
	private boolean footInWater;
	private boolean levelUnderWater;
	private boolean startWithKickPower;
	private DefaultTextureDTO defaultBackground;
	private DefaultTextureDTO defaultWall;
	private SpriteEnum defaultBrickAnimation;

	private List<PositionableDTO> hole;
	private List<PositionableDTO> rail;
	private List<PositionableDTO> interrupter;
	private List<PositionableDTO> mine;
	private List<PositionableDTO> trolley;
	private List<PositionableDTO> teleporter;
	private List<WallDTO> wall;

	private List<CustomTextureDTO> customBackgroundTexture;
	private List<CustomTextureDTO> customForegroundTexture;

	private List<PositionableDTO> startPlayer;

	public LevelDTO(LevelDTO original) {
		this.name = new ArrayList<>();
		original.getName().stream().forEach(n -> this.name.add(new TextDTO(n)));
		this.description = new ArrayList<>();
		original.getDescription().stream().forEach(n -> this.description.add(new TextDTO(n)));
		this.indexPreview = original.indexPreview;
		this.bombe = original.bombe;
		this.strenght = original.strenght;
		this.shadow = original.shadow;
		this.bonus = new Integer[Constante.MAX_BONUS];
		for (int i = 0; i < Constante.MAX_BONUS; i++) {
			this.bonus[i] = original.bonus[i];
		}
		this.fillWithBrick = original.isFillWithBrick();
		this.footInWater = original.isFootInWater();
		this.levelUnderWater = original.isLevelUnderWater();
		this.startWithKickPower = original.isStartWithKickPower();
		this.defaultBackground = new DefaultTextureDTO(original.defaultBackground);
		this.defaultWall = new DefaultTextureDTO(original.defaultWall);
		this.defaultBrickAnimation = original.defaultBrickAnimation;
		this.hole = new ArrayList<>();
		original.hole.stream().forEach(n -> this.hole.add(new PositionableDTO(n)));
		this.rail = new ArrayList<>();
		original.rail.stream().forEach(n -> this.rail.add(new PositionableDTO(n)));
		this.interrupter = new ArrayList<>();
		original.interrupter.stream().forEach(n -> this.interrupter.add(new PositionableDTO(n)));
		this.mine = new ArrayList<>();
		original.mine.stream().forEach(n -> this.mine.add(new PositionableDTO(n)));
		this.trolley = new ArrayList<>();
		original.trolley.stream().forEach(n -> this.trolley.add(new PositionableDTO(n)));
		this.teleporter = new ArrayList<>();
		original.teleporter.stream().forEach(n -> this.teleporter.add(new PositionableDTO(n)));
		this.wall = new ArrayList<>();
		original.wall.stream().forEach(n -> this.wall.add(new WallDTO(n)));
		this.customBackgroundTexture = new ArrayList<>();
		original.customBackgroundTexture.stream()
				.forEach(n -> this.customBackgroundTexture.add(new CustomTextureDTO(n)));
		this.customForegroundTexture = new ArrayList<>();
		original.customForegroundTexture.stream()
				.forEach(n -> this.customForegroundTexture.add(new CustomTextureDTO(n)));
		this.startPlayer = new ArrayList<>();
		original.startPlayer.stream().forEach(n -> this.startPlayer.add(new PositionableDTO(n)));
	}

	public List<TextDTO> getName() {
		return name;
	}

	public void setName(List<TextDTO> name) {
		this.name = name;
	}

	public List<TextDTO> getDescription() {
		return description;
	}

	public void setDescription(List<TextDTO> description) {
		this.description = description;
	}

	public int getIndexPreview() {
		return indexPreview;
	}

	public void setIndexPreview(int indexPreview) {
		this.indexPreview = indexPreview;
	}

	public int getBombe() {
		return bombe;
	}

	public void setBombe(int bombe) {
		this.bombe = bombe;
	}

	public int getStrenght() {
		return strenght;
	}

	public void setStrenght(int strenght) {
		this.strenght = strenght;
	}

	public float getShadow() {
		return shadow;
	}

	public void setShadow(float shadow) {
		this.shadow = shadow;
	}

	public Integer[] getBonus() {
		return bonus;
	}

	public void setBonus(Integer[] bonus) {
		this.bonus = bonus;
	}

	public boolean isFillWithBrick() {
		return fillWithBrick;
	}

	public void setFillWithBrick(boolean fillWithBrick) {
		this.fillWithBrick = fillWithBrick;
	}

	public boolean isFootInWater() {
		return footInWater;
	}

	public void setFootInWater(boolean footInWater) {
		this.footInWater = footInWater;
	}

	public boolean isLevelUnderWater() {
		return levelUnderWater;
	}

	public void setLevelUnderWater(boolean levelUnderWater) {
		this.levelUnderWater = levelUnderWater;
	}

	public boolean isStartWithKickPower() {
		return startWithKickPower;
	}

	public void setStartWithKickPower(boolean startWithKickPower) {
		this.startWithKickPower = startWithKickPower;
	}

	public DefaultTextureDTO getDefaultBackground() {
		return defaultBackground;
	}

	public void setDefaultBackground(DefaultTextureDTO defaultBackground) {
		this.defaultBackground = defaultBackground;
	}

	public DefaultTextureDTO getDefaultWall() {
		return defaultWall;
	}

	public void setDefaultWall(DefaultTextureDTO defaultWall) {
		this.defaultWall = defaultWall;
	}

	public SpriteEnum getDefaultBrickAnimation() {
		return defaultBrickAnimation;
	}

	public void setDefaultBrickAnimation(SpriteEnum defaultBrickAnimation) {
		this.defaultBrickAnimation = defaultBrickAnimation;
	}

	public List<PositionableDTO> getHole() {
		return hole;
	}

	public void setHole(List<PositionableDTO> hole) {
		this.hole = hole;
	}

	public List<PositionableDTO> getRail() {
		return rail;
	}

	public void setRail(List<PositionableDTO> rail) {
		this.rail = rail;
	}

	public List<PositionableDTO> getInterrupter() {
		return interrupter;
	}

	public void setInterrupter(List<PositionableDTO> interrupter) {
		this.interrupter = interrupter;
	}

	public List<PositionableDTO> getMine() {
		return mine;
	}

	public void setMine(List<PositionableDTO> mine) {
		this.mine = mine;
	}

	public List<PositionableDTO> getTrolley() {
		return trolley;
	}

	public void setTrolley(List<PositionableDTO> trolley) {
		this.trolley = trolley;
	}

	public List<PositionableDTO> getTeleporter() {
		return teleporter;
	}

	public void setTeleporter(List<PositionableDTO> teleporter) {
		this.teleporter = teleporter;
	}

	public List<WallDTO> getWall() {
		return wall;
	}

	public void setWall(List<WallDTO> wall) {
		this.wall = wall;
	}

	public List<CustomTextureDTO> getCustomBackgroundTexture() {
		return customBackgroundTexture;
	}

	public void setCustomBackgroundTexture(List<CustomTextureDTO> customBackgroundTexture) {
		this.customBackgroundTexture = customBackgroundTexture;
	}

	public List<CustomTextureDTO> getCustomForegroundTexture() {
		return customForegroundTexture;
	}

	public void setCustomForegroundTexture(List<CustomTextureDTO> customForegroundTexture) {
		this.customForegroundTexture = customForegroundTexture;
	}

	public List<PositionableDTO> getStartPlayer() {
		return startPlayer;
	}

	public void setStartPlayer(List<PositionableDTO> startPlayer) {
		this.startPlayer = startPlayer;
	}

}
