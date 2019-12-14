package com.mygdx.ia;

import java.util.LinkedList;

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
	private LinkedList<Integer> pathToObjectif;
	private Integer cellToReach;
	protected boolean bombeExploded;

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
		this.bombeExploded = true;
	}

	public abstract void think();

	protected boolean moveToObjectif() {
		// Gdx.app.log("BRAIN ", "pos cell : " + cellToReach + ", player pos : " +
		// this.player.getGridIndex());
		if (cellToReach == this.player.getGridIndex()) {
			// Gdx.app.log("BRAIN", "Cell diff of player, poll first cell");
			this.cellToReach = this.pathToObjectif.pollLast();
		}
		if (this.pathToObjectif.isEmpty()) {
			// Gdx.app.log("BRAIN", "Objectif reached");
			this.player.move(PovDirection.center);
			return true;
		}
		this.moveImpl(this.player.getGridIndex(), this.cellToReach);
		return false;
	}

	private void moveImpl(int current, int next) {
		// Gdx.app.log("BRAIN", "current : " + current + " next : " + next);
		int test = next - current;
		if (test == -1 || test == (Constante.GRID_SIZE_X - 1)) {
			// Gdx.app.log("BRAIN", "WEST");
			player.move(PovDirection.west);
		} else if (test == 1 || test == -(Constante.GRID_SIZE_X - 1)) {
			// Gdx.app.log("BRAIN", "EAST");
			player.move(PovDirection.east);
		} else if (test == -Constante.GRID_SIZE_X || test >= (Constante.GRID_SIZE_X * (Constante.GRID_SIZE_Y - 1))) {
			// Gdx.app.log("BRAIN", "SOUTH");
			player.move(PovDirection.south);
		} else if (test == Constante.GRID_SIZE_X || test <= (Constante.GRID_SIZE_X - 1)) {
			// Gdx.app.log("BRAIN", "NORTH");
			player.move(PovDirection.north);
		}
	}

	protected boolean searchBrickToDetroy(boolean initBfs) {
		if (initBfs) {
			this.objectifType = BrainObjectifEnum.BRICK;
			this.bfs.init(player.getGridIndex(), CollisionConstante.CATEGORY_BRICKS, player.getLevelDefinition());
		}
		if (player.levelHasBrickToDestroy()) {
			this.bfs.solve();
			if (this.bfs.isSolved()) {
				this.objectifIndex = this.bfs.getSolution();

				if (this.initPathToObjectif()) {
					// path found to objectif
					this.bfs.resetCellToAvoid();
					this.pathToObjectif = this.astar.getPath();
					// System.out.print("player "+ player.getIndex() + " : ");
					// this.pathToObjectif.stream().forEach(cell -> System.out.print(cell + " ->
					// "));
					// System.out.println();
					switch (objectifType) {
					case BRICK:
					case PLAYER:
						// enleve le dernier maillon de la chaine
						this.pathToObjectif.pollLast();
						break;
					case BOMBE:
					case BONUS:
					case SAFE_PLACE:
					default:
						break;
					}
					this.cellToReach = this.pathToObjectif.pollLast();
					return true;
				} else {
					// no path to objectifs -> mark cell to avoid
					this.bfs.addCellToAvoid(this.objectifIndex);
					if (bfs.hasCellToTest()) {
						// research to the next same objectif type
						return this.searchBrickToDetroy(false);
					} else {
						// no more cell to test, objectifs unreachable
						// Gdx.app.log("BRAIN", "no more cell to test");
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

	public void hourBombeExplode() {
		this.bombeExploded = true;
	}

	private boolean initPathToObjectif() {
		this.astar.init(player.getGridIndex(), this.objectifIndex, (short) (this.unSecure | this.excluseSearchPath),
				player.getLevelDefinition());
		this.astar.solve();
		return astar.isSolved();
	}

	protected boolean findSecurePlace(boolean initSCR) {
		if (initSCR) {
			this.scr.init(player.getGridIndex(), player.getLevelDefinition(), this.unSecure, this.excluseSearchPath);
		}
		this.scr.solve();
		if (this.scr.isSolved()) {
			this.objectifIndex = this.scr.getSecuredCell();
			if (this.initPathToObjectif()) {
				// path found to objectif
				this.scr.resetCellToAvoid();
				this.pathToObjectif = this.astar.getPath();
				System.out.print("player " + player.getIndex() + " : ");
				this.pathToObjectif.stream().forEach(cell -> System.out.print(cell + " -> "));
				System.out.println();
				this.cellToReach = this.pathToObjectif.pollLast();
				return true;
			} else {
				// no path to objectifs -> mark cell to avoid
				this.scr.addCellToAvoid(this.objectifIndex);
				if (scr.hasCellToTest()) {
					// research to the next same objectif type
					return this.findSecurePlace(false);
				} else {
					// no more cell to test, objectifs unreachable
					// Gdx.app.log("BRAIN", "no more cell to test");
					this.objectifIndex = -1;
					return false;
				}
			}
		}
		return false;
	}

	protected void waitToDead() {
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
