package com.mygdx.domain.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.constante.CollisionConstante;
import com.mygdx.constante.Constante;
import com.mygdx.domain.Player;
import com.mygdx.domain.common.BodyAble;
import com.mygdx.domain.enumeration.BombeTypeEnum;
import com.mygdx.domain.enumeration.FireEnum;
import com.mygdx.domain.level.Level;
import com.mygdx.domain.level.Wall;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.SpriteService;

public class Bombe extends BodyAble {

	private static final int NB_FRAME = 4;

	protected int x;
	protected int y;
	private int strenght;
	private BombeTypeEnum type;
	private Player player;
	private int countDown;
	private boolean exploded;

	private int frameCounter;
	private int offsetSprite;
	private int nbFrameForAnimation;
	private int offsetSpriteAnimation;

	public Bombe(Level level, World world, MultiBombermanGame mbGame, int strenght, int x, int y, BombeTypeEnum type,
			Player player, int countDown) {
		this.world = world;
		this.mbGame = mbGame;
		this.level = level;
		this.strenght = strenght;
		this.exploded = false;
		this.x = x;
		this.y = y;
		this.type = type;
		this.player = player;
		this.countDown = countDown;
		this.frameCounter = 0;
		this.offsetSprite = 0;
		this.nbFrameForAnimation = 4;
		createBody();
	}

	@Override
	protected void createBody() {
		BodyDef groundBodyDef = new BodyDef();
		CircleShape groundBox = new CircleShape();
		groundBox.setRadius(0.5f);
		groundBodyDef.position.set(new Vector2((float) this.x + 0.5f, (float) this.y + 0.5f));
		this.body = world.createBody(groundBodyDef);
		Fixture fixture = body.createFixture(groundBox, 0.0f);
		fixture.setFriction(0f);
		fixture.setUserData(this);
		Filter filter = new Filter();
		filter.categoryBits = CollisionConstante.CATEGORY_FIRE;
		filter.maskBits = CollisionConstante.CATEGORY_PLAYER_HITBOX;
		fixture.setFilterData(filter);
	}

	@Override
	public void drawIt() {
		mbGame.getBatch().draw(SpriteService.getInstance().getSprite(type.getSpriteEnum(), offsetSpriteAnimation),
				(float) this.x * 18, (float) this.y * 16);
	}

	@Override
	public void update() {
		if (frameCounter > NB_FRAME) {
			frameCounter = 0;
			offsetSprite++;
			if (offsetSprite >= nbFrameForAnimation) {
				offsetSprite = 0;
			}
		}
		frameCounter++;
		switch (offsetSprite) {
		case 0:
			offsetSpriteAnimation = 1;
			break;
		case 1:
			offsetSpriteAnimation = 0;
			break;
		case 2:
			offsetSpriteAnimation = 1;
			break;
		case 3:
		default:
			offsetSpriteAnimation = 2;
			break;
		}
		switch (type) {
		case BOMBE:
		case BOMBE_MAX:
		case BOMBE_RUBBER:
			countDown--;
			break;
		case BOMBE_P:
			if (countDown == 1) {
				countDown--;
			}
			break;
		default:
			break;
		}

		if (countDown == 0) {
			explode();
		}
	}

	private void explode() {
		int calcX = 0;
		int calcY = 0;
		if (this.level.getOccupedWallBrick()[this.x][this.y] == null) {
			this.level.getFires()
					.add(new Fire(this.world, this.mbGame, this.level, this.x, this.y, FireEnum.FIRE_CENTER));
		}
		for (int i = 1; i <= this.strenght; i++) {
			calcX = calcIdxX(this.x, i);
			calcY = this.y;
			if (generateFire(calcX, calcY, i == this.strenght ? FireEnum.FIRE_RIGHT_EXT : FireEnum.FIRE_RIGHT)) {
				break;
			}
		}
		for (int i = 1; i <= this.strenght; i++) {
			calcX = this.x;
			calcY = calcIdxY(this.y, -i);
			if (generateFire(calcX, calcY, i == this.strenght ? FireEnum.FIRE_DOWN_EXT : FireEnum.FIRE_DOWN)) {
				break;
			}
		}
		for (int i = 1; i <= this.strenght; i++) {
			calcX = calcIdxX(this.x, -i);
			calcY = this.y;
			if (generateFire(calcX, calcY, i == this.strenght ? FireEnum.FIRE_LEFT_EXT : FireEnum.FIRE_LEFT)) {
				break;
			}
		}
		for (int i = 1; i <= this.strenght; i++) {
			calcX = this.x;
			calcY = calcIdxY(this.y, i);
			if (generateFire(calcX, calcY, i == this.strenght ? FireEnum.FIRE_UP_EXT : FireEnum.FIRE_UP)) {
				break;
			}
		}
		exploded = true;
		this.player.bombeExploded();
	}

	private boolean generateFire(int calcX, int calcY, FireEnum fireEnum) {
		if (this.level.getOccupedWallBrick()[calcX][calcY] != null) {
			this.level.getOccupedWallBrick()[calcX][calcY].action();
			if (this.type != BombeTypeEnum.BOMBE_MAX
					|| this.level.getOccupedWallBrick()[calcX][calcY].getClass().equals(Wall.class)) {
				return true;
			}
		} else {
			this.level.getFires().add(new Fire(this.world, this.mbGame, this.level, calcX, calcY, fireEnum));
		}
		return false;
	}

	private int calcIdxX(int x, int offset) {
		int r = x + offset;
		if (offset < 0 && r < 0) {
			r = Constante.GRID_SIZE_X + r;
		}
		if (offset > 0 && r >= Constante.GRID_SIZE_X) {
			r = r - Constante.GRID_SIZE_X;
		}
		return r;
	}

	private int calcIdxY(int y, int offset) {
		int r = y + offset;
		if (offset < 0 && r < 0) {
			r = Constante.GRID_SIZE_Y + r;
		}
		if (offset > 0 && r >= Constante.GRID_SIZE_Y) {
			r = r - Constante.GRID_SIZE_Y;
		}
		return r;
	}

	public boolean isExploded() {
		return exploded;
	}
}
