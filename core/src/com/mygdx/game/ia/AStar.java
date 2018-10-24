package com.mygdx.game.ia;

import java.util.PriorityQueue;

public class AStar {

	private PriorityQueue<AStarCell> cells;
	private boolean ignoreBombe;
	private int searchlevel;
	private int tab[];
	private AStarCell grid[];
	boolean closed[];
	boolean inOpen[];
	private int startX;
	private int startY;
	private int endX;
	private int endY;

	public AStar(int[] tab) {

	}

	public void init(int startX, int startY, int endX, int endY, int searchlevel) {

	}

	public void solve() {

	}

	public boolean isSolved() {
		return false;
	}

	public AStarCell getEnd() {
		return null;
	}

	private boolean checkAndUpdateCost(AStarCell current, AStarCell t, int cost) {
		return false;
	}

}
