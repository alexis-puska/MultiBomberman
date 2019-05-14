package com.mygdx.domain.enumeration;

import lombok.Getter;

@Getter
public enum BonusTypeEnum {
	DEATH,
	ROLLER,
	FIRE,
	FIRE_PLUS,
	BOMBE,
	BOMBE_P,
	KICK,
	GLOVE,
	BOMBE_RUBBER,
	BOMBE_MAX,
	SHOES,
	WALL,
	EGGS,
	SHIELD,
	BOMBE_LINE,
	NONE;// used for network, each second passed, send grid with bonus type on the level
	
}
