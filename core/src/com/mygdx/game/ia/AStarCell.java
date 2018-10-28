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

	public AStarCell() {

	}

	public void configure(int x, int y, boolean origin, int endX, int endY) {
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AStarCell other = (AStarCell) obj;
		if (finalCost != other.finalCost)
			return false;
		if (heuristicCost != other.heuristicCost)
			return false;
		if (origin != other.origin)
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		if (x != other.x)
			return false;
		return y == other.y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + finalCost;
		result = prime * result + heuristicCost;
		result = prime * result + (origin ? 1231 : 1237);
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}
}
