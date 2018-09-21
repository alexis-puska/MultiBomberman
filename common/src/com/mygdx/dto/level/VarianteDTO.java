package com.mygdx.dto.level;

import java.io.Serializable;
import java.util.List;

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

	private long id;
	private String name;
	private String description;
	private int bombe;
	private int strenght;
	private Integer[] bonus;
	private boolean fillWithBrick;
	private String backgroundTextureName;
	private int backgroundTextureIndex;
	private List<HoleDTO> hole;
	private List<RailDTO> rail;
	private List<SwitchDTO> interrupter;
	private List<MineDTO> mine;
	private List<TrolleyDTO> trolley;
	private List<TeleporterDTO> teleporter;

	private List<CustomTextureDTO> texture;
	private List<WallDTO> wall;
	
}
