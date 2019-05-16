package com.mygdx.service.network.dto.delta;

import java.nio.ByteBuffer;

import com.mygdx.domain.enumeration.BonusTypeEnum;
import com.mygdx.service.network.enumeration.NetworkGameRequestEnum;

import lombok.Getter;

@Getter
public class BonusRevealedDTO {
	private int gridIndex;
	private BonusTypeEnum type;

	public BonusRevealedDTO(int gridIndex, BonusTypeEnum type) {
		this.gridIndex = gridIndex;
		this.type = type;
	}

	public BonusRevealedDTO(byte[] encoded) {
		ByteBuffer bb = ByteBuffer.wrap(encoded);
		this.gridIndex = (int) bb.getShort();
		this.type = BonusTypeEnum.values()[(int) bb.get()];
	}

	public byte[] getBuffer() {
		ByteBuffer bb;
		bb = ByteBuffer.allocate(NetworkGameRequestEnum.BONUS_REVEALED.getRequestLenght());
		bb.put((byte) NetworkGameRequestEnum.BONUS_REVEALED.ordinal());
		bb.putShort((short) this.gridIndex);
		bb.put((byte) this.type.ordinal());
		return bb.array();
	}
}
