package com.mygdx.enumeration;

import java.util.concurrent.ThreadLocalRandom;

public enum CharacterEnum {
	BOMBERMAN,
	BARBAR,
	KID,
	COSSAK,
	PRETTY,
	MEXICAN,
	PUNK,
	CHAN;
	
	public static CharacterEnum random() {
		int val = ThreadLocalRandom.current().nextInt(0, values().length);
		return values()[val];
	}
}
