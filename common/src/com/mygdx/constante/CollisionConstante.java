package com.mygdx.constante;

public class CollisionConstante {
	//@formatter:off
	public static final short CATEGORY_PLAYER	 		= 0b0000000000000001;
	public static final short CATEGORY_PLAYER_HITBOX	= 0b0000000000000010;
	public static final short CATEGORY_WALL				= 0b0000000000000100;
	public static final short CATEGORY_FIRE				= 0b0000000000001000;
	public static final short CATEGORY_BRICKS	 		= 0b0000000000010000;
	public static final short CATEGORY_TELEPORTER 		= 0b0000000000100000;
	public static final short CATEGORY_TROLLEY			= 0b0000000001000000;
	public static final short CATEGORY_MINE	 			= 0b0000000010000000;
	public static final short CATEGORY_BUTTONS	 		= 0b0000000100000000;
	public static final short CATEGORY_HOLE		 		= 0b0000001000000000;
	public static final short CATEGORY_HOLE_BLOCK		= 0b0000010000000000;
	public static final short CATEGORY_BOMBE		 	= 0b0000100000000000;
	public static final short CATEGORY_BONUS		 	= 0b0001000000000000;
	public static final short CATEGORY_RAIL_SPACESHIP	= 0b0010000000000000;
	public static final short CATEGORY_SPACESHIP 		= 0b0100000000000000;
	//@formatter:on
	/**
	 * GROUPS , If a category is on a mask group bits, this create a collision
	 * (contact), if not, no collision create
	 */
	public static final short GROUP_PLAYER_BODY = CATEGORY_WALL | CATEGORY_BRICKS | CATEGORY_BOMBE
			| CATEGORY_HOLE_BLOCK;
	public static final short GROUP_PLAYER_BODY_PASS_WALL = CATEGORY_WALL | CATEGORY_BOMBE | CATEGORY_HOLE_BLOCK;
	public static final short GROUP_PLAYER_HITBOX = CATEGORY_BOMBE | CATEGORY_BONUS | CATEGORY_FIRE | CATEGORY_HOLE
			| CATEGORY_BUTTONS | CATEGORY_MINE | CATEGORY_TROLLEY | CATEGORY_TELEPORTER;
	public static final short GROUP_BOMBE = CATEGORY_WALL | CATEGORY_BRICKS | CATEGORY_BOMBE | CATEGORY_HOLE_BLOCK
			| CATEGORY_FIRE | CATEGORY_PLAYER | CATEGORY_PLAYER_HITBOX | CATEGORY_TROLLEY;
	public static final short GROUP_TROLLEY = CATEGORY_WALL | CATEGORY_PLAYER_HITBOX | CATEGORY_BRICKS | CATEGORY_BOMBE;
	public static final short GROUP_MINE = CATEGORY_PLAYER_HITBOX;
	public static final short GROUP_SPACESHIP = CATEGORY_RAIL_SPACESHIP;
	public static final short GROUP_RAIL_SPACESIP = CATEGORY_SPACESHIP;

	private CollisionConstante() {
		// empty private constructor
	}
}
