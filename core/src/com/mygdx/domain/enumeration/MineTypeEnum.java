package com.mygdx.domain.enumeration;

import java.util.concurrent.ThreadLocalRandom;

public enum MineTypeEnum {

	BEND, 
	STRAIGHT;

	public static MineTypeEnum random() {
		int val = ThreadLocalRandom.current().nextInt(0, values().length);
		return values()[val];
	}

}
