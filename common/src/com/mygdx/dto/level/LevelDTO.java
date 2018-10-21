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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LevelDTO implements Serializable {

	private static final long serialVersionUID = -2962754568139006753L;

	private List<TextDTO> name;
	private List<TextDTO> description;

	private int bombe;
	private int strenght;
	private float shadow;
	private Integer[] bonus;
	private boolean fillWithBrick;
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
		this.bombe = original.bombe;
		this.strenght = original.strenght;
		this.shadow = original.shadow;
		this.bonus = new Integer[Constante.MAX_BONUS];
		for (int i = 0; i < Constante.MAX_BONUS; i++) {
			this.bonus[i] = original.bonus[i];
		}
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

}
