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

	public static byte[] getMenu() {
		ByteBuffer bb;
		bb = ByteBuffer.allocate(NetworkGameRequestEnum.MENU.getRequestLenght());
		bb.put((byte) NetworkGameRequestEnum.MENU.ordinal());
		return bb.array();
	}

	public static byte[] getPause() {
		ByteBuffer bb;
		bb = ByteBuffer.allocate(NetworkGameRequestEnum.PAUSE.getRequestLenght());
		bb.put((byte) NetworkGameRequestEnum.PAUSE.ordinal());
		return bb.array();
	}

}
