package com.mygdx.game.ia;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.mygdx.constante.Constante;

public class SecureCellResolver {
	LinkedList<Integer> open;
	private Map<Integer, Short> level;
	private Set<Integer> tested;
	private int start;
	private int securedCellIndex;
	private Short unsecured;
	private Short unTestCell;

	public void init(int start, Map<Integer, Short> level, Short unSecured, Short unTestCell) {
		tested = new HashSet<>();
		open = new LinkedList<>();
		this.level = level;
		this.start = start;
		this.securedCellIndex = -1;
		this.unsecured = unSecured;
		this.unTestCell = unTestCell;
	}

	public int getSecuredCell() {
		return securedCellIndex;
	}

	public void solve() {
		open.add(start);
		tested.add(start);
		Integer current;
		while (true) {
			if (open.isEmpty()) {
				return;
			}
			current = open.pop();
			if (cellIsSecured(current)) {
				securedCellIndex = current;
				return;
			}
			loadNextCell(IAUtils.getLeftPos(current));
			loadNextCell(IAUtils.getRightPos(current));
			loadNextCell(IAUtils.getUpPos(current));
			loadNextCell(IAUtils.getDownPos(current));
		}
	}

	private void loadNextCell(int val) {
		if (!tested.contains(val)) {
			tested.add(val);
			open.add(val);
		}
	}

	public boolean cellIsSecured(int index) {
		if (level.containsKey(index) && ((level.get(index) & unsecured) > 0 || (level.get(index) & unTestCell) > 0)) {
			return false;
		}
		int calcX = index % Constante.GRID_SIZE_X;
		int calcY = Math.floorDiv(index, Constante.GRID_SIZE_X);
		for (int val = calcX - 1; val > 0; val--) {
			if (isUnSecured(val))
				return false;
			if (isWall(val))
				break;
		}
		for (int val = calcX + 1; val < Constante.GRID_SIZE_X - 1; val++) {
			if (isUnSecured(val))
				return false;
			if (isWall(val))
				break;
		}
		for (int val = calcY - 1; val > 0; val--) {
			if (isUnSecured(val))
				return false;
			if (isWall(val))
				break;
		}
		for (int val = calcY - 1; val < Constante.GRID_SIZE_Y - 1; val++) {
			if (isUnSecured(val))
				return false;
			if (isWall(val))
				break;
		}
		return true;
	}

	private boolean isUnSecured(int x) {
		return level.containsKey(x) && (level.get(x) & unsecured) > 0;
	}

	private boolean isWall(int x) {
		return level.containsKey(x) && (level.get(x) & unTestCell) > 0;
	}
}
