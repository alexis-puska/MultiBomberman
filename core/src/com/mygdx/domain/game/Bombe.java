package com.mygdx.domain.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.constante.CollisionConstante;
import com.mygdx.constante.Constante;
import com.mygdx.domain.Player;
import com.mygdx.domain.common.BodyAble;
import com.mygdx.domain.enumeration.BombeStateEnum;
import com.mygdx.domain.enumeration.BombeTypeEnum;
import com.mygdx.domain.enumeration.FireEnum;
import com.mygdx.domain.level.Level;
import com.mygdx.domain.level.Wall;
import com.mygdx.enumeration.SoundEnum;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.SoundService;
import com.mygdx.service.SpriteService;
import com.mygdx.utils.GridUtils;
import com.mygdx.utils.ReboundUtils;

public class Bombe extends BodyAble {
	private static final float BORDER_MAX = 0.45f;
	private static final float BORDER_MIN = 0.3f;
	private BombeStateEnum state;
	private float animationTime;
	private float reboundTime;
	private float offsetZ;
	private Animation<TextureRegion> animation;
	private int strenght;
	private BombeTypeEnum type;
	private Player player;
	private float timeToExplode;
	private PovDirection direction;
	private float walkSpeed;
	protected int x;
	protected int y;
	private float count;
	private Player lastPlayerTouched;

	public Bombe(Level level, World world, MultiBombermanGame mbGame, int strenght, int x, int y, BombeTypeEnum type,
			Player player, float timeToExplode) {
		this.world = world;
		this.mbGame = mbGame;
		this.level = level;
		this.strenght = strenght;
		this.x = x;
		this.y = y;
		this.type = type;
		this.player = player;
		this.timeToExplode = timeToExplode;
		this.direction = PovDirection.center;
		this.walkSpeed = Constante.WALK_SPEED;
		this.animationTime = 0f;
		this.count = 0f;
		this.state = BombeStateEnum.CREATED;
		this.reboundTime = 0f;
		this.animation = new Animation<>(0.16f,
				SpriteService.getInstance().getSpriteForAnimation(type.getSpriteEnum()));
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
		groundBodyDef.position.set(new Vector2((float) this.x + 0.5f, (float) this.y + 0.5f));
		body = world.createBody(groundBodyDef);
		body.setFixedRotation(false);
		MassData data = new MassData();
		data.mass = 10000f;
		body.setMassData(data);
		body.setUserData(this);
		Fixture fixture = body.createFixture(diamondBody, 0.0f);
		fixture.setFriction(0f);
		fixture.setUserData(this);
		Filter filter = new Filter();
		filter.categoryBits = CollisionConstante.CATEGORY_BOMBE;
		filter.maskBits = CollisionConstante.GROUP_BOMBE;
		fixture.setFilterData(filter);
	}

	@Override
	public void drawIt() {
		mbGame.getBatch().draw(animation.getKeyFrame(animationTime, true),
				(float) ((body.getPosition().x - 0.5f) * Constante.GRID_PIXELS_SIZE_X),
				(float) ((body.getPosition().y - 0.5f) * Constante.GRID_PIXELS_SIZE_Y) + offsetZ);
	}

	public BombeLight getOffesetShadow() {
		return this.type.getOffsetLight()
				.get(Integer.valueOf(Double.valueOf(Math.floor(
						(animationTime % this.animation.getAnimationDuration()) / this.animation.getFrameDuration()))
						.intValue()));
	}

	public float getReboundOffset() {
		return this.offsetZ;
	}

	public boolean isCreateLight() {
		return this.type.isCreateLight();
	}

	public float getLight() {
		return this.type.getLight();
	}

