package com.mygdx.enumeration;

public enum GameModeEnum {
	LOCAL,
	SERVER,
	CLIENT;
	
	
	public static GameModeEnum next(GameModeEnum val) {
		 return values()[(val.ordinal() + 1) % values().length];
	}

	public static GameModeEnum previous(GameModeEnum val) {
		if ((val.ordinal() - 1) < 0) {
			return values()[values().length - 1];
		}
		return values()[(val.ordinal() - 1)];
	}
}
