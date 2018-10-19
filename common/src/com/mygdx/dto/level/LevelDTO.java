package com.mygdx.dto.level;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mygdx.dto.level.common.TextDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LevelDTO implements Serializable {

	private static final long serialVersionUID = -2962754568139006753L;

	private List<TextDTO> name;
	private List<VarianteDTO> variante;

	public LevelDTO(LevelDTO orignial) {
		this.name = new ArrayList<>();
		orignial.getName().stream().forEach(n -> this.name.add(new TextDTO(n)));
		this.variante = new ArrayList<>();
		orignial.getVariante().stream().forEach(n -> this.variante.add(new VarianteDTO(n)));
	}

}
