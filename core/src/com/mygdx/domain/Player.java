package com.mygdx.domain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.constante.CollisionConstante;
import com.mygdx.domain.common.BodyAble;
import com.mygdx.enumeration.CharacterColorEnum;
import com.mygdx.enumeration.CharacterEnum;
import com.mygdx.enumeration.CharacterSpriteEnum;
import com.mygdx.game.MultiBombermanGame;
import com.mygdx.service.SpriteService;
import com.mygdx.service.input_processor.ControlEventListener;

public class Player extends BodyAble implements ControlEventListener {

	private static final String CLASS_NAME = "Player.class";

	private static final float WALK_SPEED = 8f;

	private final CharacterEnum character;
	private final CharacterColorEnum color;
	private PovDirection direction;

	public Player(World world, MultiBombermanGame game, CharacterEnum character, CharacterColorEnum color) {
		init(world, game);
		this.character = character;
		this.color = color;
		this.direction = PovDirection.center;
	}

	@Override
	public void createBody() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(1, 1);
		body = world.createBody(bodyDef);
		body.setFixedRotation(false);
		MassData data = new MassData();
		data.mass = 100f;
		body.setMassData(data);
		body.setUserData(this);
		body.getPosition().x = 1f;
		body.getPosition().y = 1f;
		CircleShape bodyCircle = new CircleShape();
		bodyCircle.setRadius(0.4f);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = bodyCircle;
		fixtureDef.density = 0;
		fixtureDef.restitution = 0f;
		Fixture fixture = body.createFixture(fixtureDef);
		fixture.setFriction(0f);
		Filter filter = new Filter();
		filter.categoryBits = CollisionConstante.CATEGORY_PLAYER;
		filter.maskBits = CollisionConstante.GROUP_PLAYER;
		fixture.setFilterData(filter);
		bodyCircle.dispose();
	}

	@Override
	public void drawIt() {
		// unused method

		switch (this.direction) {
		case center:
			this.body.setLinearVelocity(0f, 0f);
			break;
		case east:
			this.body.setLinearVelocity(WALK_SPEED, 0f);
			break;
		case north:
			this.body.setLinearVelocity(0f, WALK_SPEED);
			break;
		case south:
			this.body.setLinearVelocity(0f, -WALK_SPEED);
			break;
		case west:
			this.body.setLinearVelocity(-WALK_SPEED, 0f);
			break;
		case southEast:
		case southWest:
		case northEast:
		case northWest:
		default:
			break;
		}

//		Gdx.app.log("PLAYER", "draw : " + body.getPosition().x + " , " + body.getPosition().y);
		game.getBatch().draw(SpriteService.getInstance().getSprite(CharacterSpriteEnum.WALK_DOWN, color, character, 0),
				(body.getPosition().x * 18f) - 15, body.getPosition().y * 16f);
	}

	@Override
	public void move(PovDirection value) {
		Gdx.app.debug(CLASS_NAME, "press move : " + value.toString());
		this.direction = value;
	}

	@Override
	public void pressStart() {
		// unused method

	}

	@Override
	public void pressSelect() {
		// unused method

	}

	@Override
	public void pressA() {
		// unused method

	}

	@Override
	public void pressB() {
		// unused method

	}

	@Override
	public void pressX() {
		// unused method

	}

	@Override
	public void pressY() {
		// unused method

	}

	@Override
	public void pressL() {
		// unused method

	}

	@Override
	public void pressR() {
		// unused method

	}

	@Override
	public void releaseL() {
		// unused method

	}

	@Override
	public void releaseR() {
		// unused method

	}

}
