package com.mygdx.service.network.dto;

import java.nio.ByteBuffer;

import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.service.network.enumeration.NetworkGameRequestEnum;

public class SpritePixelDTO {
	private SpriteEnum sprite;
	private int index;
	private float x;
	private float y;

	public SpritePixelDTO(SpriteEnum sprite, int index, float x, float y) {
		this.sprite = sprite;
		this.index = index;
		this.x = x;
		this.y = y;
	}

	public SpritePixelDTO(byte[] encoded) {
		ByteBuffer bb = ByteBuffer.wrap(encoded);
		this.sprite = SpriteEnum.values()[(int) bb.get()];
		this.index = (int) bb.get();
		this.x = bb.getFloat();
		this.y = bb.getFloat();
	}

	public byte[] getBuffer() {
		ByteBuffer bb;
		bb = ByteBuffer.allocate(NetworkGameRequestEnum.DRAW_PIXEL.getRequestLenght());
		bb.put((byte) NetworkGameRequestEnum.DRAW_PIXEL.ordinal());
		bb.put((byte) sprite.ordinal());
		bb.put((byte) index);
		bb.putFloat(x);
		bb.putFloat(y);
		return bb.array();
	}
}
