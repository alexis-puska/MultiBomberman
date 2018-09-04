package com.mygdx.service;

import com.mygdx.enumeration.GameModeEnum;
import com.mygdx.enumeration.LocaleEnum;

public class Context {

	private static LocaleEnum locale = LocaleEnum.FRENCH;
	private static GameModeEnum gameMode = GameModeEnum.LOCAL;

	private Context() {
	}

	public static LocaleEnum getLocale() {
		return locale;
	}

	public static void setLocale(LocaleEnum locale) {
		Context.locale = locale;
	}

	public static GameModeEnum getGameMode() {
		return gameMode;
	}

	public static void setGameMode(GameModeEnum gameMode) {
		Context.gameMode = gameMode;
	}

	public static void resetContext() {
		locale = LocaleEnum.FRENCH;
		gameMode = GameModeEnum.LOCAL;
	}
}
