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
	
	public static LocaleEnum next(LocaleEnum val) {
		 return values()[(val.ordinal() + 1) % values().length];
	}

	public static LocaleEnum previous(LocaleEnum val) {
		if ((val.ordinal() - 1) < 0) {
			return values()[values().length - 1];
		}
		return values()[(val.ordinal() - 1)];
	}
}
