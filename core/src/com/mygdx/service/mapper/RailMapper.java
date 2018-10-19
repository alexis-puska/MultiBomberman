package com.mygdx.service.mapper;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.domain.level.Rail;
import com.mygdx.dto.level.common.PositionableDTO;

public class RailMapper {

	
	public Rail toEntity(PositionableDTO dto) {
		Rail customTexture = new Rail();
		customTexture.setX(dto.getX());
		customTexture.setY(dto.getY());
		return customTexture;
	}

	public List<Rail> toEntitys(List<PositionableDTO> dtos) {
		List<Rail> list = new ArrayList<>();
		if (dtos == null) {
			return list;
		}
		for (PositionableDTO dto : dtos) {
			list.add(toEntity(dto));
		}
		return list;
	}
	
}
