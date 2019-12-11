package com.mygdx.ia;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.mygdx.constante.Constante;

public class BFS {

	LinkedList<Integer> open;
	private Map<Integer, Short> level;
	private Set<Integer> tested;
	private int start;
	private Short search;

	private Integer solution;

	private List<Integer> cellToAvoid;

	public BFS() {
		this.cellToAvoid = new ArrayList<>();
	}

	public void init(int start, Short search, Map<Integer, Short> level) {
		tested = new HashSet<>();
		open = new LinkedList<>();
		this.level = level;
		this.search = search;
		this.start = start;
		this.solution = -1;
	}

	public void solve() {
		open.add(start);
		tested.add(start);
		Integer current;
		while (true) {
			if (open.isEmpty()) {
				break;
			}
			current = open.pop();
			if (level.containsKey(current) && (level.get(current) & search) > 0) {
				solution = current;
				break;
			}
			verifCell(IAUtils.getLeftPos(current));
			verifCell(IAUtils.getRightPos(current));
			verifCell(IAUtils.getUpPos(current));
			verifCell(IAUtils.getDownPos(current));
		}
	}

	private void verifCell(int val) {
		if (!tested.contains(val) && !cellToAvoid.contains(val)) {
			tested.add(val);
			open.add(val);
		}
	}

	public boolean isSolved() {
		return solution != -1;
	}

	public int getSolution() {
		return solution;
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
