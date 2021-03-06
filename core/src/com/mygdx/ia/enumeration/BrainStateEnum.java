package com.mygdx.ia.enumeration;

public enum BrainStateEnum {
	
	DROP_BOMBE,
	
	FIND_PLAYER,
	FIND_BONUS,
	FIND_BRICK,
	FIND_SECURE,
	FIND_BOMBE,
	
	GO_TO_DROP_BOMBE,
	GO_TO_NEAR_PLAYER,
	GO_TO_TAKE_BONUS,
	GO_TO_SECURE,
	GO_TO_BOMBE,
	
	WAIT,
	WAIT_FOR_EXPLODE,
	WAIT_AND_CHECK_SECURE,
	WAIT_TO_DEAD, 
	WAIT_FOR_END_OF_FIRE;
	
}
