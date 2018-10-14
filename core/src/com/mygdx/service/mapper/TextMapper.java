package com.mygdx.service.mapper;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.domain.level.Text;
import com.mygdx.dto.level.common.TextDTO;

public class TextMapper {
	public Text toEntity(TextDTO dto) {
		Text text = new Text();
		text.setLang(dto.getLang());
		text.setValue(dto.getValue());
		return text;
	}

	public List<Text> toEntitys(List<TextDTO> dtos) {
		List<Text> list = new ArrayList<>();
		if (dtos == null) {
			return list;
		}
		for (TextDTO dto : dtos) {
			list.add(toEntity(dto));
		}
		return list;
	}
}
