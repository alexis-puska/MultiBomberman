package com.mygdx.service.network.dto.other;

import java.nio.ByteBuffer;

import com.mygdx.service.network.enumeration.NetworkGameRequestEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PauseDTO {

	public byte[] getBuffer() {
		ByteBuffer bb;
		bb = ByteBuffer.allocate(NetworkGameRequestEnum.PAUSE.getRequestLenght());
		bb.put((byte) NetworkGameRequestEnum.PAUSE.ordinal());
		return bb.array();
	}

}
