package com.mygdx.service.mapper;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.domain.level.Trolley;
import com.mygdx.dto.level.common.PositionableDTO;

public class TrolleyMapper {

	public Trolley toEntity(PositionableDTO dto) {
		Trolley trolley = new Trolley();
		trolley.setId(dto.getId());
		trolley.setX(dto.getX());
		trolley.setY(dto.getY());
		return trolley;
	}

	public List<Trolley> toEntitys(List<PositionableDTO> dtos) {
		List<Trolley> list = new ArrayList<>();
		if (dtos == null) {
			return list;
		}
		for (PositionableDTO dto : dtos) {
			list.add(toEntity(dto));
		}
		return list;
	}

}
