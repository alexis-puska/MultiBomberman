package com.mygdx.domain.enumeration;

import com.mygdx.enumeration.SpriteEnum;

public enum BombeTypeEnum {
	BOMBE(SpriteEnum.BOMBE),
	BOMBE_P(SpriteEnum.BOMBE_P),
	BOMBE_MAX(SpriteEnum.BOMBE_D),
	BOMBE_RUBBER(SpriteEnum.BOMBE_E);
	
	private SpriteEnum spriteEnum;
	
	private BombeTypeEnum(SpriteEnum spriteEnum) {
		this.spriteEnum = spriteEnum;
	}
	
	public SpriteEnum getSpriteEnum() {
		return this.spriteEnum;
	}
}
