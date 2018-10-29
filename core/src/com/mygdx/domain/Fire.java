package com.mygdx.domain;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.constante.CollisionConstante;
import com.mygdx.domain.common.BodyAble;
import com.mygdx.domain.enumeration.FireEnum;
import com.mygdx.domain.level.Level;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.SpriteService;

public class Fire extends BodyAble {

	protected int x;
	protected int y;
	private FireEnum fireEnum;
	private boolean off;

	public Fire(int x, int y, FireEnum fireEnum) {
		this.x = x;
		this.y = y;
		this.fireEnum = fireEnum;
	}

	public void init(World world, MultiBombermanGame mbGame, Level level) {
		this.world = world;
		this.mbGame = mbGame;
		this.level = level;
		this.createBody();
	}

	@Override
	public void createBody() {
		BodyDef groundBodyDef = new BodyDef();
		PolygonShape groundBox = new PolygonShape();
		switch (fireEnum) {
		case FIRE_DOWN:
			groundBox.setAsBox(0.4f, 0.5f);
			groundBodyDef.position.set(new Vector2((float) this.x + 0.5f, (float) this.y + 0.55f));
			break;
		case FIRE_DOWN_EXT:
			groundBox.setAsBox(0.4f, 0.45f);
			groundBodyDef.position.set(new Vector2((float) this.x + 0.5f, (float) this.y + 0.5f));
			break;
		case FIRE_LEFT:
			groundBox.setAsBox(0.5f, 0.4f);
			groundBodyDef.position.set(new Vector2((float) this.x + 0.5f, (float) this.y + 0.5f));
			break;
		case FIRE_LEFT_EXT:
			groundBox.setAsBox(0.45f, 0.4f);
			groundBodyDef.position.set(new Vector2((float) this.x + 0.55f, (float) this.y + 0.5f));
			break;
		case FIRE_RIGHT:
			groundBox.setAsBox(0.5f, 0.4f);
			groundBodyDef.position.set(new Vector2((float) this.x + 0.5f, (float) this.y + 0.5f));
			break;
		case FIRE_RIGHT_EXT:
			groundBox.setAsBox(0.45f, 0.4f);
			groundBodyDef.position.set(new Vector2((float) this.x + 0.5f, (float) this.y + 0.45f));
			break;
		case FIRE_TOP:
			groundBox.setAsBox(0.4f, 0.5f);
			groundBodyDef.position.set(new Vector2((float) this.x + 0.5f, (float) this.y + 0.5f));
			break;
		case FIRE_TOP_EXT:
			groundBox.setAsBox(0.4f, 0.45f);
			groundBodyDef.position.set(new Vector2((float) this.x + 0.5f, (float) this.y + 0.45f));
			break;
		case FIRE_CENTER:
		default:
			groundBox.setAsBox(0.5f, 0.5f);
			groundBodyDef.position.set(new Vector2((float) this.x + 0.5f, (float) this.y + 0.5f));
			break;
		}
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
		mbGame.getBatch().draw(SpriteService.getInstance().getSprite(fireEnum.getSpriteEnum(), 0), this.x * 18,
				this.y * 16);
	}

	public void update() {
		if (off) {
			this.dispose();
		}
	}

	public boolean isOff() {
		return off;
	}

	public int getX() {
		return (int) (body.getPosition().x * 18f);
	}

	public int getY() {
		return (int) (body.getPosition().y * 16f);
	}

}
