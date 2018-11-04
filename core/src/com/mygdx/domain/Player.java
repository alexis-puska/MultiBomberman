package com.mygdx.domain;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

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
import com.mygdx.domain.enumeration.PlayerStateEnum;
import com.mygdx.domain.level.Level;
import com.mygdx.domain.level.StartPlayer;
import com.mygdx.domain.level.Teleporter;
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
	private PovDirection previousDirection;
	private Body collisionBody;

	// state
	PlayerStateEnum state;

	// teleporte
	private Teleporter destinationTeleporter;
	private int teleportCountDown;

	// player start
	private StartPlayer startPlayer;

	public Player(World world, MultiBombermanGame mbGame, Level level, CharacterEnum character,
			CharacterColorEnum color, StartPlayer startPlayer) {
		this.startPlayer = startPlayer;
		this.character = character;
		this.color = color;
		this.direction = PovDirection.center;
		this.state = PlayerStateEnum.NORMAL;
		init(world, mbGame, level);
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
		CharacterSpriteEnum drawSprite = CharacterSpriteEnum.WALK_DOWN;
		switch (this.state) {
		case BURNING:
			break;
		case CARRY_BOMBE:
			break;
		case CRYING:
			break;
		case DEAD:
			break;
		case INSIDE_TROLLEY:
			break;
		case NORMAL:
			switch (this.direction) {
			case center:
				if (previousDirection == PovDirection.west) {
					drawSprite = CharacterSpriteEnum.WALK_LEFT;
				} else if (previousDirection == PovDirection.north) {
					drawSprite = CharacterSpriteEnum.WALK_UP;
				} else if (previousDirection == PovDirection.east) {
					drawSprite = CharacterSpriteEnum.WALK_RIGHT;
				} else if (previousDirection == PovDirection.south) {
					drawSprite = CharacterSpriteEnum.WALK_DOWN;
				}
				break;
			case east:
				drawSprite = CharacterSpriteEnum.WALK_RIGHT;
				break;
			case north:
				drawSprite = CharacterSpriteEnum.WALK_UP;
				break;
			case south:
				drawSprite = CharacterSpriteEnum.WALK_DOWN;
				break;
			case west:
				drawSprite = CharacterSpriteEnum.WALK_LEFT;
				break;
			case northEast:
			case northWest:
			case southEast:
			case southWest:
			default:
				break;

			}
			break;
		case ON_LOUIS:
			break;
		case TELEPORT:
			break;
		case THROW_BOMBE:
			break;
		case VICTORY:
			break;
		case VICTORY_ON_LOUIS:
			break;
		default:
			break;

		}

		mbGame.getBatch().draw(SpriteService.getInstance().getSprite(drawSprite, color, character, 0),
				(body.getPosition().x * 18f) - 15, (body.getPosition().y * 16f) - 5f);
	}

	public void teleporte(Teleporter tel) {
		if (destinationTeleporter == null) {
			List<Teleporter> destination = this.level.getTeleporter().stream()
					.filter(t -> ((t.getX() != tel.getX() || t.getY() != tel.getY())
							&& !this.level.getOccupedWallBrick()[t.getX()][t.getY()]))
					.collect(Collectors.toList());
			if (!destination.isEmpty()) {
				int idx = ThreadLocalRandom.current().nextInt(0, destination.size());
				destinationTeleporter = destination.get(idx);
				this.state = PlayerStateEnum.TELEPORT;
				this.teleportCountDown = 6;
				tel.animate(true);
				destinationTeleporter.animate(false);
			}
		}
	}

	public void teleporteEnd(Teleporter tel) {
		if (tel.equals(destinationTeleporter) && this.teleportCountDown == 0
				&& this.state != PlayerStateEnum.TELEPORT) {
			Gdx.app.log(CLASS_NAME, "teleporte end ok 2! ");
			destinationTeleporter = null;
		}
	}

	@Override
	public void update() {
		switch (this.state) {
		case BURNING:
			break;
		case CARRY_BOMBE:
			break;
		case CRYING:
			break;
		case DEAD:
			break;
		case INSIDE_TROLLEY:
			break;
		case NORMAL:
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
			break;
		case ON_LOUIS:
			break;
		case TELEPORT:
			if (destinationTeleporter != null && teleportCountDown == 0) {
				this.body.setTransform(destinationTeleporter.getX() + 0.5f, destinationTeleporter.getY() + 0.5f, 0f);
				this.state = PlayerStateEnum.NORMAL;
			} else {
				teleportCountDown--;
				this.body.setLinearVelocity(0f, 0f);
			}
			break;
		case THROW_BOMBE:
			break;
		case VICTORY:
			break;
		case VICTORY_ON_LOUIS:
			break;
		default:
			break;

		}
		collisionBody.setTransform(this.body.getPosition(), body.getAngle());
	}

	public int getX() {
		return (int) (body.getPosition().x * 18f);
	}

	public int getY() {
		return (int) (body.getPosition().y * 16f);
	}

	public void fireIn() {
		Gdx.app.log(CLASS_NAME, "fire in");
	}

	public void fireOut() {
		Gdx.app.log(CLASS_NAME, "fire out");
	}

	@Override
	public void move(PovDirection value) {
		Gdx.app.debug(CLASS_NAME, "press move : " + value.toString());
		if (value == PovDirection.center) {
			this.previousDirection = direction;
		}
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
		} else if (!collisionBody.equals(other.collisionBody)) {
			return false;
		}
		if (color != other.color)
			return false;
		if (direction != other.direction)
			return false;
		if (startPlayer == null) {
			if (other.startPlayer != null)
				return false;
		} else if (!startPlayer.equals(other.startPlayer)) {
			return false;
		}
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

	public void bombeExploded() {
		// bombe++
	}

}
