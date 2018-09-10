package com.mygdx.enumeration;

import java.util.concurrent.ThreadLocalRandom;

public enum CharacterEnum {
	BOMBERMAN, BARBAR, KID, COSSAK, PRETTY, MEXICAN, PUNK, CHAN;

	public static CharacterEnum random() {
		int val = ThreadLocalRandom.current().nextInt(0, values().length);
		return values()[val];
	}

	public static CharacterEnum next(CharacterEnum val) {
		return values()[(val.ordinal() + 1) % values().length];
	}

	public static CharacterEnum previous(CharacterEnum val) {
		if ((val.ordinal() - 1) < 0) {
			return values()[values().length - 1];
		}
		return values()[(val.ordinal() - 1)];
	}
}
