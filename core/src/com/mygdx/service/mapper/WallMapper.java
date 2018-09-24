package com.mygdx.service.mapper;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.domain.level.Wall;
import com.mygdx.dto.level.WallDTO;

public class WallMapper {

	public Wall toEntity(WallDTO dto) {
		Wall wall = new Wall();
		wall.setId(dto.getId());
		wall.setX(dto.getX());
		wall.setY(dto.getY());
		return wall;
	}

	public List<Wall> toEntitys(List<WallDTO> dtos) {
		List<Wall> list = new ArrayList<>();
		if (dtos == null) {
			return list;
		}
		for (WallDTO dto : dtos) {
			list.add(toEntity(dto));
		}
		return list;
	}
}
