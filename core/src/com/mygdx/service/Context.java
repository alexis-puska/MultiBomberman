package com.mygdx.service;

import com.mygdx.constante.Constante;
import com.mygdx.enumeration.GameModeEnum;
import com.mygdx.enumeration.LocaleEnum;

public class Context {

	private static String uuid;
	private static LocaleEnum locale;
	private static GameModeEnum gameMode;
	private static boolean useUpnp = true;
	private static int port;
	private static int localPlayer;
	private static int externalPlayer;

	private Context() {
		//empty constructor
	}

	public static void resetContext() {
		locale = LocaleEnum.FRENCH;
		gameMode = GameModeEnum.LOCAL;
		useUpnp = true;
		port = Constante.NETWORK_PORT;
		localPlayer = 1;
		externalPlayer = 0;
	}

	public static String getUuid() {
		return uuid;
	}

	public static void setUuid(String uuid) {
		Context.uuid = uuid;
	}

	public static LocaleEnum getLocale() {
		return locale;
	}

	public static GameModeEnum getGameMode() {
		return gameMode;
	}

	public static boolean isUseUpnp() {
		return useUpnp;
	}

	public static void toogleUpnp() {
		if (useUpnp) {
			useUpnp = false;
		} else {
			useUpnp = true;
		}
	}

	public static int getPort() {
		return port;
	}

	public static void setPort(int networkPort) {
		port = networkPort;
	}

	public static void incPort() {
		port++;
		if (port > 65535) {
			port = 0;
		}
	}

	public static void decPort() {
		port--;
		if (port < 1024) {
			port = 65535;
		}
	}

	public static int getLocalPlayer() {
		return localPlayer;
	}

	public static void incLocalPlayer() {
		if (localPlayer + externalPlayer < 16) {
			localPlayer++;
			if (localPlayer > 16) {
				localPlayer = 16;
			}
		}
	}

	public static void decLocalPlayer() {
		localPlayer--;
		if (localPlayer < 0) {
			localPlayer = 0;
		}
	}

	public static int getExternalPlayer() {
		return externalPlayer;
	}

	public static void incExternalPlayer() {
		if (localPlayer + externalPlayer < 16) {
			externalPlayer++;
			if (externalPlayer > 16) {
				externalPlayer = 16;
			}
		}
	}

	public static void decExternalPlayer() {
		externalPlayer--;
		if (externalPlayer < 0) {
			externalPlayer = 0;
		}
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
