package com.mygdx.ia;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.PovDirection;
import com.mygdx.constante.CollisionConstante;
import com.mygdx.domain.Player;

public class BrainLevel2 extends Brain {

	public BrainLevel2(Player player) {
		super(player, (short) 0, CollisionConstante.CATEGORY_WALL);
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
			thinkLevel1();
		}
	}

	private void thinkLevel1() {
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
