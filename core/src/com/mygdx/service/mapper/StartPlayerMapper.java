package com.mygdx.service.mapper;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.domain.level.StartPlayer;
import com.mygdx.dto.level.common.PositionableDTO;

public class StartPlayerMapper {

	public StartPlayer toEntity(PositionableDTO dto) {
		StartPlayer startPlayer = new StartPlayer();
		startPlayer.setX(dto.getX());
		startPlayer.setY(dto.getY());
		return startPlayer;
	}

	public List<StartPlayer> toEntitys(List<PositionableDTO> dtos) {
		List<StartPlayer> list = new ArrayList<>();
		if (dtos == null) {
			return list;
		}
		for (PositionableDTO dto : dtos) {
			list.add(toEntity(dto));
		}
		return list;
	}

}
