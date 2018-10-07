package com.mygdx.game.editor.utils;

import com.mygdx.game.editor.constant.Constante;

/**
 * Utils to transform java coordinate to libgdx coordinate. Prefere sav inside
 * json the libgdx coordonate system value instead of the java coordinate value
 * (code simplification in game)
 * 
 * @author apuskarczyk
 * 
 */
public class CoordinateUtils {

	/**
	 * @param y coordinate of object to draw
	 * @param height height of object to draw
	 * @return the java y value
	 */
	public static int drawY(int y, int height) {
		return 500 - height - y;
	}

	/**
	 * @param clicY java coordinate y clic
	 * @return libgdx y coordinate
	 */
	public static int clickY(int clicY) {
		return 500 - clicY;
	}

	/**
	 * @param clicY java coordinate y clic
	 * @return libgdx case coordinate
	 */
	public static int clicGridY(int clicY) {
		return (500 - clicY) / Constante.GRID_SIZE;
	}

	/** 
	 * @param y libgdx y coordinate
	 * @return y java coordinate
	 */
	public static int invGridY(int y) {
		return 24 - y;
	}
	
	/** 
	 * @param y libgdx y coordinate
	 * @return y java coordinate
	 */
	public static double invGridY(double y) {
		return 24.0 - y;
	}
}
