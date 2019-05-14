package com.mygdx.service.network.utils;

import com.mygdx.constante.Constante;

public class NetworkRequestUtils {

	private NetworkRequestUtils() {
		// unused constrctor
	}

	public static int calcBufferSizeForBrick() {
		int nb = Constante.GRID_SIZE_X * Constante.GRID_SIZE_Y;
		int nbCalc = nb;
		if (nb % 8 != 0) {
			nbCalc = (nb / 8);
			nbCalc = (nbCalc + 1) * 8;
		}
		return nbCalc / 8;
	}

	public static int calcBufferSizeForBonus() {
		return Constante.GRID_SIZE_X * Constante.GRID_SIZE_Y;
	}

}
