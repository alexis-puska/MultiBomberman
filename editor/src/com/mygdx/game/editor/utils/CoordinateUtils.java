package com.mygdx.game.editor.utils;

import com.mygdx.constante.EditorConstante;

/**
 * Utils to transform java coordinate to libgdx coordinate. Prefere sav inside
 * json the libgdx coordonate system value instead of the java coordinate value
 * (code simplification in game)
 * 
 * @author apuskarczyk
 * 
 */
public class CoordinateUtils {

	private static final int SCREEN_SIZE_Y = 336;
	private static final int MAX_BLOC_Y = 21;

	private CoordinateUtils() {
		// unused constructor
	}

	/**
	 * @param y      coordinate of object to draw
	 * @param height height of object to draw
	 * @return the java y value
	 */
	public static int drawY(int y, int height) {
		return SCREEN_SIZE_Y - height - y;
	}

	/**
	 * @param clicY java coordinate y clic
	 * @return libgdx y coordinate
	 */
	public static int clickY(int clicY) {
		return SCREEN_SIZE_Y - clicY;
	}

	/**
	 * @param clicY java coordinate y clic
	 * @return libgdx case coordinate
	 */
	public static int clicGridY(int clicY) {
		return (SCREEN_SIZE_Y - clicY) / EditorConstante.GRID_SIZE_Y;
	}

	/**
	 * @param y libgdx y coordinate
	 * @return y java coordinate
	 */
	public static int invGridY(int y) {
		return MAX_BLOC_Y - y;
	}

	/**
	 * @param y libgdx y coordinate
	 * @return y java coordinate
	 */
	public static double invGridY(double y) {
		return ((double) MAX_BLOC_Y) - y;
	}
}
