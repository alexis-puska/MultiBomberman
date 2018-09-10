package com.mygdx.enumeration;

import java.util.concurrent.ThreadLocalRandom;

public enum CharacterColorEnum {
	NONE, GREY, RED, BLUE, GREEN, GOLD, BROWN;

	public static CharacterColorEnum random() {
		int val = ThreadLocalRandom.current().nextInt(0, values().length);
		return values()[val];
	}

	public static CharacterColorEnum next(CharacterColorEnum val) {
		return values()[(val.ordinal() + 1) % values().length];
	}

	public static CharacterColorEnum previous(CharacterColorEnum val) {
		if ((val.ordinal() - 1) < 0) {
			return values()[values().length - 1];
		}
		return values()[(val.ordinal() - 1)];
	}
}
