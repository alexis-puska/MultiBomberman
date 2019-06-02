package com.mygdx.ia;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class AStar {

	private PriorityQueue<AStarCell> open;
	private Map<Integer, Short> level;
	private Set<Integer> tested;
	private int start;
	private int end;
	private Short evicted;

	// result
	private AStarCell endOfPath;
	private boolean solve;
	private LinkedList<AStarCell> path;

	/**
	 * Initialization of AStart algorithm.
	 * 
	 * @param start   index of start cell
	 * @param end     index to destination cell
	 * @param evicted mask of element to be evicted when path is search
	 * @param level   desciption of the level
	 */
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

	/**
	 * Start the path search.
	 */
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
				buildPath();
				break;
			}
			verifCell(current, IAUtils.getLeftPos(current.getIndex()));
			verifCell(current, IAUtils.getRightPos(current.getIndex()));
			verifCell(current, IAUtils.getUpPos(current.getIndex()));
			verifCell(current, IAUtils.getDownPos(current.getIndex()));
		}
	}

	private void buildPath() {
		path = new LinkedList<>();
		AStarCell cell = endOfPath.getParent();
		while (true) {
			if (cell != null) {
				path.add(cell);
			} else {
				return;
			}
			cell = cell.getParent();
		}
	}

	/**
	 * Verify the index cell, if the cell desciption in level is not in the evicted
	 * mask, create and add the cell to the sortedList for the next iteration of
	 * Astar
	 * 
	 * @param current the current Cell (parent of the cell created on the index)
	 * @param index   cell index to test
	 */
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
