package com.mygdx.domain.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.constante.CollisionConstante;
import com.mygdx.constante.Constante;
import com.mygdx.domain.Player;
import com.mygdx.domain.common.BodyAble;
import com.mygdx.domain.enumeration.BombeTypeEnum;
import com.mygdx.domain.enumeration.FireEnum;
import com.mygdx.domain.level.Level;
import com.mygdx.domain.level.Wall;
import com.mygdx.enumeration.SoundEnum;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.SoundService;
import com.mygdx.service.SpriteService;
import com.mygdx.utils.GridUtils;

public class Bombe extends BodyAble {

	private static final float BORDER_MAX = 0.45f;
	private static final float BORDER_MIN = 0.3f;

	private static final int NB_FRAME = 4;

	protected int x;
	protected int y;
	private int strenght;
	private BombeTypeEnum type;
	private Player player;
	private int countDown;
	private boolean exploded;

	private PovDirection direction;
	private float walkSpeed;

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
		this.direction = PovDirection.center;
		this.walkSpeed = Constante.WALK_SPEED;
		createBody();
	}

	@Override
	protected void createBody() {
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.active = true;
		groundBodyDef.type = BodyType.DynamicBody;

		PolygonShape diamondBody = new PolygonShape();
		Vector2[] vertices = new Vector2[8];
		vertices[0] = new Vector2(BORDER_MIN, BORDER_MAX);
		vertices[1] = new Vector2(BORDER_MAX, BORDER_MIN);
		vertices[2] = new Vector2(BORDER_MAX, -BORDER_MIN);
		vertices[3] = new Vector2(BORDER_MIN, -BORDER_MAX);
		vertices[4] = new Vector2(-BORDER_MIN, -BORDER_MAX);
		vertices[5] = new Vector2(-BORDER_MAX, -BORDER_MIN);
		vertices[6] = new Vector2(-BORDER_MAX, BORDER_MIN);
		vertices[7] = new Vector2(-BORDER_MIN, BORDER_MAX);
		diamondBody.set(vertices);

		CircleShape groundBox = new CircleShape();
		groundBox.setRadius(0.5f);
		groundBodyDef.position.set(new Vector2((float) this.x + 0.5f, (float) this.y + 0.5f));
		body = world.createBody(groundBodyDef);
		body.setFixedRotation(false);
		MassData data = new MassData();
		data.mass = 100000f;
		body.setMassData(data);
		body.setUserData(this);
		Fixture fixture = body.createFixture(diamondBody, 0.0f);
		fixture.setFriction(0f);
		fixture.setUserData(this);
		Filter filter = new Filter();
		filter.categoryBits = CollisionConstante.CATEGORY_BOMBE;
		filter.maskBits = CollisionConstante.GROUP_BOMBE_HITBOX;
		fixture.setFilterData(filter);
		Gdx.app.log("BOMBE", "BODY CREATED");
	}

	@Override
	public void drawIt() {
		mbGame.getBatch().draw(SpriteService.getInstance().getSprite(type.getSpriteEnum(), offsetSpriteAnimation),
				(float) ((body.getPosition().x - 0.5f) * 18f), (float) ((body.getPosition().y - 0.5f) * 16f));
	}

	public int getX() {
		return (int) (body.getPosition().x * 18f);
	}

	public int getY() {
		return (int) (body.getPosition().y * 16f);
	}

	public BombeLight getOffesetShadow() {
		return this.type.getOffsetLight().get(offsetSpriteAnimation);
	}

	public boolean isCreateLight() {
		return this.type.isCreateLight();
	}

	public float getLight() {
		return this.type.getLight();
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
		default:
			break;
		}

		switch (direction) {
		case east:
			this.body.setLinearVelocity(walkSpeed, 0f);
			break;
		case north:
			this.body.setLinearVelocity(0f, walkSpeed);
			break;
		case south:
			this.body.setLinearVelocity(0f, -walkSpeed);
			break;
		case west:
			this.body.setLinearVelocity(-walkSpeed, 0f);
			break;
		case northEast:
		case northWest:
		case southEast:
		case southWest:
		case center:
		default:
			this.body.setLinearVelocity(0f, 0f);
			break;
		}

		if (this.body.getPosition().x > (float) Constante.GRID_SIZE_X) {
			this.body.setTransform(this.body.getPosition().x - (float) Constante.GRID_SIZE_X, this.body.getPosition().y,
					0f);
		}
		if (this.body.getPosition().x < 0f) {
			this.body.setTransform(this.body.getPosition().x + (float) Constante.GRID_SIZE_X, this.body.getPosition().y,
					0f);
		}
		if (this.body.getPosition().y > (float) Constante.GRID_SIZE_Y) {
			this.body.setTransform(this.body.getPosition().x, this.body.getPosition().y - (float) Constante.GRID_SIZE_Y,
					0f);
		}
		if (this.body.getPosition().y < 0f) {
			this.body.setTransform(this.body.getPosition().x, this.body.getPosition().y + (float) Constante.GRID_SIZE_Y,
					0f);
		}

		if (countDown == 0) {
			explode();
		}
	}

	public void inFire() {
		countDown = 1;
	}

	public void explode() {
		int posX = (int) body.getPosition().x;
		int posY = (int) body.getPosition().y;
		if (this.level.getOccupedWallBrick()[posX][posY] == null) {
			this.level.getFires().add(new Fire(this.world, this.mbGame, this.level, posX, posY, FireEnum.FIRE_CENTER));
		}
		generateFireRight(posX, posY);
		generateFireDown(posX, posY);
		generateFireLeft(posX, posY);
		generateFireUp(posX, posY);
		exploded = true;
		SoundService.getInstance().playSound(SoundEnum.FIRE);
		this.dispose();
		this.player.bombeExploded();
	}

	private void generateFireUp(int posX, int posY) {
		int calcX;
		int calcY;
		for (int i = 1; i <= this.strenght; i++) {
			calcX = posX;
			calcY = GridUtils.calcIdxY(posY, i);
			if (generateFire(calcX, calcY, i == this.strenght ? FireEnum.FIRE_UP_EXT : FireEnum.FIRE_UP)) {
				break;
			}
		}
	}

	private void generateFireLeft(int posX, int posY) {
		int calcX;
		int calcY;
		for (int i = 1; i <= this.strenght; i++) {
			calcX = GridUtils.calcIdxX(posX, -i);
			calcY = posY;
			if (generateFire(calcX, calcY, i == this.strenght ? FireEnum.FIRE_LEFT_EXT : FireEnum.FIRE_LEFT)) {
				break;
			}
		}
	}

	private void generateFireDown(int posX, int posY) {
		int calcX;
		int calcY;
		for (int i = 1; i <= this.strenght; i++) {
			calcX = posX;
			calcY = GridUtils.calcIdxY(posY, -i);
			if (generateFire(calcX, calcY, i == this.strenght ? FireEnum.FIRE_DOWN_EXT : FireEnum.FIRE_DOWN)) {
				break;
			}
		}
	}

	private void generateFireRight(int posX, int posY) {
		int calcX;
		int calcY;
		for (int i = 1; i <= this.strenght; i++) {
			calcX = GridUtils.calcIdxX(posX, i);
			calcY = posY;
			if (generateFire(calcX, calcY, i == this.strenght ? FireEnum.FIRE_RIGHT_EXT : FireEnum.FIRE_RIGHT)) {
				break;
			}
		}
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

	public boolean isExploded() {
		return exploded;
	}

	/*****************************************
	 * --- PLAYER HURT BOMBE ---
	 *****************************************/
	public void kick(PovDirection direction) {
		this.direction = direction;
	}

	/*****************************************
	 * --- PLAYER TRIGGER BOMBE P ---
	 *****************************************/
	public boolean bombeOfPlayer(Player player) {
		return this.player.equals(player);
	}

	public BombeTypeEnum getType() {
		return this.type;
	}

	/*****************************************
	 * --- HURT WALL ---
	 *****************************************/
	public void hurtWallOrBrick(float x, float y) {
		Gdx.app.log("BOMBE", "hurt wall");
		float diffX = Math.abs(this.body.getPosition().x - x);
		float diffY = Math.abs(this.body.getPosition().y - y);
		if (direction != PovDirection.center) {
			if (diffX > 0.8f && diffY < 0.2f) {
				hurtSomethingOnEastOrWest();
			} else if (diffY > 0.8f && diffX < 0.2f) {
				hurtSomethingOnNorthOrSouth();
			}
		}
	}

	private void hurtSomethingOnNorthOrSouth() {
		if (type == BombeTypeEnum.BOMBE_RUBBER) {
			if (direction == PovDirection.north) {
				SoundService.getInstance().playSound(SoundEnum.BOUNCE);
				direction = PovDirection.south;
			} else if (direction == PovDirection.south) {
				SoundService.getInstance().playSound(SoundEnum.BOUNCE);
				direction = PovDirection.north;
			}
		} else if (direction == PovDirection.north || direction == PovDirection.south) {
			direction = PovDirection.center;
		}
	}

	private void hurtSomethingOnEastOrWest() {
		if (type == BombeTypeEnum.BOMBE_RUBBER) {
			if (direction == PovDirection.east) {
				SoundService.getInstance().playSound(SoundEnum.BOUNCE);
				direction = PovDirection.west;
			} else if (direction == PovDirection.west) {
				SoundService.getInstance().playSound(SoundEnum.BOUNCE);
				direction = PovDirection.east;
			}
		} else if (direction == PovDirection.east || direction == PovDirection.west) {
			direction = PovDirection.center;
		}
	}

	public void hurtBombe() {
		Gdx.app.log("BOMBE", "hurt bombe");
		hurtSomethingOnNorthOrSouth();
		hurtSomethingOnEastOrWest();
	}
}
