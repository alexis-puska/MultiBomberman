package com.mygdx.domain.enumeration;

import java.util.HashMap;
import java.util.Map;

import com.mygdx.domain.game.BombeLight;
import com.mygdx.enumeration.SpriteEnum;

public enum BombeTypeEnum {
	//@formatter:off
	BOMBE(SpriteEnum.BOMBE, true, initBombe()), 
	BOMBE_P(SpriteEnum.BOMBE_P, true, initBombeP()),
	BOMBE_MAX(SpriteEnum.BOMBE_D, true, initBombeD()), 
	BOMBE_RUBBER(SpriteEnum.BOMBE_E, true, initBombeRubber());
	//@formatter:on

	private SpriteEnum spriteEnum;
	private boolean createLight;
	//private float light;
	private Map<Integer, BombeLight> offsetLight;

	private BombeTypeEnum(SpriteEnum spriteEnum, boolean createLight, //float light,
			Map<Integer, BombeLight> offsetLight) {
		this.spriteEnum = spriteEnum;
		this.createLight = createLight;
	//	this.light = light;
		this.offsetLight = offsetLight;
	}

	private static Map<Integer, BombeLight> initBombe() {
		Map<Integer, BombeLight> v = new HashMap<>();
		v.put(0, new BombeLight(6, 6, 4));
		v.put(1, new BombeLight(7, 7, 3));
		v.put(2, new BombeLight(6, 6, 4));
		v.put(3, new BombeLight(6, 6, 4));
		return v;
	}

	private static Map<Integer, BombeLight> initBombeP() {
		Map<Integer, BombeLight> v = new HashMap<>();
		v.put(0, new BombeLight(7, 7, 4));
		v.put(1, new BombeLight(7, 7, 4));
		v.put(2, new BombeLight(7, 7, 4));
		v.put(3, new BombeLight(7, 7, 4));
		return v;
	}

	private static Map<Integer, BombeLight> initBombeD() {
		Map<Integer, BombeLight> v = new HashMap<>();
		v.put(0, new BombeLight(-1, 0, 7));
		v.put(1, new BombeLight(-1, 0, 8));
		v.put(2, new BombeLight(-1, 0, 7));
		v.put(3, new BombeLight(-1, 0, 6));
		return v;
	}

	private static Map<Integer, BombeLight> initBombeRubber() {
		Map<Integer, BombeLight> v = new HashMap<>();
		v.put(0, new BombeLight(0, 6, 4));
		v.put(1, new BombeLight(-2, 6, 4));
		v.put(2, new BombeLight(0, 6, 4));
		v.put(3, new BombeLight(2, 6, 4));
		return v;
	}

	public SpriteEnum getSpriteEnum() {
		return this.spriteEnum;
	}

	public boolean isCreateLight() {
		return this.createLight;
	}

//	public float getLight() {
//		return this.light;
//	}

	public Map<Integer, BombeLight> getOffsetLight() {
		return this.offsetLight;
	}
}
