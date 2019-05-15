package com.mygdx.enumeration;

import com.mygdx.constante.Constante;

public enum SpriteEnum {
	//@formatter:off
	BACKGROUND(0f),
	NETWORK(0f),
	FLAG(0f),
	CURSOR(1f / 10f),
	SPACESHIP(0f),
	TROLLEY(0f),
	BONUS(0f),
	BOMBE(0.16f),
	BOMBE_P(0.16f),
	BOMBE_D(0.16f),
	BOMBE_E(0.16f),
	BONUS_BURN((1f / (float) Constante.FPS) * 4f),
	POP(0f),
	MINE_C(1f / 8f),
	MINE_D(1f / 8f),
	MINE(0f),
	RAIL(0f),
	BUTTON(0f),
	HOLE(0f),
	UNDERWATER(1f/12f),
	TELEPORTER(1f / 10f),
	FIRE_CENTER(1f / 10f),
	FIRE_TOP_EXT(1f / 10f),
	FIRE_TOP(1f / 10f),
	FIRE_LEFT_EXT(1f / 10f),
	FIRE_LEFT(1f / 10f),
	FIRE_DOWN_EXT(1f / 10f),
	FIRE_DOWN(1f / 10f),
	FIRE_RIGHT_EXT(1f / 10f),
	FIRE_RIGHT(1f / 10f),
	LEVEL1(0.16f),
	LEVEL2(0.16f),
	LEVEL3(0.16f),
	LEVEL4(0.16f),
	LEVEL5(0.16f),
	LEVEL6(0.16f),
	LEVEL7(0.16f),
	LEVEL8(0.16f),
	LEVEL9(0.16f),
	LEVEL10(0.16f),
	SKY(0f),
	PREVIEW(0f),
	LEVEL(0f);
	//@formatter:on

	private float frameAnimationTime;

	private SpriteEnum(float frameAnimationTime) {
		this.frameAnimationTime = frameAnimationTime;
	}

	public float getFrameAnimationTime() {
		return frameAnimationTime;
	}
}
