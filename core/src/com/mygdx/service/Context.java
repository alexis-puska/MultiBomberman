package com.mygdx.service;

import com.mygdx.enumeration.GameModeEnum;
import com.mygdx.enumeration.LocaleEnum;

public class Context {

	public static LocaleEnum locale = LocaleEnum.FRENCH;
	public static GameModeEnum gameMode = GameModeEnum.LOCAL;
	public static boolean useUpnp = true;

	private Context() {
	}

	public static void resetContext() {
		locale = LocaleEnum.FRENCH;
		gameMode = GameModeEnum.LOCAL;
	}
}
