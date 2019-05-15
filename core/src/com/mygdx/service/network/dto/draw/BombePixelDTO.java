package com.mygdx.service.network.dto.draw;

import java.nio.ByteBuffer;

import com.mygdx.constante.Constante;
import com.mygdx.domain.enumeration.BombeTypeEnum;
import com.mygdx.domain.game.Bombe;
import com.mygdx.service.network.enumeration.NetworkGameRequestEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BombePixelDTO {
	private float x;
	private float y;
	private BombeTypeEnum bombeTypeEnum;
	private int spriteIndex;

	public BombePixelDTO(Bombe bombe) {
		this.x = (bombe.getBodyX() - 0.5f) * Constante.GRID_PIXELS_SIZE_X;
		this.y = ((bombe.getBodyY() - 0.5f) * Constante.GRID_PIXELS_SIZE_Y) + bombe.getReboundOffset();
		this.bombeTypeEnum = bombe.getType();
		this.spriteIndex = bombe.getDrawIndex();
	}

	public byte[] getBuffer() {
		ByteBuffer bb;
		bb = ByteBuffer.allocate(NetworkGameRequestEnum.DRAW_BOMBE.getRequestLenght());
		bb.put((byte) NetworkGameRequestEnum.DRAW_BOMBE.ordinal());
		bb.putFloat(x);
		bb.putFloat(y);
		bb.put((byte) this.bombeTypeEnum.ordinal());
		bb.putShort((short) this.spriteIndex);
		return bb.array();
	}

	public BombePixelDTO(byte[] encoded) {
		ByteBuffer bb = ByteBuffer.wrap(encoded);
		this.x = bb.getFloat();
		this.y = bb.getFloat();
		this.bombeTypeEnum = BombeTypeEnum.values()[(int) bb.get()];
		this.spriteIndex = bb.getShort();
	}
}
