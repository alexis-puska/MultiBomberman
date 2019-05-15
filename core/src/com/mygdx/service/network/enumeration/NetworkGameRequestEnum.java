package com.mygdx.service.network.enumeration;

import com.mygdx.service.network.utils.NetworkRequestUtils;

public enum NetworkGameRequestEnum {
	//@formatter:off
	DRAW_GRID(5), 
	DRAW_PIXEL(11), 
	DRAW_FRONT_GRID(5), 
	DRAW_FRONT_PIXEL(11), 
	DRAW_PLAYER(16), 
	DRAW_PLAYER_ON_LOUIS(19),
	BRICK_POSITION(NetworkRequestUtils.calcBufferSizeForBrick() + 1),
	BONUS_POSITION(NetworkRequestUtils.calcBufferSizeForBonus() + 1),
	TIME(5), 
	SCORE(4), 
	PAUSE_OVERLAY(1), 
	MENU_OVERLAY(1),
	SCORE_OVERLAY(1), 
	DRAW_GAME_OVERLAY(1),
	RESET_ADDITIONAL_WALL(1),
	BONUS_BURN(3),
	BONUS_REMOVE(3),
	BONUS_TAKED(3),
	BRICK_BURN(3),
	BRICK_REMOVE(3),
	ADD_WALL(3),
	FIRE_APPEARED(4),
	BONUS_REVEALED(4), 
	DRAW_BOMBE(12);
	//@formatter:on

	private int requestLength;

	private NetworkGameRequestEnum(int requestLenght) {
		this.requestLength = requestLenght;
	}

	public int getRequestLenght() {
		return this.requestLength;
	}

}