	@Override
	public void update() {
		switch (type) {
		case BOMBE:
		case BOMBE_MAX:
		case BOMBE_RUBBER:
			if (state != BombeStateEnum.FLY) {
				this.count += Gdx.graphics.getDeltaTime();
			}
			break;
		case BOMBE_P:
		default:
			break;
		}

		this.animationTime += Gdx.graphics.getDeltaTime();
		if (this.state == BombeStateEnum.FLY) {
			this.reboundTime += Gdx.graphics.getDeltaTime();
			this.offsetZ = ReboundUtils.calcReboundOffset(this.reboundTime * (float) Constante.FPS);
		} else if (state == BombeStateEnum.CARRIED) {
			this.offsetZ = 20f;
		} else {
			offsetZ = 0f;
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
		if (state == BombeStateEnum.FLY && reboundTime >= 0.5f
				&& this.level.getOccupedWallBrickBonus()[(int) this.getBodyX()][(int) this.getBodyY()] == null && lastPlayerTouched == null) {
			this.state = BombeStateEnum.CREATED;
			this.direction = PovDirection.center;
			this.body.setLinearVelocity(0f, 0f);
			this.body.setTransform((float) Math.floor(this.getBodyX()) + 0.5f,
					(float) Math.floor(this.getBodyY()) + 0.5f, 0f);
			for (int i = 0; i < this.body.getFixtureList().size; i++) {
				Filter f = new Filter();
				f.categoryBits = CollisionConstante.CATEGORY_BOMBE;
				f.maskBits = CollisionConstante.GROUP_BOMBE;
				this.body.getFixtureList().get(i).setFilterData(f);
				Gdx.app.log("BOMBE", "filter set with reactivate collision " + reboundTime);
			}
		}

		if (state == BombeStateEnum.EXPLODE || this.count > this.timeToExplode) {
			explode();
		}
	}

	public void inFire() {
		this.state = BombeStateEnum.EXPLODE;
	}

	public void explode() {
		int posX = (int) body.getPosition().x;
		int posY = (int) body.getPosition().y;
		if (this.level.getOccupedWallBrickBonus()[posX][posY] == null) {
			this.level.getFires().add(new Fire(this.world, this.mbGame, this.level, posX, posY, FireEnum.FIRE_CENTER));
		}
		generateFireRight(posX, posY);
		generateFireDown(posX, posY);
		generateFireLeft(posX, posY);
		generateFireUp(posX, posY);
		this.state = BombeStateEnum.EXPLODED;
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
		if (this.level.getOccupedWallBrickBonus()[calcX][calcY] != null) {
			this.level.getOccupedWallBrickBonus()[calcX][calcY].action();
			if (this.level.getOccupedWallBrickBonus()[calcX][calcY].getClass().equals(Wall.class)) {
				return true;
			}
			if (this.type != BombeTypeEnum.BOMBE_MAX) {
				return !this.level.getOccupedWallBrickBonus()[calcX][calcY].getClass().equals(Bonus.class);
			}
		} else {
			this.level.getFires().add(new Fire(this.world, this.mbGame, this.level, calcX, calcY, fireEnum));
		}
		return false;
	}

	public boolean isExploded() {
		return this.state == BombeStateEnum.EXPLODED;
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
		hurtSomethingOnNorthOrSouth();
		hurtSomethingOnEastOrWest();
	}

	public void hurtTrolleyInMove() {
		direction = PovDirection.center;
		this.state = BombeStateEnum.EXPLODE;
	}

	public void gotCarried() {
		this.state = BombeStateEnum.CARRIED;
		this.reboundTime = 0f;
		this.offsetZ = 20f;
	}

	public void playerCarryThatBombe(float x, float y) {
		this.body.setTransform(x, y, 0f);
	}

	public void iBelieveICanFly(PovDirection direction) {
		Gdx.app.log("BOMBE", "i believe i can fly");
		if (this.state == BombeStateEnum.CARRIED) {
			this.reboundTime = 0f;
			this.offsetZ = 0f;
			this.state = BombeStateEnum.FLY;
			this.direction = direction;
			for (int i = 0; i < this.body.getFixtureList().size; i++) {
				Filter f = new Filter();
				f.categoryBits = CollisionConstante.CATEGORY_BOMBE;
				f.maskBits = CollisionConstante.GROUP_BOMBE - CollisionConstante.CATEGORY_WALL
						- CollisionConstante.CATEGORY_BRICKS - CollisionConstante.CATEGORY_BOMBE
						- CollisionConstante.CATEGORY_PLAYER;
				this.body.getFixtureList().get(i).setFilterData(f);
				Gdx.app.log("BOMBE", "filter set with deactivate collision");
			}
		}
	}

	public boolean isFly() {
		return this.state == BombeStateEnum.FLY;
	}

	public void touchPlayer(Player p) {
		Gdx.app.log("Bombe", "touch player : " + p.toString());
		lastPlayerTouched = p;
	}

	public void untouchPlayer(Player p) {
		if (lastPlayerTouched != null && lastPlayerTouched.equals(p)) {
			lastPlayerTouched = null;
		}
		Gdx.app.log("Bombe", "untouch player : " + p.toString());
	}

}
