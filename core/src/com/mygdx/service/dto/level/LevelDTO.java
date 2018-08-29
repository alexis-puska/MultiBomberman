package com.mygdx.service.dto.level;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LevelDTO implements Serializable {

	private static final long serialVersionUID = -2962754568139006753L;

	private long id;
	private String name;
	private List<VarianteDTO> variante;

}
