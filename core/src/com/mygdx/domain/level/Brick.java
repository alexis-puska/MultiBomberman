package com.mygdx.domain.level;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.constante.CollisionConstante;
import com.mygdx.domain.common.BodyAble;
import com.mygdx.domain.enumeration.BrickStateEnum;
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
public class Brick extends BodyAble {

	protected int x;
	protected int y;
	private SpriteEnum animation;
	
	private Level level;
	private BrickStateEnum state;
	private int countdown;
	private int indexAnimation;

	private SpriteEnum defaultAnimation;
	private int defaultTexture;

	public void init(World world, MultiBombermanGame mbGame, SpriteEnum animation, int i, int j) {
		this.x = i;
		this.y = j;
		this.world = world;
		this.animation = animation;
		this.init(mbGame);
		this.state = BrickStateEnum.CREATED;
		createBody();
	}

	@Override
	public void drawIt() {
		if (this.state == BrickStateEnum.BURN || this.state == BrickStateEnum.CREATED) {
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(this.animation, this.indexAnimation),
					this.x * 18, this.y * 16);
		}
	}

	@Override
	public void createBody() {
		BodyDef groundBodyDef = new BodyDef();
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(0.5f, 0.5f);
		groundBodyDef.position.set(new Vector2((float) this.x + 0.5f, (float) this.y + 0.5f));
		Body body = world.createBody(groundBodyDef);
		Fixture fixture = body.createFixture(groundBox, 0.0f);
		fixture.setFriction(0f);
		Filter filter = new Filter();
		filter.categoryBits = CollisionConstante.CATEGORY_BRICKS;
		fixture.setFilterData(filter);
	}

	public void burn() {
		this.state = BrickStateEnum.BURN;
		this.countdown = 10;
		this.indexAnimation++;
	}

	public void update() {
		if (this.state == BrickStateEnum.BURN) {
			this.countdown--;
			if (this.countdown < 0) {
				this.countdown = 10;
				indexAnimation++;
				if (this.indexAnimation >= SpriteService.getInstance().getAnimationSize(this.animation)) {
					this.state = BrickStateEnum.BURNED;
					dispose();
				}
			}
		}
	}
}
