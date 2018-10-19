package com.mygdx.service.mapper;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.domain.level.Mine;
import com.mygdx.dto.level.common.PositionableDTO;

public class MineMapper {

	
	public Mine toEntity(PositionableDTO dto) {
		Mine customTexture = new Mine();
		customTexture.setX(dto.getX());
		customTexture.setY(dto.getY());
		return customTexture;
	}

	public List<Mine> toEntitys(List<PositionableDTO> dtos) {
		List<Mine> list = new ArrayList<>();
		if (dtos == null) {
			return list;
		}
		for (PositionableDTO dto : dtos) {
			list.add(toEntity(dto));
		}
		return list;
	}
	
	
}
