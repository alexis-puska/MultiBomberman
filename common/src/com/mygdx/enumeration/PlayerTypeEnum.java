package com.mygdx.enumeration;

public enum PlayerTypeEnum {
	HUMAN("game.menu.player.type.human"), CPU("game.menu.player.type.cpu"), NET("game.menu.player.type.network"),
	NONE("game.menu.player.type.none");

	private String key;

	private PlayerTypeEnum(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public static PlayerTypeEnum next(PlayerTypeEnum val, boolean network) {
		PlayerTypeEnum v = values()[(val.ordinal() + 1) % values().length];
		if (!network && v == PlayerTypeEnum.NET) {
			v = values()[(v.ordinal() + 1) % values().length];
		}
		return v;
	}

	public static PlayerTypeEnum previous(PlayerTypeEnum val, boolean network) {
		PlayerTypeEnum v = val;
		if ((val.ordinal() - 1) < 0) {
			v = values()[values().length - 1];
		} else {
			v = values()[val.ordinal() - 1];
		}
		if (!network && v == PlayerTypeEnum.NET) {
			if ((v.ordinal() - 1) < 0) {
				v = values()[values().length - 1];
			} else {
				v = values()[v.ordinal() - 1];
			}
		}
		return v;
	}
}
