package com.mygdx.service.network.dto;

import java.nio.ByteBuffer;

import com.mygdx.service.network.enumeration.NetworkGameRequestEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PauseDTO {

	public PauseDTO() {

	}

	public PauseDTO(byte[] encoded) {
	}

	public byte[] getBuffer() {
		ByteBuffer bb;
		bb = ByteBuffer.allocate(NetworkGameRequestEnum.PAUSE.getRequestLenght());
		bb.put((byte) NetworkGameRequestEnum.PAUSE.ordinal());
		return bb.array();
	}
}
