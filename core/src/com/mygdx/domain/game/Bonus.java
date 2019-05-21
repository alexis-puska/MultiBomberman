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
import com.mygdx.game.Game;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.SpriteService;

public class Bonus extends BodyAble implements LevelElement {

	protected int x;
	protected int y;
	private Game game;
	private BonusTypeEnum type;
	private BonusStateEnum state;
	private float stateTime;
	private Animation<TextureRegion> animation;

	public Bonus(Game game, Level level, World world, MultiBombermanGame mbGame, int x, int y, BonusTypeEnum type,
			BonusStateEnum state) {
		this.game = game;
		this.world = world;
		this.mbGame = mbGame;
		this.level = level;
		this.x = x;
		this.y = y;
		this.type = type;
		this.state = state;
		this.stateTime = 0f;
		this.animation = new Animation<>(SpriteEnum.BONUS_BURN.getFrameAnimationTime(),
				SpriteService.getInstance().getSpriteForAnimation(SpriteEnum.BONUS_BURN));
		this.drawSprite = SpriteEnum.BONUS;
		if (state == BonusStateEnum.REVEALED) {
			this.level.getOccupedWallBrickBonus()[x][y] = this;
			this.createBody();
		}
	}

	@Override
	public void drawIt() {
		if (this.state == BonusStateEnum.BURN) {
			this.stateTime += Gdx.graphics.getDeltaTime();
			drawSprite = SpriteEnum.BONUS_BURN;
			drawIndex = animation.getKeyFrameIndex(stateTime % animation.getAnimationDuration());
			mbGame.getBatch().draw(animation.getKeyFrame(stateTime, false), this.x * Constante.GRID_PIXELS_SIZE_X - 6,
					this.y * Constante.GRID_PIXELS_SIZE_Y);
		} else if (this.state == BonusStateEnum.REVEALED) {
			drawSprite = SpriteEnum.BONUS;
			drawIndex = this.type.ordinal();
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.BONUS, this.type.ordinal()),
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
			this.game.bonusBurn(this.getGridIndex());
			this.state = BonusStateEnum.BURN;
		}
	}

	@Override
	public void update() {
		if (this.state == BonusStateEnum.TAKED) {
			this.level.getOccupedWallBrickBonus()[this.x][this.y] = null;
			this.level.getNoWall().add(this.getGridIndex());
			if (this.type == BonusTypeEnum.DEATH && !this.level.getNoWall().isEmpty()) {
				int idx = ThreadLocalRandom.current().nextInt(0, this.level.getNoWall().size());
				int chx = this.level.getNoWall().get(idx);
				this.x = chx % Constante.GRID_SIZE_X;
				this.y = (chx - this.x) / Constante.GRID_SIZE_X;
				this.body.setTransform((float) this.x + 0.5f, (float) this.y + 0.5f, 0f);
				this.level.getNoWall().remove(Integer.valueOf(idx));
				this.state = BonusStateEnum.REVEALED;
			} else {
				this.state = BonusStateEnum.DISPOSED;
				dispose();
			}
		} else if (this.state == BonusStateEnum.BURN && this.stateTime >= animation.getAnimationDuration()) {
			this.state = BonusStateEnum.BURNED;
			this.level.getOccupedWallBrickBonus()[this.x][this.y] = null;
			this.level.getNoWall().add(this.getGridIndex());
			dispose();
		}
	}

	public void revealBonus() {
		this.state = BonusStateEnum.REVEALED;
		this.level.getOccupedWallBrickBonus()[x][y] = this;
		this.createBody();
		this.game.bonusAppeared(this.type, this.getGridIndex());
	}

	public BonusTypeEnum playerTakeBonus() {
		if (this.state == BonusStateEnum.REVEALED) {
			this.game.bonusTaked(this.getGridIndex());
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
		this.game.removeBonus(this.getGridIndex());
		this.state = BonusStateEnum.BURNED;
		this.level.getOccupedWallBrickBonus()[this.x][this.y] = null;
		this.level.getNoWall().add(this.getGridIndex());
		dispose();
	}

	@Override
	public SpriteEnum getDrawSprite() {
		return drawSprite;
	}

	@Override
	public int getDrawIndex() {
		return drawIndex;
	}
}
