package com.mygdx.ia;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.PovDirection;
import com.mygdx.constante.CollisionConstante;
import com.mygdx.constante.Constante;
import com.mygdx.domain.Player;
import com.mygdx.service.Context;

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
		if (!player.isDead()) {
			bfs.init(player.getGridIndex(), CollisionConstante.CATEGORY_BRICKS, player.getLevelDefinition());

			scr.init(0, player.getLevelDefinition(), CollisionConstante.CATEGORY_BOMBE,
					CollisionConstante.CATEGORY_WALL);
			bfs.solve();
			if (bfs.isSolved()) {
				Gdx.app.log("BRAIN", "bfs solution : " + bfs.getSolution());
			}
			astar.init(0, 400, CollisionConstante.CATEGORY_WALL, player.getLevelDefinition());
			astar.solve();
			scr.solve();
			switch (Context.getIaLevel()) {
			case 1:
				thinkLevel1();
				break;
			case 2:
				thinkLevel2();
				break;
			case 3:
				thinkLevel3();
				break;
			case 4:
				thinkLevel4();
				break;
			case 5:
				thinkLevel5();
			default:
				break;
			}
		}
	}

	private void thinkLevel1() {

		this.move(700, 0);

//		if (prev == PovDirection.east) {
//			player.move(PovDirection.north);
//			prev = PovDirection.north;
//		} else if (prev == PovDirection.north) {
//			player.move(PovDirection.west);
//			prev = PovDirection.west;
//		} else if (prev == PovDirection.west) {
//			player.move(PovDirection.south);
//			prev = PovDirection.south;
//		} else {
//			player.move(PovDirection.east);
//			prev = PovDirection.east;
//		}
	}

	private void thinkLevel2() {
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

	private void thinkLevel3() {
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

	private void thinkLevel4() {
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

	private void thinkLevel5() {
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

	private void move(int current, int next) {
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
