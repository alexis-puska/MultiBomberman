package com.mygdx.domain;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.constante.CollisionConstante;
import com.mygdx.domain.common.BodyAble;
import com.mygdx.domain.enumeration.FireEnum;
import com.mygdx.service.SpriteService;

public class Fire extends BodyAble {

	private static final int NB_FRAME = 4;

	protected int x;
	protected int y;
	private FireEnum fireEnum;
	private boolean off;

	private int frameCounter;
	private int offsetSprite;
	private int nbFrameForAnimation;
	private int offsetSpriteAnimation;

	public Fire(int x, int y, FireEnum fireEnum) {
		this.x = x;
		this.y = y;
		this.fireEnum = fireEnum;
		frameCounter = 0;
		offsetSprite = 0;
		nbFrameForAnimation = 7;
	}

	@Override
	public void createBody() {
		BodyDef groundBodyDef = new BodyDef();
		PolygonShape groundBox = new PolygonShape();
		switch (fireEnum) {
		case FIRE_DOWN_EXT:
			groundBox.setAsBox(0.4f, 0.45f);
			groundBodyDef.position.set(new Vector2((float) this.x + 0.5f, (float) this.y + 0.5f));
			break;
		case FIRE_LEFT:
		case FIRE_RIGHT:
			groundBox.setAsBox(0.5f, 0.4f);
			groundBodyDef.position.set(new Vector2((float) this.x + 0.5f, (float) this.y + 0.5f));
			break;
		case FIRE_LEFT_EXT:
			groundBox.setAsBox(0.45f, 0.4f);
			groundBodyDef.position.set(new Vector2((float) this.x + 0.55f, (float) this.y + 0.5f));
			break;
		case FIRE_RIGHT_EXT:
			groundBox.setAsBox(0.45f, 0.4f);
			groundBodyDef.position.set(new Vector2((float) this.x + 0.5f, (float) this.y + 0.45f));
			break;
		case FIRE_TOP:
		case FIRE_DOWN:
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
		mbGame.getBatch().draw(SpriteService.getInstance().getSprite(fireEnum.getSpriteEnum(), offsetSpriteAnimation),
				(float) this.x * 18, (float) this.y * 16);
	}

	public void update() {
		if (frameCounter > NB_FRAME) {
			frameCounter = 0;
			offsetSprite++;
			if (offsetSprite >= nbFrameForAnimation) {
				off = true;
				this.dispose();
			}
		}
		frameCounter++;
		offsetSpriteAnimation = 0;
		switch (offsetSprite) {
		case 0:
			offsetSpriteAnimation = 3;
			break;
		case 1:
			offsetSpriteAnimation = 2;
			break;
		case 2:
			offsetSpriteAnimation = 1;
			break;
		case 3:
			offsetSpriteAnimation = 0;
			break;
		case 4:
			offsetSpriteAnimation = 1;
			break;
		case 5:
			offsetSpriteAnimation = 2;
			break;
		case 6:
		default:
			offsetSpriteAnimation = 3;
			break;
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
