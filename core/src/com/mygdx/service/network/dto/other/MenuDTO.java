package com.mygdx.service.network.dto.other;

import java.nio.ByteBuffer;

import com.mygdx.service.network.enumeration.NetworkGameRequestEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MenuDTO {

	public byte[] getBuffer() {
		ByteBuffer bb;
		bb = ByteBuffer.allocate(NetworkGameRequestEnum.MENU.getRequestLenght());
		bb.put((byte) NetworkGameRequestEnum.MENU.ordinal());
		return bb.array();
	}

}
