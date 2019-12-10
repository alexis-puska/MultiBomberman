package com.mygdx.ia;

import com.badlogic.gdx.controllers.PovDirection;
import com.mygdx.constante.Constante;
import com.mygdx.domain.Player;

public abstract class Brain {

	protected final AStar astar;
	protected final BFS bfs;
	protected final SecureCellResolver scr;
	protected PovDirection prev;

	protected final Player player;

	public Brain(Player player) {
		this.player = player;
		this.astar = new AStar();
		this.scr = new SecureCellResolver();
		this.bfs = new BFS();
		prev = PovDirection.east;
	}

	public abstract void think();

	protected void move(int current, int next) {
		int test = next - current;
		if (test == -1 || test == (Constante.GRID_SIZE_X - 1)) {
			player.move(PovDirection.west);
		} else if (test == 1 || test == -(Constante.GRID_SIZE_X - 1)) {
			player.move(PovDirection.east);
		} else if (test == -Constante.GRID_SIZE_X || test >= (Constante.GRID_SIZE_X * (Constante.GRID_SIZE_Y - 1))) {
			player.move(PovDirection.south);
		} else if (test == Constante.GRID_SIZE_X || test <= (Constante.GRID_SIZE_X - 1)) {
			player.move(PovDirection.north);
		}
	}
}
