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

	public static void main(String[] args) {
		Map<Integer, Short> m = new HashMap<>();
		m.put(19, CollisionConstante.CATEGORY_WALL);
		m.put(20, CollisionConstante.CATEGORY_WALL);
		m.put(22, CollisionConstante.CATEGORY_WALL);
		m.put(52, CollisionConstante.CATEGORY_WALL);
		m.put(87, CollisionConstante.CATEGORY_WALL);
		m.put(122, CollisionConstante.CATEGORY_WALL);
		m.put(82, CollisionConstante.CATEGORY_WALL);
		m.put(0, CollisionConstante.CATEGORY_WALL);
		m.put(10, CollisionConstante.CATEGORY_WALL);
		m.put(12, CollisionConstante.CATEGORY_WALL);
		m.put(46, CollisionConstante.CATEGORY_WALL);
		m.put(47, CollisionConstante.CATEGORY_WALL);
		m.put(48, CollisionConstante.CATEGORY_WALL);
		m.put(709, CollisionConstante.CATEGORY_WALL);
		m.put(710, CollisionConstante.CATEGORY_WALL);
		m.put(711, CollisionConstante.CATEGORY_WALL);
		AStar astar = new AStar();
		short evicted = CollisionConstante.CATEGORY_WALL | CollisionConstante.CATEGORY_BOMBE;
		astar.init(11, 50, evicted, m);
		astar.solve();
		if (astar.isSolved()) {
			AStarCell current = astar.getEnd();
			while (current.getParent() != null) {
				System.out.println(current.getIndex());
				current = current.getParent();
			}
		} else {
			System.out.println("NO PATH FOUND !!!!!!!!");
		}
	}

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
