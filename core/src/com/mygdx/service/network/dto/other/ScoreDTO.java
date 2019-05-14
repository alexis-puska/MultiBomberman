package com.mygdx.service.network.dto.other;

import java.nio.ByteBuffer;

import com.mygdx.service.network.enumeration.NetworkGameRequestEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScoreDTO {
	private int playerIndex;
	private int score;

	public ScoreDTO(int playerIndex, int score) {
		this.playerIndex = playerIndex;
		this.score = score;
	}

	public ScoreDTO(byte[] encoded) {
		ByteBuffer bb = ByteBuffer.wrap(encoded);
		this.playerIndex = (int) bb.get();
		this.score = (int) bb.getShort();
	}

	public byte[] getBuffer() {
		ByteBuffer bb;
		bb = ByteBuffer.allocate(NetworkGameRequestEnum.SCORE.getRequestLenght());
		bb.put((byte) NetworkGameRequestEnum.SCORE.ordinal());
		bb.put((byte) this.playerIndex);
		bb.putShort((short) this.score);
		return bb.array();
	}
}
