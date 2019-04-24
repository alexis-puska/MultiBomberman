package com.mygdx.utils;

public class ReboundUtils {
	
	public static float calcReboundOffset(float deltaTime) {
		if (deltaTime < 47f) {
			return (float) Math.abs(Math.exp(-0.05 * (double) deltaTime) * 80 * Math.sin(0.2d * (double) deltaTime));
		} else {
			return (float) Math.abs(10 * Math.sin(0.2 * (double) deltaTime));
		}
	}
	
}
