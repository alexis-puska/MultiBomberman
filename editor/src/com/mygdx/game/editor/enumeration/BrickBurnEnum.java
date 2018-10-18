package com.mygdx.game.editor.enumeration;

import com.mygdx.enumeration.SpriteEnum;

public enum BrickBurnEnum {
	LEVEL1,
	LEVEL2,
	LEVEL3,
	LEVEL4,
	LEVEL5,
	LEVEL6,
	LEVEL7,
	LEVEL8,
	LEVEL9,
	LEVEL10;
	
	public static BrickBurnEnum getBrickFromSprite(SpriteEnum spriteEnum) {
		for(BrickBurnEnum e : BrickBurnEnum.values()) {
			if(e.toString().equals(spriteEnum.toString())) {
				return e;
			}
		}
		return BrickBurnEnum.LEVEL1;
	}
	
	public static SpriteEnum getSpriteFromBrick(BrickBurnEnum birkcBurnEnum) {
		for(SpriteEnum se : SpriteEnum.values()) {
			if(se.toString().equals(birkcBurnEnum.toString())) {
				return se;
			}
		}
		return SpriteEnum.LEVEL1;
	}
	
}
