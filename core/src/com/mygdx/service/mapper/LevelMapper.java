package com.mygdx.service.mapper;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.domain.level.Level;
import com.mygdx.dto.level.LevelDTO;

public class LevelMapper {
	
	private VarianteMapper varianteMapper;
	private TextMapper textMapper;
	
	public LevelMapper() {
		this.varianteMapper = new VarianteMapper();
		this.textMapper = new TextMapper();
	}

	public Level toEntity(LevelDTO dto) {
		Level level = new Level();
		level.setId(dto.getId());
		level.setName(textMapper.toEntitys(dto.getName()));
		level.setVariante(varianteMapper.toEntitys(dto.getVariante()));
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
