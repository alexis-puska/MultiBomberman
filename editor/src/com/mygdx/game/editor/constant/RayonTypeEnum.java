package com.mygdx.game.editor.constant;

public enum RayonTypeEnum {
	BLACK(0),
	BLUE(2),
	GREEN(4),
	RED(6),
	WHITE(8),
	PURPLE(10),
	DARK_PURPLE(12),
	ORANGE_YELLOW(14),
	DOUBLE_ORANGE(16);

	private int index;
	
	private RayonTypeEnum(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
}
