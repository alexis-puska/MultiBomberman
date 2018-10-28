package com.mygdx.game.ia;

import java.util.PriorityQueue;

import com.mygdx.constante.Constante;

public class AStar {

	private PriorityQueue<AStarCell> open;
	private boolean ignoreBombe;
	private int searchlevel;
	private int[] tab;
	private AStarCell[] grid;
	private boolean[] closed;
	private boolean[] inOpen;
	private int startX;
	private int startY;
	private int endX;
	private int endY;

	public AStar(int[] tab) {
		this.tab = tab;
	}

	public void init(int startX, int startY, int endX, int endY, int searchlevel) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		while (!open.isEmpty()) {
			open.poll();
		}
		this.ignoreBombe = true;
		if (tab[endX + endY * Constante.GRID_SIZE_X] == 3) {
			this.ignoreBombe = false;
		}
		for (int i = 0; i < Constante.GRID_SIZE_X; i++) {
			for (int j = 0; j < Constante.GRID_SIZE_Y; j++) {
				grid[i + j * Constante.GRID_SIZE_X].configure(i, j, (startX == i && startY == j), endX, endY);
				closed[i + j * Constante.GRID_SIZE_X] = false;
				inOpen[i + j * Constante.GRID_SIZE_X] = false;
			}
		}
	}

	public void solve() {
		// a definir
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
