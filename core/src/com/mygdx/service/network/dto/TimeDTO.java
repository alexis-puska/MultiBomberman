package com.mygdx.service.network.dto;

import java.nio.ByteBuffer;

import com.mygdx.service.network.enumeration.NetworkGameRequestEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeDTO {
	private float time;

	public TimeDTO(float time) {
		this.time = time;
	}

	public TimeDTO(byte[] encoded) {
		ByteBuffer bb = ByteBuffer.wrap(encoded);
		this.time = bb.getFloat();
	}

	public byte[] getBuffer() {
		ByteBuffer bb;
		bb = ByteBuffer.allocate(NetworkGameRequestEnum.TIME.getRequestLenght());
		bb.put((byte) NetworkGameRequestEnum.TIME.ordinal());
		bb.putFloat(this.time);
		return bb.array();
	}
}
