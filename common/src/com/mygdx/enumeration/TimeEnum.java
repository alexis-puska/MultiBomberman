package com.mygdx.enumeration;

public enum TimeEnum {

	//@formatter:off
	ONE(1, "game.menu.ruleScreen.time.one"),
	TWO(2, "game.menu.ruleScreen.time.two"),
	THREE(3, "game.menu.ruleScreen.time.three"),
	FOUR(4, "game.menu.ruleScreen.time.four"),
	FIVE(5, "game.menu.ruleScreen.time.five"),
	INF(-1, "game.menu.ruleScreen.time.infinite");
	//@formatter:on

	private int time;
	private String key;

	private TimeEnum(int time, String key) {
		this.time = time;
		this.key = key;
	}

	public int getTime() {
		return time;
	}

	public String getKey() {
		return key;
	}

	public static TimeEnum next(TimeEnum val) {
		return values()[(val.ordinal() + 1) % values().length];
	}

	public static TimeEnum previous(TimeEnum val) {
		if ((val.ordinal() - 1) < 0) {
			return values()[values().length - 1];
		}
		return values()[(val.ordinal() - 1)];
	}
}
