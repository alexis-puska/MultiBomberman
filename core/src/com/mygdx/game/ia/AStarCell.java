package com.mygdx.game.ia;

import com.mygdx.constante.Constante;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class AStarCell implements Comparable<AStarCell> {

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

	@Override
	public int compareTo(AStarCell o) {
		if (this.getFinalCost() > o.getFinalCost()) {
			return 1;
		} else if (this.getFinalCost() < o.getFinalCost()) {
			return -1;
		}
		return 0;
	}
}
