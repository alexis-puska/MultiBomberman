package com.mygdx.service.mapper;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.domain.level.DefaultTexture;
import com.mygdx.dto.level.common.DefaultTextureDTO;

public class DefaultTextureMapper {

	public DefaultTexture toEntity(DefaultTextureDTO dto) {
		DefaultTexture defaultTexture = new DefaultTexture();
		defaultTexture.setAnimation(dto.getAnimation());
		defaultTexture.setIndex(dto.getIndex());
		return defaultTexture;
	}

	public List<DefaultTexture> toEntitys(List<DefaultTextureDTO> dtos) {
		List<DefaultTexture> list = new ArrayList<>();
		if (dtos == null) {
			return list;
		}
		for (DefaultTextureDTO dto : dtos) {
			list.add(toEntity(dto));
		}
		return list;
	}

}
