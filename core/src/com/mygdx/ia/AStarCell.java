package com.mygdx.ia;

import com.mygdx.constante.Constante;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AStarCell implements Comparable<AStarCell> {
	private int pos;
	private int finalCost;
	private int heuristicCost;
	private boolean origin;
	private AStarCell parent;

	public AStarCell(int pos, int end, boolean origin, AStarCell parent) {
		this.pos = pos;
		int x = pos % Constante.GRID_SIZE_X;
		int y = pos / Constante.GRID_SIZE_X;
		int endX = end % Constante.GRID_SIZE_X;
		int endY = end / Constante.GRID_SIZE_X;
		int heuristicCostStandart = Math.abs(x - endX) + Math.abs(y - endY);
		this.heuristicCost = heuristicCostStandart;
		this.finalCost = heuristicCost + 1;
		this.origin = origin;
		this.parent = parent;
	}

	public int getIndex() {
		return this.pos;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + finalCost;
		result = prime * result + heuristicCost;
		result = prime * result + (origin ? 1231 : 1237);
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		result = prime * result + pos;
		return result;
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
		} else if (!parent.equals(other.parent)) {
			return false;
		} else if (pos != other.pos) {
			return false;
		}
		return true;
	}
}
