package com.mygdx.service.mapper;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.domain.level.Interrupter;
import com.mygdx.dto.level.common.PositionableDTO;

public class InterrupterMapper {

	public Interrupter toEntity(PositionableDTO dto) {
		Interrupter interrupter = new Interrupter();
		interrupter.setId(dto.getId());
		interrupter.setX(dto.getX());
		interrupter.setY(dto.getY());
		return interrupter;
	}

	public List<Interrupter> toEntitys(List<PositionableDTO> dtos) {
		List<Interrupter> list = new ArrayList<>();
		if (dtos == null) {
			return list;
		}
		for (PositionableDTO dto : dtos) {
			list.add(toEntity(dto));
		}
		return list;
	}

}
