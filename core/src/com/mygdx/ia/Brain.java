package com.mygdx.ia;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.PovDirection;
import com.mygdx.constante.CollisionConstante;
import com.mygdx.constante.Constante;
import com.mygdx.domain.Player;
import com.mygdx.ia.enumeration.BrainObjectifEnum;
import com.mygdx.ia.enumeration.BrainStateEnum;

public abstract class Brain {

	protected final AStar astar;
	protected final BFS bfs;
	protected final SecureCellResolver scr;
	protected PovDirection prev;

	protected final Player player;

	// Define all element of game grid that can be view a danger by the brain player
	protected final short unSecure;

	// Define all element of game grid that can be exclude for the search of A*
	// algorithm pathfinding
	protected final short excluseSearchPath;

	/******************
	 * --- OBJECTIF ---
	 ******************/
	protected BrainObjectifEnum objectifType;
	protected int objectifIndex;

	/******************
	 * --- OBJECTIF ---
	 ******************/
	protected BrainStateEnum state;

	public Brain(Player player, short unSecure, short excludeSearchPath) {
		this.player = player;
		this.unSecure = unSecure;
		this.excluseSearchPath = excludeSearchPath;
		this.astar = new AStar();
		this.scr = new SecureCellResolver();
		this.bfs = new BFS();
		this.prev = PovDirection.east;
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

	protected boolean searchBrickToDetroy(boolean initBfs) {
		if (initBfs) {
			this.bfs.init(player.getGridIndex(), CollisionConstante.CATEGORY_BRICKS, player.getLevelDefinition());
		}
		if (player.levelHasBrickToDestroy()) {
			this.bfs.solve();
			if (this.bfs.isSolved()) {
				this.objectifIndex = this.bfs.getSolution();
				if (this.initPathToObjectif()) {
					// path found to objectif
					this.bfs.resetCellToAvoid();
					return true;
				} else {
					// no path to objectifs -> mark cell to avoid
					this.bfs.addCellToAvoid(this.objectifIndex);
					if (bfs.hasCellToTest()) {
						//research to the next same objectif type
						return this.searchBrickToDetroy(false);
					} else {
						// no more cell to test, objectifs unreachable
						Gdx.app.log("BRAIN", "no more cell to test");
						this.objectifIndex = -1;
						return false;
					}
				}
			} else {
				// no more cell to tests
				this.objectifIndex = -1;
				return false;
			}
		} else {
			// no more brick
			this.objectifIndex = -1;
			return false;
		}
	}

	public boolean initPathToObjectif() {
		this.astar.init(player.getGridIndex(), this.objectifIndex, this.unSecure, player.getLevelDefinition());
		this.astar.solve();
		return astar.isSolved();
	}

	public void findSecurePlace(BrainStateEnum nextState) {

	}

}
