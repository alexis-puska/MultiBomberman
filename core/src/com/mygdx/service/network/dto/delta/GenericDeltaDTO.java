package com.mygdx.service.network.dto.delta;

import java.nio.ByteBuffer;

import com.mygdx.service.network.enumeration.NetworkGameRequestEnum;

public class GenericDeltaDTO {

	private GenericDeltaDTO() {
		// unused
	}

	public static byte[] createRemoveBrickRequest(int gridIndex) {
		ByteBuffer bb;
		bb = ByteBuffer.allocate(NetworkGameRequestEnum.BRICK_REMOVE.getRequestLenght());
		bb.put((byte) NetworkGameRequestEnum.BRICK_REMOVE.ordinal());
		bb.putShort((short) gridIndex);
		return bb.array();
	}

	public static byte[] createBurnBrickRequest(int gridIndex) {
		ByteBuffer bb;
		bb = ByteBuffer.allocate(NetworkGameRequestEnum.BRICK_BURN.getRequestLenght());
		bb.put((byte) NetworkGameRequestEnum.BRICK_BURN.ordinal());
		bb.putShort((short) gridIndex);
		return bb.array();
	}

	public static byte[] createRemoveBonusRequest(int gridIndex) {
		ByteBuffer bb;
		bb = ByteBuffer.allocate(NetworkGameRequestEnum.BONUS_REMOVE.getRequestLenght());
		bb.put((byte) NetworkGameRequestEnum.BONUS_REMOVE.ordinal());
		bb.putShort((short) gridIndex);
		return bb.array();
	}

	public static byte[] createBurnBonusRequest(int gridIndex) {
		ByteBuffer bb;
		bb = ByteBuffer.allocate(NetworkGameRequestEnum.BONUS_BURN.getRequestLenght());
		bb.put((byte) NetworkGameRequestEnum.BONUS_BURN.ordinal());
		bb.putShort((short) gridIndex);
		return bb.array();
	}

	public static byte[] createBonusTakedRequest(int gridIndex) {
		ByteBuffer bb;
		bb = ByteBuffer.allocate(NetworkGameRequestEnum.BONUS_TAKED.getRequestLenght());
		bb.put((byte) NetworkGameRequestEnum.BONUS_TAKED.ordinal());
		bb.putShort((short) gridIndex);
		return bb.array();
	}

	public static byte[] createAddWallRequest(int gridIndex) {
		ByteBuffer bb;
		bb = ByteBuffer.allocate(NetworkGameRequestEnum.ADD_WALL.getRequestLenght());
		bb.put((byte) NetworkGameRequestEnum.ADD_WALL.ordinal());
		bb.putShort((short) gridIndex);
		return bb.array();
	}

	public static int getIndex(byte[] line) {
		ByteBuffer bb = ByteBuffer.wrap(line);
		return (int) bb.getShort();
	}

}
