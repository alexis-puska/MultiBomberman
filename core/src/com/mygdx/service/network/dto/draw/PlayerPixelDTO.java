package com.mygdx.service.network.dto.draw;

import java.nio.ByteBuffer;

import com.mygdx.domain.Player;
import com.mygdx.enumeration.CharacterSpriteEnum;
import com.mygdx.enumeration.LouisColorEnum;
import com.mygdx.enumeration.LouisSpriteEnum;
import com.mygdx.service.network.enumeration.NetworkGameRequestEnum;
import com.mygdx.service.network.enumeration.NetworkPlayerStateEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlayerPixelDTO {

	private int playerIndex;
	private int score;
	private float x;
	private float y;
	private CharacterSpriteEnum characterSpriteEnum;
	private int characterSpriteIndex;
	private NetworkPlayerStateEnum networkPlayerStateEnum;
	private LouisSpriteEnum louisSpriteEnum;
	private LouisColorEnum louisColorEnum;
	private int louisSpriteIndex;

	public PlayerPixelDTO(Player player) {
		this.playerIndex = player.getIndex();
		this.score = player.getScore();
		switch (player.getState()) {
		case BURNING:
		case CARRY_BOMBE:
		case CRYING:
		case NORMAL:
		case TELEPORT:
		case THROW_BOMBE:
		case VICTORY:
			this.x = player.getBodyX();
			this.y = player.getBodyY();
			this.networkPlayerStateEnum = NetworkPlayerStateEnum.OTHER;
			break;
		case ON_LOUIS:
		case VICTORY_ON_LOUIS:
			this.x = player.getBodyX();
			this.y = player.getBodyY();
			this.networkPlayerStateEnum = NetworkPlayerStateEnum.LOUIS;
			break;
		case INSIDE_TROLLEY:
			this.x = player.getBodyX();
			this.y = player.getBodyY();
			this.networkPlayerStateEnum = NetworkPlayerStateEnum.TROLLEY;
			break;
		case BAD_BOMBER:
			this.x = player.getShipX();
			this.y = player.getShipY();
			this.networkPlayerStateEnum = NetworkPlayerStateEnum.BADBOMBER;
			break;
		case DEAD:
			this.x = 0.0f;
			this.y = 0.0f;
			this.networkPlayerStateEnum = NetworkPlayerStateEnum.DEAD;
			break;
		default:
			break;
		}
		this.characterSpriteEnum = player.getDrawCharacterSprite();
		this.characterSpriteIndex = player.getSpriteIndex();
		this.louisSpriteEnum = player.getDrawSpriteLouis();
		this.louisColorEnum = player.getLouisColor();
		this.louisSpriteIndex = player.getSpriteIndexLouis();
	}

	public byte[] getBuffer() {
		ByteBuffer bb;
		if (this.networkPlayerStateEnum != NetworkPlayerStateEnum.LOUIS) {
			bb = ByteBuffer.allocate(NetworkGameRequestEnum.DRAW_PLAYER.getRequestLenght());
			bb.put((byte) NetworkGameRequestEnum.DRAW_PLAYER.ordinal());
		} else {
			bb = ByteBuffer.allocate(NetworkGameRequestEnum.DRAW_PLAYER_ON_LOUIS.getRequestLenght());
			bb.put((byte) NetworkGameRequestEnum.DRAW_PLAYER_ON_LOUIS.ordinal());
		}
		bb.put((byte) this.playerIndex);
		bb.putShort((short) this.score);
		bb.putFloat(this.x);
		bb.putFloat(this.y);
		bb.put((byte) characterSpriteEnum.ordinal());
		bb.put((byte) this.characterSpriteIndex);
		bb.put((byte) networkPlayerStateEnum.ordinal());
		if (this.networkPlayerStateEnum == NetworkPlayerStateEnum.LOUIS) {
			bb.put((byte) louisSpriteEnum.ordinal());
			bb.put((byte) louisColorEnum.ordinal());
			bb.put((byte) this.louisSpriteIndex);
		}
		return bb.array();
	}

	public PlayerPixelDTO(byte[] encoded) {
		ByteBuffer bb = ByteBuffer.wrap(encoded);
		this.playerIndex = (int) bb.get();
		this.score = bb.getShort();
		this.x = bb.getFloat();
		this.y = bb.getFloat();
		byte index = bb.get();
		this.characterSpriteEnum = CharacterSpriteEnum.values()[index];
		this.characterSpriteIndex = (int) bb.get();
		this.networkPlayerStateEnum = NetworkPlayerStateEnum.values()[(int) bb.get()];
		if (bb.capacity() == NetworkGameRequestEnum.DRAW_PLAYER_ON_LOUIS.getRequestLenght() - 1) {
			this.louisSpriteEnum = LouisSpriteEnum.values()[(int) bb.get()];
			this.louisColorEnum = LouisColorEnum.values()[(int) bb.get()];
			this.louisSpriteIndex = (int) bb.get();
		}
	}
}
