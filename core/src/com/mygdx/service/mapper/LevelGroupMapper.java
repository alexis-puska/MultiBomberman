package com.mygdx.service.mapper;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.domain.level.LevelGroup;
import com.mygdx.dto.level.LevelGroupDTO;

public class LevelGroupMapper {

	private LevelMapper levelMapper;
	private TextMapper textMapper;

	public LevelGroupMapper() {
		this.levelMapper = new LevelMapper();
		this.textMapper = new TextMapper();
	}

	public LevelGroup toEntity(LevelGroupDTO dto) {
		LevelGroup levelGroup = new LevelGroup();
		levelGroup.setName(textMapper.toEntitys(dto.getName()));
		levelGroup.setLevel(levelMapper.toEntitys(dto.getLevel()));
		return levelGroup;
	}

	public List<LevelGroup> toEntitys(List<LevelGroupDTO> dtos) {
		List<LevelGroup> list = new ArrayList<>();
		if (dtos == null) {
			return list;
		}
		for (LevelGroupDTO dto : dtos) {
			list.add(toEntity(dto));
		}
		return list;
	}

}
