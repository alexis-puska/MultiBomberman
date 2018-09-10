package com.mygdx.service;

import com.mygdx.constante.Constante;
import com.mygdx.enumeration.GameModeEnum;
import com.mygdx.enumeration.LocaleEnum;

public class Context {

	public static String guid;
	public static LocaleEnum locale;
	public static GameModeEnum gameMode;
	public static boolean useUpnp = true;
	public static int port;
	public static int localPlayer;
	public static int externalPlayer;

	private Context() {

	}

	public static void resetContext() {
		locale = LocaleEnum.FRENCH;
		gameMode = GameModeEnum.LOCAL;
		useUpnp = true;
		port = Constante.NETWORK_PORT;
		localPlayer = 1;
		externalPlayer = 0;
	}
}
