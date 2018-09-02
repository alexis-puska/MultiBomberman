package com.mygdx.constante;

import com.badlogic.gdx.Application;

public class Constante {

	public static final boolean DEBUG = true;
	public static final int LIBGDX_LOG_LEVEL = Application.LOG_INFO;

	
	public static final int NETWORK_PORT = 7777;
	public static final int NETWORK_WAIT_TIME = 20;
	public static final String NETWORK_IP_SERVICE = "http://checkip.amazonaws.com";
	public static final boolean NETWORK_IP_SPLIT = true;
	public static final int NETWORK_EXTERNAL_IP_POSITION = 0;
	public static final int NETWORK_INTERNET_IP_POSITION = 0;
	
	public static final int SCREEN_SIZE_X = 640;
	public static final int SCREEN_SIZE_Y = 360;
	public static final int EDITOR_SIZE_X = 1200;
	public static final int EDITOR_SIZE_Y = 800;

	private Constante() {
		// empty private constructor
	}
}
