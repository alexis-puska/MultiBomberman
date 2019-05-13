package com.mygdx.service.network.enumeration;

import com.mygdx.service.network.utils.NetworkRequestUtils;

public enum NetworkGameRequestEnum {
	DRAW_GRID(5), 
	DRAW_PIXEL(11), 
	DRAW_FRONT_GRID(5), 
	DRAW_FRONT_PIXEL(11), 
	DRAW_PLAYER(17), 
	DRAW_PLAYER_ON_LOUIS(20),
	BRICK_POSITION(NetworkRequestUtils.calcGridSizeArray() + 1), 
	TIME(5), 
	LIGHT(6), 
	SCORE(4), 
	PAUSE(1), 
	MENU(1);

	private int requestLength;

	private NetworkGameRequestEnum(int requestLenght) {
		this.requestLength = requestLenght;
	}

	public int getRequestLenght() {
		return this.requestLength;
	}

}
