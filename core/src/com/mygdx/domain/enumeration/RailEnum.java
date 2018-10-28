package com.mygdx.domain.enumeration;

import lombok.Getter;

@Getter
public enum RailEnum {

	DOWN_TO_RIGHT(0, 0b0000000000000011),
	HORIZONTAL(1, 0b0000000000001010),
	LEFT_TO_DOWN(2, 0b0000000000001001),
	UP_TO_RIGHT(3, 0b0000000000000110),
	VERTICAL(4, 0b0000000000000101),
	LEFT_TO_UP(5, 0b0000000000001100),
	START_LEFT(6, 0b0000000000000010),
	START_RIGHT(7, 0b0000000000001000);
	
	private int textureIndex;
	private int direction;

	private RailEnum(int textureIndex, int direction) {
		this.textureIndex = textureIndex;
		this.direction = direction;
	}

}
