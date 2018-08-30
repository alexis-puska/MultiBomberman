package com.mygdx.dto.level;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LevelFileDTO implements Serializable {

	private static final long serialVersionUID = -2962754568139006753L;

	private List<LevelDTO> levels;

}
