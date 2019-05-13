package com.mygdx.domain.game;

import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.constante.CollisionConstante;
import com.mygdx.constante.Constante;
import com.mygdx.domain.common.BodyAble;
import com.mygdx.domain.enumeration.BonusStateEnum;
import com.mygdx.domain.enumeration.BonusTypeEnum;
import com.mygdx.domain.level.Level;
import com.mygdx.domain.level.LevelElement;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.SpriteService;

public class Bonus extends BodyAble implements LevelElement {

	protected int x;
	protected int y;
	private BonusTypeEnum type;
	private BonusStateEnum state;
	private float stateTime;
	private Animation<TextureRegion> animation;

	public Bonus(Level level, World world, MultiBombermanGame mbGame, int x, int y, BonusTypeEnum type,
			BonusStateEnum state) {
		this.world = world;
		this.mbGame = mbGame;
		this.level = level;
		this.x = x;
		this.y = y;
		this.type = type;
		this.state = state;
		this.stateTime = 0f;
		this.animation = new Animation<>((1f / (float) Constante.FPS) * 4f,
				SpriteService.getInstance().getSpriteForAnimation(SpriteEnum.BONUS_BURN));
		if (state == BonusStateEnum.REVEALED) {
			this.level.getOccupedWallBrickBonus()[x][y] = this;
			this.createBody();
		}
	}

	@Override
	public void drawIt() {
		if (this.state == BonusStateEnum.BURN) {
			this.stateTime += Gdx.graphics.getDeltaTime();
			mbGame.getBatch().draw(animation.getKeyFrame(stateTime, false), this.x * Constante.GRID_PIXELS_SIZE_X - 6,
					this.y * Constante.GRID_PIXELS_SIZE_Y);
		} else if (this.state == BonusStateEnum.REVEALED) {
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.BONUS, this.type.getIndex()),
					this.x * Constante.GRID_PIXELS_SIZE_X, this.y * Constante.GRID_PIXELS_SIZE_Y);
		}
	}

	@Override
	protected void createBody() {
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.type = BodyType.DynamicBody;
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(0.5f, 0.5f);
		groundBodyDef.position.set(new Vector2((float) this.x + 0.5f, (float) this.y + 0.5f));
		this.body = world.createBody(groundBodyDef);
		Fixture fixture = body.createFixture(groundBox, 0.0f);
		fixture.setFriction(0f);
		fixture.setUserData(this);
		Filter filter = new Filter();
		filter.categoryBits = CollisionConstante.CATEGORY_BONUS;
		filter.maskBits = CollisionConstante.CATEGORY_PLAYER_HITBOX;
		fixture.setFilterData(filter);
	}

	@Override
	public void action() {
		if (state == BonusStateEnum.REVEALED) {
			this.state = BonusStateEnum.BURN;
		}
	}

	@Override
	public void update() {
		if (this.state == BonusStateEnum.TAKED) {
			this.level.getOccupedWallBrickBonus()[this.x][this.y] = null;
			if (this.type == BonusTypeEnum.DEATH && !this.level.getNoWall().isEmpty()) {
				int idx = ThreadLocalRandom.current().nextInt(0, this.level.getNoWall().size());
				int chx = this.level.getNoWall().get(idx);
				int x = chx % Constante.GRID_SIZE_X;
				int y = (chx - x) / Constante.GRID_SIZE_X;
				this.x = x;
				this.y = y;
				this.body.setTransform((float) this.x + 0.5f, (float) this.y + 0.5f, 0f);
				this.level.getNoWall().remove(new Integer(idx));
				this.state = BonusStateEnum.REVEALED;
			} else {
				this.state = BonusStateEnum.DISPOSED;
				dispose();
			}
		} else if (this.state == BonusStateEnum.BURN && this.stateTime >= animation.getAnimationDuration()) {
			this.state = BonusStateEnum.BURNED;
			this.level.getOccupedWallBrickBonus()[this.x][this.y] = null;
			dispose();
		}
	}

	public void revealBonus() {
		this.state = BonusStateEnum.REVEALED;
		this.level.getOccupedWallBrickBonus()[x][y] = this;
		this.createBody();
	}

	public BonusTypeEnum playerTakeBonus() {
		if (this.state == BonusStateEnum.REVEALED) {
			this.state = BonusStateEnum.TAKED;
			return this.type;
		}
		return null;
	}

	public BonusTypeEnum getType() {
		return this.type;
	}

	public boolean isRevealed() {
		return this.state == BonusStateEnum.REVEALED;
	}

	public boolean isBurned() {
		return this.state == BonusStateEnum.BURNED;
	}

	public boolean isBurning() {
		return this.state == BonusStateEnum.BURN;
	}

	public boolean idDisposed() {
		return this.state == BonusStateEnum.DISPOSED;
	}

	@Override
	public void immediateAction() {
		this.state = BonusStateEnum.BURNED;
		this.level.getOccupedWallBrickBonus()[this.x][this.y] = null;
		dispose();
	}
	


	@Override
	public SpriteEnum getDrawSprite() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDrawIndex() {
		// TODO Auto-generated method stub
		return 0;
	}
}
