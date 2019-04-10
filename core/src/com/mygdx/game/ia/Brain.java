package com.mygdx.game.ia;

import com.badlogic.gdx.controllers.PovDirection;
import com.mygdx.constante.CollisionConstante;
import com.mygdx.domain.Player;

public class Brain {

	private final AStar astar;
	private final BFS bfs;
	private final SecureCellResolver scr;
	private PovDirection prev;

	private final Player player;

	public Brain(Player player) {
		this.player = player;
		this.astar = new AStar();
		this.scr = new SecureCellResolver();
		this.bfs = new BFS();
		prev = PovDirection.east;
	}

	public void think() {

		bfs.init(player.getGridIndex(), CollisionConstante.CATEGORY_BUTTONS, player.getLevelDefinition());
		astar.init(0, 400, CollisionConstante.CATEGORY_WALL, player.getLevelDefinition());
		scr.init(0, player.getLevelDefinition(), CollisionConstante.CATEGORY_BOMBE, CollisionConstante.CATEGORY_WALL);
		bfs.solve();
		astar.solve();
		scr.solve();

		if (prev == PovDirection.east) {
			player.move(PovDirection.north);
			prev = PovDirection.north;
		} else if (prev == PovDirection.north) {
			player.move(PovDirection.west);
			prev = PovDirection.west;
		} else if (prev == PovDirection.west) {
			player.move(PovDirection.south);
			prev = PovDirection.south;
		} else {
			player.move(PovDirection.east);
			prev = PovDirection.east;
		}
	}
}
