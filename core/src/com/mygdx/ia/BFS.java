package com.mygdx.ia;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class BFS {

	LinkedList<Integer> open;
	private Map<Integer, Short> level;
	private Set<Integer> tested;
	private int start;
	private Short search;

	private Integer solution;

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
		if (!tested.contains(val)) {
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
}
