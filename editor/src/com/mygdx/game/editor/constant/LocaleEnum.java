package com.mygdx.game.editor.constant;

public enum LocaleEnum {

	FRENCH("fr"), 
	ENGLISH("en"), 
	SPANISH("es");

	private String code;

	private LocaleEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
