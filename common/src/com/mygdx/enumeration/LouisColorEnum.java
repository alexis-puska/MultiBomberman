package com.mygdx.enumeration;

import java.util.concurrent.ThreadLocalRandom;

public enum LouisColorEnum {
	NONE, GOLD, GREEN, PINK, BROWN;

	public static LouisColorEnum random() {
		int val = ThreadLocalRandom.current().nextInt(0, values().length);
		return values()[val];
	}
}
