package com.mygdx.service;

import com.mygdx.constante.Constante;
import com.mygdx.dto.level.LevelDTO;
import com.mygdx.enumeration.GameModeEnum;
import com.mygdx.enumeration.LocaleEnum;
import com.mygdx.enumeration.TimeEnum;

public class Context {

	private static String uuid;

	// lang
	private static LocaleEnum locale;

	// mode
	private static GameModeEnum gameMode;

	// network
	private static boolean useUpnp = true;
	private static int port;
	private static int localPlayer;
	private static int externalPlayer;
	private static int[] ipPart;

	// Rules
	private static boolean suddenDeath;
	private static boolean badBomber;
	private static TimeEnum time;
	private static int iaLevel;

	// Game
	private static LevelDTO level;

	private Context() {
		// empty constructor
	}

	public static void resetContext() {
		locale = LocaleEnum.FRENCH;
		gameMode = GameModeEnum.LOCAL;
		useUpnp = Constante.NETWORK_USE_UPNP;
		port = Constante.NETWORK_PORT;
		localPlayer = Constante.DEFAULT_LOCAL_PLAYER;
		externalPlayer = Constante.DEFAULT_EXTERNAL_PLAYER;
		ipPart = new int[] { 127, 0, 0, 1 };
		time = Constante.DEFAULT_GAME_TIME;
		suddenDeath = Constante.SUDDEN_DEATH;
		badBomber = Constante.BAD_BOMBER;
		iaLevel = Constante.MIN_IA_LEVEL;
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
		if (localPlayer + externalPlayer < Constante.MAX_PLAYER) {
			localPlayer++;
			if (localPlayer > Constante.MAX_PLAYER) {
				localPlayer = Constante.MAX_PLAYER;
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

	public static void setExternalPlayer(int eexternalPlayer) {
		externalPlayer = eexternalPlayer;
	}

	public static void incExternalPlayer() {
		if (localPlayer + externalPlayer < Constante.MAX_PLAYER) {
			externalPlayer++;
			if (externalPlayer > Constante.MAX_PLAYER) {
				externalPlayer = Constante.MAX_PLAYER;
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

	public static String getIp() {
		return ipPart[0] + "." + ipPart[1] + "." + ipPart[2] + "." + ipPart[3];
	}

	public static int extractIp(int part, int pos) {
		int n = ipPart[part];
		int u = n % 10;
		int d = n / 10 % 10;
		int c = n / 100 % 10;
		switch (pos) {
		case 0:
			return u;
		case 1:
			return d;
		default:
		case 2:
			return c;
		}
	}

	public static void incIp(int idx, int val) {
		ipPart[idx] += val;
		if (ipPart[idx] > 255) {
			ipPart[idx] = 255;
		} else if (ipPart[idx] < 0) {
			ipPart[idx] = 0;
		}
	}

	public static void decIp(int idx, int val) {
		ipPart[idx] -= val;
		if (ipPart[idx] > 255) {
			ipPart[idx] = 255;
		} else if (ipPart[idx] < 0) {
			ipPart[idx] = 0;
		}
	}

	public static void toogleSuddenDeath() {
		if (suddenDeath) {
			suddenDeath = false;
		} else {
			suddenDeath = true;
		}
	}

	public static void toogleBadBomber() {
		if (badBomber) {
			badBomber = false;
		} else {
			badBomber = true;
		}
	}

	public static void decIaLevel() {
		iaLevel--;
		if (iaLevel < Constante.MIN_IA_LEVEL) {
			iaLevel = Constante.MAX_IA_LEVEL;
		}
	}

	public static void incIaLevel() {
		iaLevel++;
		if (iaLevel > Constante.MAX_IA_LEVEL) {
			iaLevel = Constante.MIN_IA_LEVEL;
		}
	}

	public static void decTime() {
		time = TimeEnum.previous(time);
	}

	public static void incTime() {
		time = TimeEnum.next(time);
	}

	public static boolean isSuddenDeath() {
		return suddenDeath;
	}

	public static boolean isBadBomber() {
		return badBomber;
	}

	public static TimeEnum getTime() {
		return time;
	}

	public static int getIaLevel() {
		return iaLevel;
	}

	public static void decBombe() {
		level.setBombe(level.getBombe() - 1);
		if (level.getBombe() < Constante.MIN_BOMBE) {
			level.setBombe(Constante.MAX_BOMBE);
		}
	}

	public static void incBombe() {
		level.setBombe(level.getBombe() + 1);
		if (level.getBombe() > Constante.MAX_BOMBE) {
			level.setBombe(Constante.MAX_BOMBE);
		}
	}

	public static void decStrength() {
		level.setStrenght(level.getStrenght() - 1);
		if (level.getStrenght() < Constante.MIN_STRENGTH) {
			level.setStrenght(Constante.MAX_STRENGTH);
		}
	}

	public static void incStrength() {
		level.setStrenght(level.getStrenght() + 1);
		if (level.getStrenght() > Constante.MAX_STRENGTH) {
			level.setStrenght(Constante.MIN_STRENGTH);
		}
	}

	public static int getBombe() {
		return level.getBombe();
	}

	public static int getStrength() {
		return level.getStrenght();
	}

	public static void setLevel(LevelDTO levelDTO) {
		level = levelDTO;
	}

	public static LevelDTO getLevel() {
		return level;
	}

	public static void incBonus(int i, int n) {
		int nb = 0;
		for (int j = 0; j < Constante.MAX_BONUS; j++) {
			nb += level.getBonus()[j];
		}

		if ((nb + n) < Constante.MAX_BONUS_PER_LEVEL) {
			level.getBonus()[i] += n;
		} else {
			level.getBonus()[i] += Constante.MAX_BONUS_PER_LEVEL - nb;
		}

	}

	public static void decBonus(int i, int n) {
		level.getBonus()[i] -= n;
		if (level.getBonus()[i] < 0) {
			level.getBonus()[i] = 0;
		}
	}

	public static int getBonus(int i) {
		return level.getBonus()[i];
	}
}
