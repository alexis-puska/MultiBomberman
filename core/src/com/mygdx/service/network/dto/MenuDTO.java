package com.mygdx.service.network.dto;

import java.nio.ByteBuffer;

import com.mygdx.service.network.enumeration.NetworkGameRequestEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuDTO {

	public MenuDTO() {

	}

	public MenuDTO(byte[] encoded) {
	}

	public byte[] getBuffer() {
		ByteBuffer bb;
		bb = ByteBuffer.allocate(NetworkGameRequestEnum.MENU.getRequestLenght());
		bb.put((byte) NetworkGameRequestEnum.MENU.ordinal());
		return bb.array();
	}
}
