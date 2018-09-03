package com.mygdx.enumeration;

public enum LocaleEnum {

	FRENCH("fr"), 
	ENGLISH("en");

	private String code;

	private LocaleEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
