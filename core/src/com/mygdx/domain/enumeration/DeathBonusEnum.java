package com.mygdx.domain.enumeration;

import java.util.concurrent.ThreadLocalRandom;

public enum DeathBonusEnum {
	DIAREE, // drop bombe each time player can
	CONSTIPATION, // no bombe drop
	EXCHANGE, // exchange position with an other player
	SLOW_BOMBE, // bombe take long time to explode
	FAST_BOMBE, // bombe take a short time to explode
	SLOW_MOVE, // move like en old man
	FAST_MOVE, // move realy fast
	LOW_BOMBE, // bombe explode with radius 1
	REVERSE_MOVE; // inverse move direction

	public static DeathBonusEnum random() {
		int val = ThreadLocalRandom.current().nextInt(0, values().length);
		return values()[val];
	}
}
