package com.mygdx.domain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.constante.CollisionConstante;
import com.mygdx.domain.common.BodyAble;
import com.mygdx.domain.level.StartPlayer;
import com.mygdx.enumeration.CharacterColorEnum;
import com.mygdx.enumeration.CharacterEnum;
import com.mygdx.enumeration.CharacterSpriteEnum;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.SpriteService;
import com.mygdx.service.input_processor.ControlEventListener;

public class Player extends BodyAble implements ControlEventListener, Comparable<Player> {

	private static final String CLASS_NAME = "Player.class";

	private static final float WALK_SPEED = 6f;
	private static final float RADIUS = 0.47f;

	private final CharacterEnum character;
	private final CharacterColorEnum color;
	private PovDirection direction;
	private Body collisionBody;

	private StartPlayer startPlayer;

	public Player(World world, MultiBombermanGame mbGame, CharacterEnum character, CharacterColorEnum color,
			StartPlayer startPlayer) {
		this.startPlayer = startPlayer;
		this.character = character;
		this.color = color;
		this.direction = PovDirection.center;
		init(world, mbGame);
	}

	@Override
	public void createBody() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(this.startPlayer.getX() + 0.5f, this.startPlayer.getY() + 0.5f);
		body = world.createBody(bodyDef);
		body.setFixedRotation(false);
		MassData data = new MassData();
		data.mass = 100f;
		body.setMassData(data);
		body.setUserData(this);
		PolygonShape diamondBody = new PolygonShape();
		Vector2[] vertices = new Vector2[4];
		vertices[0] = new Vector2(-RADIUS, 0);
		vertices[1] = new Vector2(0, RADIUS);
		vertices[2] = new Vector2(RADIUS, 0);
		vertices[3] = new Vector2(0, -RADIUS);
		diamondBody.set(vertices);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = diamondBody;
		fixtureDef.density = 0;
		fixtureDef.restitution = 0f;
		Fixture fixture = body.createFixture(fixtureDef);
		fixture.setFriction(0f);
		fixture.setUserData(this);
		Filter filter = new Filter();
		filter.categoryBits = CollisionConstante.CATEGORY_PLAYER;
		filter.maskBits = CollisionConstante.GROUP_PLAYER_MOVE;
		fixture.setFilterData(filter);
		diamondBody.dispose();

		collisionBody = world.createBody(bodyDef);
		collisionBody.setFixedRotation(false);
		collisionBody.setUserData(this);
		CircleShape groundBox = new CircleShape();
		groundBox.setRadius(0.02f);
		Fixture fixtureColision = collisionBody.createFixture(groundBox, 0.0f);
		fixtureColision.setFriction(0f);
		fixtureColision.setUserData(this);
		Filter filterColision = new Filter();
		filterColision.categoryBits = CollisionConstante.CATEGORY_PLAYER_HITBOX;
		filterColision.maskBits = CollisionConstante.GROUP_PLAYER_HITBOX;
		fixtureColision.setFilterData(filterColision);
	}

	@Override
	public void dispose() {
		if (body != null) {
			this.world.destroyBody(body);
			body = null;
		}
	}

	@Override
	public void drawIt() {
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
		collisionBody.setTransform(this.body.getPosition(), body.getAngle());
		mbGame.getBatch().draw(
				SpriteService.getInstance().getSprite(CharacterSpriteEnum.WALK_DOWN, color, character, 0),
				(body.getPosition().x * 18f) - 15, (body.getPosition().y * 16f) - 5f);
	}

	public int getX() {
		return (int) (body.getPosition().x * 18f);
	}

	public int getY() {
		return (int) (body.getPosition().y * 16f);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((character == null) ? 0 : character.hashCode());
		result = prime * result + ((collisionBody == null) ? 0 : collisionBody.hashCode());
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + ((startPlayer == null) ? 0 : startPlayer.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (character != other.character)
			return false;
		if (collisionBody == null) {
			if (other.collisionBody != null)
				return false;
		} else if (!collisionBody.equals(other.collisionBody))
			return false;
		if (color != other.color)
			return false;
		if (direction != other.direction)
			return false;
		if (startPlayer == null) {
			if (other.startPlayer != null)
				return false;
		} else if (!startPlayer.equals(other.startPlayer))
			return false;
		return true;
	}

	@Override
	public int compareTo(Player o) {
		if (this.body.getPosition().y < o.body.getPosition().y) {
			return 1;
		} else if (this.body.getPosition().y > o.body.getPosition().y) {
			return -1;
		}
		return 0;
	}

}
