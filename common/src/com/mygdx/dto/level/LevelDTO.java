package com.mygdx.dto.level;

import java.io.Serializable;
import java.util.List;

import com.mygdx.dto.level.common.TextDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LevelDTO implements Serializable {

	private static final long serialVersionUID = -2962754568139006753L;

	private long id;
	private List<TextDTO> name;
	private List<VarianteDTO> variante;

}
