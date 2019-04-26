package com.mygdx.game.ia;

import com.mygdx.constante.Constante;

public class IAUtils {

	public static void main(String[] args) {
		
	}

	public static int getLeftPos(int pos) {
		int r = pos - 1;
		if (pos % 35 == 0) {
			r += Constante.GRID_SIZE_X;
		}
		return r;
	}

	public static int getRightPos(int pos) {
		int r = pos + 1;
		if ((r % Constante.GRID_SIZE_X) == 0) {
			r -= Constante.GRID_SIZE_X;
		}
		return r;
	}

	public static int getUpPos(int pos) {
		int r = pos + Constante.GRID_SIZE_X;
		if (r >= (Constante.GRID_SIZE_X * Constante.GRID_SIZE_Y)) {
			r -= (Constante.GRID_SIZE_X * Constante.GRID_SIZE_Y);
		}
		return r;
	}

	public static int getDownPos(int pos) {
		int r = pos - Constante.GRID_SIZE_X;
		if (r <= -1) {
			r += (Constante.GRID_SIZE_X * Constante.GRID_SIZE_Y);
		}
		return r;
	}

}
