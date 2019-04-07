package com.mygdx.game.ia;

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

	public void init(int start, Short search, Map<Integer, Short> level) {
		tested = new HashSet<>();
		open = new LinkedList<>();
		this.level = level;
		this.search = search;
		this.start = start;
	}

	public void solve() {
		open.add(start);
		Integer current;
		while (true) {
			current = open.pop();
			tested.add(current);
			if ((level.get(current) & search) > 0) {
				break;
			}
			verifCell(IAUtils.getLeftPos(current));
			verifCell(IAUtils.getRightPos(current));
			verifCell(IAUtils.getUpPos(current));
			verifCell(IAUtils.getDownPos(current));
		}
	}

	private void verifCell(int left) {
		if (!tested.contains(left)) {
			open.add(left);
		}
	}

	public boolean isSolved() {
		return false;
	}
}
