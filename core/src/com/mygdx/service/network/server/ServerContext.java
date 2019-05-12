package com.mygdx.service.network.server;

import com.mygdx.enumeration.ServerStateEnum;
import com.mygdx.service.network.enumeration.NetworkGameRequestEnum;

public class ServerContext {

	private static ServerStateEnum currentServerScreen;
	private static String waitScreenRequestBuffer;
	private static String skinScreenRequestBuffer;
	private static String ruleScreenRequestBuffer;
	private static String levelScreenRequestBuffer;
	private static String levelDefinitionBuffer;
	private static String gameScreenBuffer;

	private ServerContext() {
		// empty constructor
	}

	public static void resetContext() {
		currentServerScreen = ServerStateEnum.WAIT_SCREEN;
	}

	public static void setCurrentServerScreen(ServerStateEnum state) {
		currentServerScreen = state;
	}

	public static ServerStateEnum getCurrentServerScreen() {
		return currentServerScreen;
	}

	public static String getWaitScreenRequestBuffer() {
		return waitScreenRequestBuffer;
	}

	public static void setWaitScreenRequestBuffer(String waitScreenRequestBuffer) {
		ServerContext.waitScreenRequestBuffer = waitScreenRequestBuffer;
	}

	public static String getSkinScreenRequestBuffer() {
		return skinScreenRequestBuffer;
	}

	public static void setSkinScreenRequestBuffer(String skinScreenRequestBuffer) {
		ServerContext.skinScreenRequestBuffer = skinScreenRequestBuffer;
	}

	public static String getRuleScreenRequestBuffer() {
		return ruleScreenRequestBuffer;
	}

	public static void setRuleScreenRequestBuffer(String ruleScreenRequestBuffer) {
		ServerContext.ruleScreenRequestBuffer = ruleScreenRequestBuffer;
	}

	public static String getLevelScreenRequestBuffer() {
		return levelScreenRequestBuffer;
	}

	public static void setLevelScreenRequestBuffer(String levelScreenRequestBuffer) {
		ServerContext.levelScreenRequestBuffer = levelScreenRequestBuffer;
	}

	public static String getLevelDefinitionBuffer() {
		return levelDefinitionBuffer;
	}

	public static void setLevelDefinitionBuffer(String levelDefinitionBuffer) {
		ServerContext.levelDefinitionBuffer = levelDefinitionBuffer;
	}

	public static void addTime(float time) {
		NetworkGameRequestEnum.TIME.ordinal();
	}
}
