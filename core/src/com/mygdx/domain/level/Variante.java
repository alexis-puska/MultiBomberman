package com.mygdx.domain.level;

import java.util.List;

import com.mygdx.enumeration.SpriteEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Variante {
	private long id;
	private List<Text> name;
	private List<Text> description;
	private int bombe;
	private int strenght;
	private float shadow;
	private Integer[] bonus;
	private boolean fillWithBrick;
	private DefaultTexture defaultBackground;
	private DefaultTexture defaultWall;
	private SpriteEnum defaultBrickAnimation;
	private List<Hole> hole;
	private List<Rail> rail;
	private List<Interrupter> interrupter;
	private List<Mine> mine;
	private List<Trolley> trolley;
	private List<Teleporter> teleporter;
	private List<Wall> wall;
	private List<CustomTexture> customBackgroundTexture;
	private List<CustomTexture> customForegroundTexture;
	private List<StartPlayer> startPlayer;
}
