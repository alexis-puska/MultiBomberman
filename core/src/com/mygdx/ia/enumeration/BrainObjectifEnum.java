package com.mygdx.ia.enumeration;

import com.mygdx.constante.CollisionConstante;

public enum BrainObjectifEnum {
	BOMBE(CollisionConstante.CATEGORY_BOMBE),
	BRICK(CollisionConstante.CATEGORY_BRICKS), 
	PLAYER(CollisionConstante.CATEGORY_PLAYER), 
	BONUS(CollisionConstante.CATEGORY_BONUS), 
	SAFE_PLACE((short)0);
	
	short objectif;
	
	private BrainObjectifEnum(short objectif) {
		this.objectif = objectif;
		
	}
	
	public short getObjectif() {
		return this.objectif;
	}
}
