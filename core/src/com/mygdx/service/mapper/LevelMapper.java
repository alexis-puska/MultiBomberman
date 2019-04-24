package com.mygdx.service.mapper;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.domain.level.Level;
import com.mygdx.dto.level.LevelDTO;

public class LevelMapper {

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

	public LevelMapper() {
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

	public Level toEntity(LevelDTO dto) {
		Level level = new Level();
		level.setBombe(dto.getBombe());
		level.setBonus(dto.getBonus());
		level.setCustomBackgroundTexture(customTextureMapper.toEntitys(dto.getCustomBackgroundTexture()));
		level.setCustomForegroundTexture(customTextureMapper.toEntitys(dto.getCustomForegroundTexture()));
		level.setDefaultBackground(defaultTextureMapper.toEntity(dto.getDefaultBackground()));
		level.setDefaultBrickAnimation(dto.getDefaultBrickAnimation());
		level.setDefaultWall(defaultTextureMapper.toEntity(dto.getDefaultWall()));
		level.setDescription(textMapper.toEntitys(dto.getDescription()));
		level.setFillWithBrick(dto.isFillWithBrick());
		level.setFootInWater(dto.isFootInWater());
		level.setLevelUnderWater(dto.isLevelUnderWater());
		level.setHole(holeMapper.toEntitys(dto.getHole()));
		level.setInterrupter(interrupterMapper.toEntitys(dto.getInterrupter()));
		level.setMine(mineMapper.toEntitys(dto.getMine()));
		level.setName(textMapper.toEntitys(dto.getName()));
		level.setRail(railMapper.toEntitys(dto.getRail()));
		level.setShadow(dto.getShadow());
		level.setStartPlayer(startPlayerMapper.toEntitys(dto.getStartPlayer()));
		level.setStrenght(dto.getStrenght());
		level.setTeleporter(teleporterMapper.toEntitys(dto.getTeleporter()));
		level.setTrolley(trolleyMapper.toEntitys(dto.getTrolley()));
		level.setWall(wallMapper.toEntitys(dto.getWall()));
		return level;
	}

	public List<Level> toEntitys(List<LevelDTO> dtos) {
		List<Level> list = new ArrayList<>();
		if (dtos == null) {
			return list;
		}
		for (LevelDTO dto : dtos) {
			list.add(toEntity(dto));
		}
		return list;
	}

}
