package com.mygdx.service;

import java.util.HashMap;
import java.util.Map;

import com.mygdx.constante.Constante;
import com.mygdx.domain.PlayerDefinition;
import com.mygdx.enumeration.GameModeEnum;
import com.mygdx.enumeration.LocaleEnum;
import com.mygdx.enumeration.PlayerTypeEnum;

public class Context {

	public static LocaleEnum locale;
	public static GameModeEnum gameMode;
	public static boolean useUpnp = true;
	public static int port;
	public static int localPlayer;
	public static int externalPlayer;
	public static Map<Integer, PlayerDefinition> playerType;

	private Context() {

	}

	public static void resetContext() {
		locale = LocaleEnum.FRENCH;
		gameMode = GameModeEnum.LOCAL;
		useUpnp = true;
		port = Constante.NETWORK_PORT;
		localPlayer = 1;
		externalPlayer = 0;
		playerType = new HashMap<>();
		playerType.put(0, new PlayerDefinition(0, PlayerTypeEnum.HUMAN));
		for (int i = 1; i < 16; i++) {
			playerType.put(i, new PlayerDefinition(i, PlayerTypeEnum.CPU));
		}
	}
}
