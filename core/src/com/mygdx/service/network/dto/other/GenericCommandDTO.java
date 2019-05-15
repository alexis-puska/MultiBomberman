package com.mygdx.service.network.dto.other;

import java.nio.ByteBuffer;

import com.mygdx.service.network.enumeration.NetworkGameRequestEnum;

public class GenericCommandDTO {

	private GenericCommandDTO() {
		// unused
	}

	public static byte[] getResetAdditionalWall() {
		ByteBuffer bb;
		bb = ByteBuffer.allocate(NetworkGameRequestEnum.RESET_ADDITIONAL_WALL.getRequestLenght());
		bb.put((byte) NetworkGameRequestEnum.RESET_ADDITIONAL_WALL.ordinal());
		return bb.array();
	}

	public static byte[] getMenuOverlay() {
		ByteBuffer bb;
		bb = ByteBuffer.allocate(NetworkGameRequestEnum.MENU_OVERLAY.getRequestLenght());
		bb.put((byte) NetworkGameRequestEnum.MENU_OVERLAY.ordinal());
		return bb.array();
	}

	public static byte[] getPauseOverlay() {
		ByteBuffer bb;
		bb = ByteBuffer.allocate(NetworkGameRequestEnum.PAUSE_OVERLAY.getRequestLenght());
		bb.put((byte) NetworkGameRequestEnum.PAUSE_OVERLAY.ordinal());
		return bb.array();
	}

	public static byte[] getDrawOverlay() {
		ByteBuffer bb;
		bb = ByteBuffer.allocate(NetworkGameRequestEnum.DRAW_GAME_OVERLAY.getRequestLenght());
		bb.put((byte) NetworkGameRequestEnum.DRAW_GAME_OVERLAY.ordinal());
		return bb.array();
	}

	public static byte[] getScoreOverlay() {
		ByteBuffer bb;
		bb = ByteBuffer.allocate(NetworkGameRequestEnum.SCORE_OVERLAY.getRequestLenght());
		bb.put((byte) NetworkGameRequestEnum.SCORE_OVERLAY.ordinal());
		return bb.array();
	}

	public static byte[] getTurnLightOn() {
		ByteBuffer bb;
		bb = ByteBuffer.allocate(NetworkGameRequestEnum.TURN_LIGHT_ON.getRequestLenght());
		bb.put((byte) NetworkGameRequestEnum.TURN_LIGHT_ON.ordinal());
		return bb.array();
	}

}
