package com.mygdx.ia;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.mygdx.constante.Constante;

public class SecureCellResolver {
	LinkedList<Integer> open;
	private Map<Integer, Short> level;
	private Set<Integer> tested;
	private int start;
	private int securedCellIndex;
	private Short unsecured;
	private Short unTestCell;

	private List<Integer> cellToAvoid;

	public void init(int start, Map<Integer, Short> level, Short unSecured, Short unTestCell) {
		tested = new HashSet<>();
		open = new LinkedList<>();
		this.level = level;
		this.start = start;
		this.securedCellIndex = -1;
		this.unsecured = unSecured;
		this.unTestCell = unTestCell;
		this.cellToAvoid = new ArrayList<>();
	}

	public int getSecuredCell() {
		return securedCellIndex;
	}

	public boolean isSolved() {
		return securedCellIndex != -1;
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
		if (!tested.contains(val) && !cellToAvoid.contains(val)) {
			tested.add(val);
			open.add(val);
		}
	}

	public boolean cellIsSecured(int index) {
		int tmp = 0;
		if (isUnSecured(index) || isWall(index)) {
			return false;
		}
		Gdx.app.log("SCR", "cell not secured : "+ index);
		int calcX = index % Constante.GRID_SIZE_X;
		int calcY = Math.floorDiv(index, Constante.GRID_SIZE_X);
		for (int val = calcX - 1; val > 0; val--) {
			tmp = calcY * Constante.GRID_SIZE_X + val;
			if (isUnSecured(tmp)) {
				return false;
			} else if (isWall(tmp)) {
				break;
			}
		}
		for (int val = calcX + 1; val < Constante.GRID_SIZE_X - 1; val++) {
			tmp = calcY * Constante.GRID_SIZE_X + val;
			if (isUnSecured(tmp)) {
				return false;
			} else if (isWall(tmp)) {
				break;
			}
		}
		for (int val = calcY - 1; val > 0; val--) {
			tmp = val * Constante.GRID_SIZE_X + calcX;
			if (isUnSecured(tmp)) {
				return false;
			} else if (isWall(tmp)) {
				break;
			}
		}
		for (int val = calcY - 1; val < Constante.GRID_SIZE_Y - 1; val++) {
			tmp = val * Constante.GRID_SIZE_X + calcX;
			if (isUnSecured(tmp)) {
				return false;
			} else if (isWall(tmp)) {
				break;
			}
		}
		return true;
	}

	private boolean isUnSecured(int x) {
		return level.containsKey(x) && !cellToAvoid.contains(x) && (level.get(x) & unsecured) > 0;
	}

	private boolean isWall(int x) {
		return level.containsKey(x) && !cellToAvoid.contains(x) && (level.get(x) & unTestCell) > 0;
	}

	public void addCellToAvoid(int cellIndex) {
		this.cellToAvoid.add(cellIndex);
	}

	public void resetCellToAvoid() {
		this.cellToAvoid.clear();
	}

	public boolean hasCellToTest() {
		return (this.tested.size() + this.cellToAvoid.size()) < (Constante.GRID_SIZE_X * Constante.GRID_SIZE_Y);
	}
}
