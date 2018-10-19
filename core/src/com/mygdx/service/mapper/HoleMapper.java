package com.mygdx.service.mapper;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.domain.level.Hole;
import com.mygdx.dto.level.common.PositionableDTO;

public class HoleMapper {

	public Hole toEntity(PositionableDTO dto) {
		Hole hole = new Hole();
		hole.setX(dto.getX());
		hole.setY(dto.getY());
		return hole;
	}

	public List<Hole> toEntitys(List<PositionableDTO> dtos) {
		List<Hole> list = new ArrayList<>();
		if (dtos == null) {
			return list;
		}
		for (PositionableDTO dto : dtos) {
			list.add(toEntity(dto));
		}
		return list;
	}

}
