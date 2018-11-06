package com.mygdx.dto.level;

import java.io.Serializable;
import java.util.List;

public class LevelFileDTO implements Serializable {

	private static final long serialVersionUID = -2962754568139006753L;

	private List<LevelGroupDTO> levelGroups;

	public List<LevelGroupDTO> getLevelGroups() {
		return levelGroups;
	}

	public void setLevelGroups(List<LevelGroupDTO> levelGroups) {
		this.levelGroups = levelGroups;
	}

}
