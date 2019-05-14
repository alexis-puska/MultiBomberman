package com.mygdx.service.network.dto.delta;

import java.nio.ByteBuffer;

import com.mygdx.domain.enumeration.FireEnum;
import com.mygdx.service.network.enumeration.NetworkGameRequestEnum;

import lombok.Getter;

@Getter
public class FireAppareadDTO {
	private int gridIndex;
	private FireEnum fireEnum;

	public FireAppareadDTO(int gridIndex, FireEnum fireEnum) {
		this.gridIndex = gridIndex;
		this.fireEnum = fireEnum;
	}

	public FireAppareadDTO(byte[] encoded) {
		ByteBuffer bb = ByteBuffer.wrap(encoded);
		this.gridIndex = (int) bb.getShort();
		this.fireEnum = FireEnum.values()[(int) bb.get()];
	}

	public byte[] getBuffer() {
		ByteBuffer bb;
		bb = ByteBuffer.allocate(NetworkGameRequestEnum.FIRE_APPEARED.getRequestLenght());
		bb.put((byte) NetworkGameRequestEnum.FIRE_APPEARED.ordinal());
		bb.putShort((short) this.gridIndex);
		bb.put((byte) this.fireEnum.ordinal());
		return bb.array();
	}
}
