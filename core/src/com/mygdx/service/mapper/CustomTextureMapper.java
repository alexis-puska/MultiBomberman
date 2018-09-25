package com.mygdx.service.mapper;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.domain.level.CustomTexture;
import com.mygdx.dto.level.CustomTextureDTO;

public class CustomTextureMapper {

	public CustomTexture toEntity(CustomTextureDTO dto) {
		CustomTexture customTexture = new CustomTexture();
		customTexture.setId(dto.getId());
		customTexture.setX(dto.getX());
		customTexture.setY(dto.getY());
		customTexture.setAnimation(dto.getAnimation());
		customTexture.setIndex(dto.getIndex());
		return customTexture;
	}

	public List<CustomTexture> toEntitys(List<CustomTextureDTO> dtos) {
		List<CustomTexture> list = new ArrayList<>();
		if (dtos == null) {
			return list;
		}
		for (CustomTextureDTO dto : dtos) {
			list.add(toEntity(dto));
		}
		return list;
	}

}
