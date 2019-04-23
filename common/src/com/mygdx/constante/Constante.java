package com.mygdx.constante;

import com.badlogic.gdx.Application;
import com.mygdx.enumeration.TimeEnum;

public class Constante {

	public static final boolean DEBUG = true;
	public static final int LIBGDX_LOG_LEVEL = Application.LOG_INFO;

	// NETWORK DEFAULT VALUE
	public static final String NETWORK_APPLICATION_UPNP_NAME = "Multi Bomberman";
	public static final boolean NETWORK_USE_UPNP = true;
	public static final int NETWORK_PORT = 7777;
	public static final int NETWORK_WAIT_TIME = 20;
	public static final String NETWORK_IP_SERVICE = "http://checkip.amazonaws.com";
	public static final boolean NETWORK_IP_SPLIT = true;
	public static final int NETWORK_EXTERNAL_IP_POSITION = 0;
	public static final int NETWORK_INTERNET_IP_POSITION = 0;

	public static final int NETWORK_REGISTRATION_TIMEOUT = 30000;
	public static final int NETWORK_REGISTRATION_SCHEDULE_TIME = 5000;
	public static final int NETWORK_REGISTRATION_HEARTHBEAT_TIME = 5000;

	// SCREEN DEFAULT VALUE
	public static final int SCREEN_SIZE_X = 640;
	public static final int SCREEN_SIZE_Y = 360;
	public static final int EDITOR_SIZE_X = 1200;
	public static final int EDITOR_SIZE_Y = 800;
	public static final int GAME_SCREEN_SIZE_X = 630;
	public static final int GAME_SCREEN_SIZE_Y = 336;

	public static final int GRID_SIZE_X = 35;
	public static final int GRID_SIZE_Y = 21;
	public static final float GRID_PIXELS_SIZE_X = 18f;
	public static final float GRID_PIXELS_SIZE_Y = 16f;
	public static final int BURN_BRICK_COUNTDOWN = 3;

	// GAME PARAM DEFAULT VALUE
	public static final int DEFAULT_LOCAL_PLAYER = 1;
	public static final int DEFAULT_EXTERNAL_PLAYER = 0;
	public static final int MAX_PLAYER = 16;
	public static final TimeEnum DEFAULT_GAME_TIME = TimeEnum.THREE;
	public static final int MIN_IA_LEVEL = 1;
	public static final int MAX_IA_LEVEL = 5;
	public static final boolean SUDDEN_DEATH = false;
	public static final boolean BAD_BOMBER = false;
	public static final int MAX_BONUS = 15;
	public static final int MAX_BONUS_PER_LEVEL = 150;
	public static final int DEFAULT_BOMBE = 2;
	public static final int MIN_BOMBE = 1;
	public static final int MAX_BOMBE = 6;
	public static final int DEFAULT_STRENGTH = 1;
	public static final int MIN_STRENGTH = 1;
	public static final int MAX_STRENGTH = 6;

	public static final float WALK_SPEED = 6f;
	public static final float ADD_WALK_SPEED = 1f;

	public static final float SLOW_WALK_SPEED = 1f;
	public static final float FAST_WALK_SPEED = 15f;

	public static final float DEFAULT_SHIP_SPEED = 0.8f;
	public static final float SHIP_SPEED_STEP = 0.5f;

	public static final int INVINCIBLE_TIME = 50;
	public static final int FASTE_BOMBE_TIME = 40;
	public static final int SLOW_BOMBE_TIME = 150;
	public static final int MALUS_TIME = 250;

	private Constante() {
		// empty private constructor
	}
}
