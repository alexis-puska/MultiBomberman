package com.mygdx.service.network.dto;

import java.nio.ByteBuffer;

import com.mygdx.service.network.enumeration.NetworkGameRequestEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LightDTO {
	private int x;
	private int y;
	private int radius;

	public LightDTO(int x, int y, int radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
	}

	public LightDTO(byte[] encoded) {
		ByteBuffer bb = ByteBuffer.wrap(encoded);
		this.x = (int) bb.getShort();
		this.y = (int) bb.getShort();
		this.radius = (int) bb.get();
	}

	public byte[] getBuffer() {
		ByteBuffer bb;
		bb = ByteBuffer.allocate(NetworkGameRequestEnum.LIGHT.getRequestLenght());
		bb.put((byte) NetworkGameRequestEnum.LIGHT.ordinal());
		bb.putShort((short) this.x);
		bb.putShort((short) this.y);
		bb.put((byte) this.radius);
		return bb.array();
	}
}
