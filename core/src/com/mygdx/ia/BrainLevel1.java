package com.mygdx.ia;

import com.badlogic.gdx.controllers.PovDirection;
import com.mygdx.constante.CollisionConstante;
import com.mygdx.domain.Player;

public class BrainLevel1 extends Brain {

	public BrainLevel1(Player player) {
		super(player);
	}

	public void think() {
		if (!player.isDead()) {
			bfs.init(player.getGridIndex(), CollisionConstante.CATEGORY_BRICKS, player.getLevelDefinition());

			scr.init(player.getGridIndex(), player.getLevelDefinition(), CollisionConstante.CATEGORY_BOMBE,
					CollisionConstante.CATEGORY_WALL);
			bfs.solve();
			// if (bfs.isSolved()) {
			// Gdx.app.log("BRAIN LVL 1", "bfs solution : " + bfs.getSolution());
			// }
			astar.init(player.getGridIndex(), bfs.getSolution(), CollisionConstante.CATEGORY_WALL,
					player.getLevelDefinition());
			astar.solve();
			scr.solve();
			thinkLevel1();
		}
	}

	private void thinkLevel1() {

		// if (astar.isSolved()) {
		// AStarCell current = astar.getPath().pollLast();
		// AStarCell next = astar.getPath().peekLast();
		// if (player.getGridIndex() == current.getIndex()) {
		// Gdx.app.log("BRAIN LVL 1", "current : " + current.getIndex() + ", next : " +
		// next != null ? Integer.toString(next.getIndex()) : "-1");
		// }else {
		// Gdx.app.log("BRAIN LVL 1", "player : " +
		// Integer.toString(player.getGridIndex()) + ", current : " + (current != null ?
		// Integer.toString(current.getIndex()) : "-1"));
		// }
		// }else {
		// Gdx.app.log("BRAIN LVL 1", "Astar not solved");
		// }

		// this.move(700, 0);
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
