package com.mygdx.service.mapper;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.domain.level.Teleporter;
import com.mygdx.dto.level.common.PositionableDTO;

public class TeleporterMapper {

	public Teleporter toEntity(PositionableDTO dto) {
		Teleporter customTexture = new Teleporter();
		customTexture.setX(dto.getX());
		customTexture.setY(dto.getY());
		return customTexture;
	}

	public List<Teleporter> toEntitys(List<PositionableDTO> dtos) {
		List<Teleporter> list = new ArrayList<>();
		if (dtos == null) {
			return list;
		}
		for (PositionableDTO dto : dtos) {
			list.add(toEntity(dto));
		}
		return list;
	}

}
