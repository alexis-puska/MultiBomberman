package com.mygdx.utils;

import com.mygdx.constante.Constante;

public class GridUtils {

	private GridUtils() {
	}

	public static int calcIdxX(int x, int offset) {
		int r = x + offset;
		if (offset < 0 && r < 0) {
			while (r < 0) {
				r = Constante.GRID_SIZE_X + r;
			}
		}
		if (offset > 0) {
			while (r >= Constante.GRID_SIZE_X) {
				r = r - Constante.GRID_SIZE_X;
			}
		}
		return r;
	}

	public static int calcIdxY(int y, int offset) {
		int r = y + offset;
		if (offset < 0 && r < 0) {
			while (r < 0) {
				r = Constante.GRID_SIZE_Y + r;
			}
		}
		if (offset > 0) {
			while (r >= Constante.GRID_SIZE_Y) {
				r = r - Constante.GRID_SIZE_Y;
			}
		}
		return r;
	}

}
