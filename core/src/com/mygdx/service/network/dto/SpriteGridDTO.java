package com.mygdx.service.network.dto;

import java.nio.ByteBuffer;

import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.service.network.enumeration.NetworkGameRequestEnum;

public class SpriteGridDTO {

	private boolean front;
	private SpriteEnum sprite;
	private int index;
	private int gridIndex;

	public SpriteGridDTO(boolean front, SpriteEnum sprite, int index, int gridIndex) {
		this.front = front;
		this.sprite = sprite;
		this.index = index;
		this.gridIndex = gridIndex;
	}

	public SpriteGridDTO(byte[] encoded) {
		ByteBuffer bb = ByteBuffer.wrap(encoded);
		this.sprite = SpriteEnum.values()[(int) bb.get()];
		this.index = (int) bb.get();
		this.gridIndex = (int) bb.getShort();
	}

	public byte[] getBuffer() {
		ByteBuffer bb;
		if (front) {
			bb = ByteBuffer.allocate(NetworkGameRequestEnum.DRAW_FRONT_GRID.getRequestLenght());
			bb.put((byte) NetworkGameRequestEnum.DRAW_FRONT_GRID.ordinal());
		} else {
			bb = ByteBuffer.allocate(NetworkGameRequestEnum.DRAW_GRID.getRequestLenght());
			bb.put((byte) NetworkGameRequestEnum.DRAW_GRID.ordinal());
		}
		bb.put((byte) sprite.ordinal());
		bb.put((byte) index);
		bb.putShort((short) gridIndex);
		return bb.array();
	}

}
