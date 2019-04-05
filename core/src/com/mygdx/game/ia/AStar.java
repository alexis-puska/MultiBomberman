package com.mygdx.game.ia;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import com.mygdx.constante.CollisionConstante;

public class AStar {

	private PriorityQueue<AStarCell> open;
	private Map<Integer, Short> level;
	private Set<Integer> tested;
	private int start;
	private int end;
	private Short evicted;

	private AStarCell endOfPath;
	private boolean solve;


	public void init(int start, int end, Short evicted, Map<Integer, Short> level) {
		tested = new HashSet<>();
		open = new PriorityQueue<>((o1, o2) -> {
			if (o1.getFinalCost() > o2.getFinalCost()) {
				return 1;
			} else if (o1.getFinalCost() < o2.getFinalCost()) {
				return -1;
			}
			return 0;
		});
		this.level = level;
		this.evicted = evicted;
		this.start = start;
		this.end = end;
		this.endOfPath = null;
		this.solve = false;
	}

	public void solve() {
		tested.add(start);
		open.add(new AStarCell(start, this.end, true, null));
		AStarCell current;
		while (true) {
			current = open.poll();

			
			if (current == null) {
				return;
			}
			tested.add(current.getIndex());
			if (current.getIndex() == end) {
				endOfPath = current;
				solve = true;
				break;
			}
			verifCell(current, IAUtils.getLeftPos(current.getIndex()));
			verifCell(current, IAUtils.getRightPos(current.getIndex()));
			verifCell(current, IAUtils.getUpPos(current.getIndex()));
			verifCell(current, IAUtils.getDownPos(current.getIndex()));
		}
	}

	private void verifCell(AStarCell current, int index) {
		if (!tested.contains(index)) {
			if (level.containsKey(index) && (level.get(index) & evicted) > 0) {
				tested.add(index);
			} else {
				open.add(new AStarCell(index, end, false, current));
			}
		}
	}

	public boolean isSolved() {
		return solve;
	}

	public AStarCell getEnd() {
		return endOfPath;
	}
}
