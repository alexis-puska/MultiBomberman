package com.mygdx.service;

import com.mygdx.constante.Constante;
import com.mygdx.enumeration.GameModeEnum;
import com.mygdx.enumeration.LocaleEnum;

public class Context {

	private static String guid;
	private static LocaleEnum locale;
	private static GameModeEnum gameMode;
	private static boolean useUpnp = true;
	private static int port;
	private static int localPlayer;
	private static int externalPlayer;

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

	public static String getGuid() {
		return guid;
	}

	public static void setGuid(String guid) {
		Context.guid = guid;
	}

	public static LocaleEnum getLocale() {
		return locale;
	}

//	public static void setLocale(LocaleEnum locale) {
//		Context.locale = locale;
//	}

	public static GameModeEnum getGameMode() {
		return gameMode;
	}

	public static void setGameMode(GameModeEnum gameMode) {
		Context.gameMode = gameMode;
	}

	public static boolean isUseUpnp() {
		return useUpnp;
	}

	public static void setUseUpnp(boolean useUpnp) {
		Context.useUpnp = useUpnp;
	}

	public static int getPort() {
		return port;
	}

	public static void setPort(int port) {
		Context.port = port;
	}

	public static int getLocalPlayer() {
		return localPlayer;
	}

	public static void setLocalPlayer(int localPlayer) {
		Context.localPlayer = localPlayer;
	}

	public static int getExternalPlayer() {
		return externalPlayer;
	}

	public static void setExternalPlayer(int externalPlayer) {
		Context.externalPlayer = externalPlayer;
	}

	public static void decLocale() {
		locale = LocaleEnum.previous(locale);
	}

	public static void incLocale() {
		locale = LocaleEnum.next(locale);
	}

	public static void decGameMode() {
		gameMode = GameModeEnum.previous(gameMode);
	}

	public static void incGameMode() {
		gameMode = GameModeEnum.next(gameMode);
	}
}
