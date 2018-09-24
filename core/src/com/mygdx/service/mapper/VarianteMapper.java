package com.mygdx.service.mapper;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.domain.level.Variante;
import com.mygdx.dto.level.VarianteDTO;

public class VarianteMapper {

	public Variante toEntity(VarianteDTO dto) {
		Variante variante = new Variante();

		return variante;
	}

	public List<Variante> toEntitys(List<VarianteDTO> dtos) {
		List<Variante> list = new ArrayList<>();
		if (dtos == null) {
			return list;
		}
		for (VarianteDTO dto : dtos) {
			list.add(toEntity(dto));
		}
		return list;
	}

}
