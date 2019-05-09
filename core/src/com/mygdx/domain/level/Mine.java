package com.mygdx.domain.level;

import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.constante.CollisionConstante;
import com.mygdx.constante.Constante;
import com.mygdx.domain.common.BodyAble;
import com.mygdx.domain.enumeration.FireEnum;
import com.mygdx.domain.enumeration.MineTypeEnum;
import com.mygdx.domain.game.Fire;
import com.mygdx.enumeration.SoundEnum;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.SoundService;
import com.mygdx.service.SpriteService;
import com.mygdx.utils.GridUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mine extends BodyAble implements Initiable {

	private static final float EXPIRATIONT_TIME = 6f;
	private static final float FRAME_DURATION = 1f / 8f;
	private static final int FIRE_LENGTH = Constante.GRID_SIZE_X;

	protected int x;
	protected int y;
	private long id;
	private MineTypeEnum type;
	private float time;
	private float expiration;
	private boolean activate;
	private Animation<TextureRegion> straightAnimation;
	private Animation<TextureRegion> bendAnimation;

	@Override
	public void createBody() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set((float) this.x + 0.5f, (float) this.y + 0.5f);
		this.body = world.createBody(bodyDef);
		this.body.setFixedRotation(false);
		MassData data = new MassData();
		data.mass = 1000000f;
		this.body.setMassData(data);
		this.body.setUserData(this);
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(0.4f, 0.4f);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = groundBox;
		fixtureDef.density = 0;
		fixtureDef.restitution = 0f;
		Fixture fixture = body.createFixture(fixtureDef);
		fixture.setFriction(0f);
		fixture.setUserData(this);
		Filter filter = new Filter();
		filter.categoryBits = CollisionConstante.CATEGORY_MINE;
		filter.maskBits = CollisionConstante.GROUP_MINE;
		fixture.setFilterData(filter);
		groundBox.dispose();
	}

	@Override
	public void dispose() {
		if (this.id != 0l) {
			SoundService.getInstance().stopSound(SoundEnum.MINE, id);
		}
		super.dispose();
	}

	@Override
	public void init(World world, MultiBombermanGame mbGame, Level level) {
		this.mbGame = mbGame;
		this.world = world;
		this.level = level;
		this.straightAnimation = new Animation<>(FRAME_DURATION,
				SpriteService.getInstance().getSpriteForAnimation(SpriteEnum.MINE_D));
		this.bendAnimation = new Animation<>(FRAME_DURATION,
				SpriteService.getInstance().getSpriteForAnimation(SpriteEnum.MINE_C));
		this.time = 0f;
		this.type = MineTypeEnum.random();
		this.expiration = 0f;
		this.activate = false;
		createBody();
	}

	@Override
	public void update() {
		if (this.activate) {
			this.time += Gdx.graphics.getDeltaTime();
			if (time >= expiration) {
				generateFire();
				SoundService.getInstance().playSound(SoundEnum.FIRE);
				SoundService.getInstance().stopSound(SoundEnum.MINE, id);
				this.activate = false;
				this.time = 0f;
				this.expiration = 0f;
			}
		}
	}

	private void generateFire() {
		if (type == MineTypeEnum.BEND) {
			generateBendFire();
		} else if (type == MineTypeEnum.STRAIGHT) {
			generateStraightFire();
		}
	}

	private void generateStraightFire() {
		if ((time - EXPIRATIONT_TIME) <= FRAME_DURATION * 2) {
			generateFireDown(x, y);
			generateFireUp(x, y);
		} else {
			generateFireRight(x, y);
			generateFireLeft(x, y);
		}
	}

	private void generateBendFire() {
		if ((time - EXPIRATIONT_TIME) <= FRAME_DURATION * 2) {
			generateFireRight(x, y);
			generateFireUp(x, y);
		} else if ((time - EXPIRATIONT_TIME) <= FRAME_DURATION * 4) {
			generateFireRight(x, y);
			generateFireDown(x, y);
		} else if ((time - EXPIRATIONT_TIME) <= FRAME_DURATION * 6) {
			generateFireDown(x, y);
			generateFireLeft(x, y);
		} else {
			generateFireLeft(x, y);
			generateFireUp(x, y);
		}
	}

	public void activate() {
		if (!activate && this.level.getOccupedWallBrickBonus()[x][y] == null) {
			this.type = MineTypeEnum.random();
			int offset = ThreadLocalRandom.current().nextInt(0, (int) (FRAME_DURATION * 1000));
			this.time = ((float) offset / 100f);
			this.expiration = EXPIRATIONT_TIME + this.time;
			this.activate = true;
			this.id = SoundService.getInstance().loopSound(SoundEnum.MINE);
		}
	}

	@Override
	public void drawIt() {
		if (!activate) {
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.MINE, 0),
					(float) this.x * Constante.GRID_PIXELS_SIZE_X, (float) this.y * Constante.GRID_PIXELS_SIZE_Y);
		} else {
			switch (type) {
			case BEND:
				mbGame.getBatch().draw(bendAnimation.getKeyFrame(this.time, true),
						(float) this.x * Constante.GRID_PIXELS_SIZE_X, (float) this.y * Constante.GRID_PIXELS_SIZE_Y);
				break;
			case STRAIGHT:
			default:
				mbGame.getBatch().draw(straightAnimation.getKeyFrame(this.time, true),
						(float) this.x * Constante.GRID_PIXELS_SIZE_X, (float) this.y * Constante.GRID_PIXELS_SIZE_Y);
				break;
			}
		}
	}

	private void generateFireUp(int posX, int posY) {
		int calcX;
		int calcY;
		for (int i = 1; i <= FIRE_LENGTH; i++) {
			calcX = posX;
			calcY = GridUtils.calcIdxY(posY, i);
			if (generateFire(calcX, calcY, i == 1 ? FireEnum.FIRE_DOWN_EXT : FireEnum.FIRE_UP)) {
				break;
			}
		}
	}

	private void generateFireLeft(int posX, int posY) {
		int calcX;
		int calcY;
		for (int i = 1; i <= FIRE_LENGTH; i++) {
			calcX = GridUtils.calcIdxX(posX, -i);
			calcY = posY;
			if (generateFire(calcX, calcY, i == 1 ? FireEnum.FIRE_RIGHT_EXT : FireEnum.FIRE_LEFT)) {
				break;
			}
		}
	}

	private void generateFireDown(int posX, int posY) {
		int calcX;
		int calcY;
		for (int i = 1; i <= FIRE_LENGTH; i++) {
			calcX = posX;
			calcY = GridUtils.calcIdxY(posY, -i);
			if (generateFire(calcX, calcY, i == 1 ? FireEnum.FIRE_UP_EXT : FireEnum.FIRE_DOWN)) {
				break;
			}
		}
	}

	private void generateFireRight(int posX, int posY) {
		int calcX;
		int calcY;
		for (int i = 1; i <= FIRE_LENGTH; i++) {
			calcX = GridUtils.calcIdxX(posX, i);
			calcY = posY;
			if (generateFire(calcX, calcY, i == 1 ? FireEnum.FIRE_LEFT_EXT : FireEnum.FIRE_RIGHT)) {
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
		} else {
			this.level.getFires().add(new Fire(this.world, this.mbGame, this.level, null, calcX, calcY, fireEnum));
		}
		return false;
	}
}
