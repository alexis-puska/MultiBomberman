package com.mygdx.dto.level;

import java.io.Serializable;
import java.util.List;

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
public class VarianteDTO implements Serializable {

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

}
