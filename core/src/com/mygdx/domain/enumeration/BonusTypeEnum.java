package com.mygdx.domain.enumeration;

import lombok.Getter;

@Getter
public enum BonusTypeEnum {
	DEATH(0),
	ROLLER(1),
	FIRE(2),
	FIRE_PLUS(3),
	BOMBE(4),
	BOMBE_P(5),
	KICK(6),
	GLOVE(7),
	BOMBE_RUBBER(8),
	BOMBE_MAX(9),
	SHOES(10),
	WALL(11),
	EGGS(12),
	SHIELD(13),
	BOMBE_LINE(14);
	
	private int index;
	
	private BonusTypeEnum(int index) {
		this.index = index;
	}
}
