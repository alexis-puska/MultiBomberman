package com.mygdx.service.mapper;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.domain.level.Variante;
import com.mygdx.dto.level.VarianteDTO;

public class VarianteMapper {

	private CustomTextureMapper customTextureMapper;
	private DefaultTextureMapper defaultTextureMapper;
	private HoleMapper holeMapper;
	private InterrupterMapper interrupterMapper;
	private MineMapper mineMapper;
	private RailMapper railMapper;
	private StartPlayerMapper startPlayerMapper;
	private TeleporterMapper teleporterMapper;
	private TextMapper textMapper;
	private TrolleyMapper trolleyMapper;
	private WallMapper wallMapper;

	public VarianteMapper() {
		this.customTextureMapper = new CustomTextureMapper();
		this.defaultTextureMapper = new DefaultTextureMapper();
		this.holeMapper = new HoleMapper();
		this.interrupterMapper = new InterrupterMapper();
		this.mineMapper = new MineMapper();
		this.railMapper = new RailMapper();
		this.startPlayerMapper = new StartPlayerMapper();
		this.teleporterMapper = new TeleporterMapper();
		this.textMapper = new TextMapper();
		this.trolleyMapper = new TrolleyMapper();
		this.wallMapper = new WallMapper();
	}

	public Variante toEntity(VarianteDTO dto) {
		Variante variante = new Variante();
		variante.setBombe(dto.getBombe());
		variante.setBonus(dto.getBonus());
		variante.setCustomBackgroundTexture(customTextureMapper.toEntitys(dto.getCustomBackgroundTexture()));
		variante.setCustomForegroundTexture(customTextureMapper.toEntitys(dto.getCustomForegroundTexture()));
		variante.setDefaultBackground(defaultTextureMapper.toEntity(dto.getDefaultBackground()));
		variante.setDefaultBrickAnimation(dto.getDefaultBrickAnimation());
		variante.setDefaultWall(defaultTextureMapper.toEntity(dto.getDefaultWall()));
		variante.setDescription(textMapper.toEntitys(dto.getDescription()));
		variante.setFillWithBrick(dto.isFillWithBrick());
		variante.setHole(holeMapper.toEntitys(dto.getHole()));
//		variante.setId(dto.getId());
		variante.setInterrupter(interrupterMapper.toEntitys(dto.getInterrupter()));
		variante.setMine(mineMapper.toEntitys(dto.getMine()));
		variante.setName(textMapper.toEntitys(dto.getName()));
		variante.setRail(railMapper.toEntitys(dto.getRail()));
		variante.setShadow(dto.getShadow());
		variante.setStartPlayer(startPlayerMapper.toEntitys(dto.getStartPlayer()));
		variante.setStrenght(dto.getStrenght());
		variante.setTeleporter(teleporterMapper.toEntitys(dto.getTeleporter()));
		variante.setTrolley(trolleyMapper.toEntitys(dto.getTrolley()));
		variante.setWall(wallMapper.toEntitys(dto.getWall()));
		return variante;
	}

	public List<Variante> toEntitys(List<VarianteDTO> dtos) {
		List<Variante> list = new ArrayList<>();
		if (dtos == null) {
			return list;
		}
		for (VarianteDTO dto : dtos) {
			list.add(toEntity(dto));
		}
		return list;
	}

}
