package com.mygdx.game.editor.domain.level;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LevelName extends Identifiable {

	private String lang;
	private String value;

	public LevelName(int id, String lang, String value) {
		super(id);
		this.lang = lang;
		this.value = value;
	}

}
