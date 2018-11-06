package com.mygdx.domain.enumeration;

import com.mygdx.enumeration.SpriteEnum;

public enum FireEnum {
	FIRE_CENTER(SpriteEnum.FIRE_CENTER),
	FIRE_UP_EXT(SpriteEnum.FIRE_TOP_EXT),
	FIRE_UP(SpriteEnum.FIRE_TOP),
	FIRE_LEFT_EXT(SpriteEnum.FIRE_LEFT_EXT),
	FIRE_LEFT(SpriteEnum.FIRE_LEFT),
	FIRE_DOWN_EXT(SpriteEnum.FIRE_DOWN_EXT),
	FIRE_DOWN(SpriteEnum.FIRE_DOWN),
	FIRE_RIGHT_EXT(SpriteEnum.FIRE_RIGHT_EXT),
	FIRE_RIGHT(SpriteEnum.FIRE_RIGHT);
	
	private SpriteEnum spriteEnum;
	
	private FireEnum(SpriteEnum spriteEnum) {
		this.spriteEnum = spriteEnum;
	}
	
	public SpriteEnum getSpriteEnum() {
		return this.spriteEnum;
	}
}
