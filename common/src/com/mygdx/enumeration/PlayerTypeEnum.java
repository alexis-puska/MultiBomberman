package com.mygdx.enumeration;

public enum PlayerTypeEnum {
	HUMAN, CPU, NET, NONE;

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
		}
		if (!network && v == PlayerTypeEnum.NET) {
			if ((v.ordinal() - 1) < 0) {
				v = values()[values().length - 1];
			}
		}
		return v;
	}
}
