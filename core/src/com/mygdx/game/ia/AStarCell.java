package com.mygdx.game.ia;

import com.mygdx.constante.Constante;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AStarCell {

	private int finalCost;
	private int heuristicCost;
	private int x;
	private int y;
	private boolean origin;
	private AStarCell parent;

	public AStarCell(int x, int y, boolean origin, int endX, int endY) {
		this.x = x;
		this.y = y;
		finalCost = 0;
		heuristicCost = Math.abs(x - endX) + Math.abs(y - endY);
		this.origin = origin;
		this.parent = null;
	}

	public int getIndex() {
		return this.x + this.y * Constante.GRID_SIZE_X;
	}

}
