package com.mygdx.dto.level;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mygdx.dto.level.common.TextDTO;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LevelGroupDTO implements Serializable {

	private static final long serialVersionUID = -2962754568139006753L;

	private List<TextDTO> name;
	private List<LevelDTO> level;

	public LevelGroupDTO(LevelGroupDTO orignial) {
		this.name = new ArrayList<>();
		orignial.getName().stream().forEach(n -> this.name.add(new TextDTO(n)));
		this.level = new ArrayList<>();
		orignial.getLevel().stream().forEach(n -> this.level.add(new LevelDTO(n)));
	}

	public List<TextDTO> getName() {
		return name;
	}

	public void setName(List<TextDTO> name) {
		this.name = name;
	}

	public List<LevelDTO> getLevel() {
		return level;
	}

	public void setLevel(List<LevelDTO> level) {
		this.level = level;
	}
}
