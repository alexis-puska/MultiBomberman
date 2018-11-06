package com.mygdx.domain.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.constante.CollisionConstante;
import com.mygdx.constante.Constante;
import com.mygdx.domain.common.BodyAble;
import com.mygdx.domain.enumeration.BrickStateEnum;
import com.mygdx.domain.level.Level;
import com.mygdx.domain.level.LevelElement;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.SpriteService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Brick extends BodyAble implements LevelElement {

	protected int x;
	protected int y;
	private SpriteEnum animation;

	private BrickStateEnum state;
	private int countdown;
	private int indexAnimation;

	private SpriteEnum defaultAnimation;
	private int defaultTexture;

	public Brick(final World world, final MultiBombermanGame mbGame, final Level level, SpriteEnum animation, int i,
			int j) {
		this.x = i;
		this.y = j;
		this.mbGame = mbGame;
		this.world = world;
		this.level = level;
		this.animation = animation;
		this.state = BrickStateEnum.CREATED;
		createBody();
	}

	@Override
	public void drawIt() {
		if (this.state == BrickStateEnum.BURN || this.state == BrickStateEnum.CREATED) {
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(this.animation, this.indexAnimation),
					this.x * 18f, this.y * 16f);
		}
	}

	@Override
	protected void createBody() {
		BodyDef groundBodyDef = new BodyDef();
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(0.5f, 0.5f);
		groundBodyDef.position.set(new Vector2((float) this.x + 0.5f, (float) this.y + 0.5f));
		this.body = world.createBody(groundBodyDef);
		Fixture fixture = body.createFixture(groundBox, 0.0f);
		fixture.setFriction(0f);
		fixture.setUserData(this);
		Filter filter = new Filter();
		filter.categoryBits = CollisionConstante.CATEGORY_BRICKS;
		filter.maskBits = CollisionConstante.CATEGORY_PLAYER;
		fixture.setFilterData(filter);
	}

	@Override
	public void action() {
		this.state = BrickStateEnum.BURN;
		this.countdown = Constante.BURN_BRICK_COUNTDOWN;
		this.indexAnimation++;
	}

	@Override
	public void update() {
		if (this.state == BrickStateEnum.BURN) {
			this.countdown--;
			if (this.countdown < 0) {
				this.countdown = Constante.BURN_BRICK_COUNTDOWN;
				indexAnimation++;
				if (this.indexAnimation >= SpriteService.getInstance().getAnimationSize(this.animation) - 1) {
					this.state = BrickStateEnum.BURNED;
					this.level.getOccupedWallBrick()[this.getX()][this.getY()] = null;
					dispose();
				}
			}
		}
	}
}
