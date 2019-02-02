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
import com.mygdx.constante.Constante;
import com.mygdx.domain.common.BodyAble;
import com.mygdx.domain.enumeration.BombeTypeEnum;
import com.mygdx.domain.enumeration.BonusTypeEnum;
import com.mygdx.domain.enumeration.PlayerStateEnum;
import com.mygdx.domain.game.Bombe;
import com.mygdx.domain.level.Level;
import com.mygdx.domain.level.StartPlayer;
import com.mygdx.domain.level.Teleporter;
import com.mygdx.enumeration.CharacterColorEnum;
import com.mygdx.enumeration.CharacterEnum;
import com.mygdx.enumeration.CharacterSpriteEnum;
import com.mygdx.enumeration.LouisColorEnum;
import com.mygdx.enumeration.LouisSpriteEnum;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.SpriteService;
import com.mygdx.service.input_processor.ControlEventListener;
import com.mygdx.utils.GridUtils;

public class Player extends BodyAble implements ControlEventListener, Comparable<Player> {

	private static final String CLASS_NAME = "Player.class";

	private static final float RADIUS = 0.48f;
	private static final float DEFAULT_SHIP_SPEED = 0.8f;
	private static final float SHIP_SPEED_STEP = 0.5f;

	private static final int NB_FRAME = 4;
	private static final int NB_FRAME_INC_ACTION = 5;
	private static final int NB_FRAME_UNDERWATER = 60;

	private static final int INVINCIBLE_TIME = 50;

	private final CharacterEnum character;
	private final CharacterColorEnum color;
	private PovDirection direction;
	private PovDirection previousDirection;
	private Body collisionBody;

	// player start
	private StartPlayer startPlayer;

	private float walkSpeed;
	private float shipSpeed;
	private int bombeStrenght;
	private int nbBombe;

	private boolean insideBombe;
	private int insideFire;

	// state
	private PlayerStateEnum state;
	private BombeTypeEnum bombeType;

	// bonus
	private boolean canPutLineOfBombe;
	private boolean canKickBombe;
	private boolean canRaiseBombe;
	private float previousWalkSpeed;

	// teleporte
	private Teleporter destinationTeleporter;
	private int teleportCountDown;

	// animation part
	int frameCounter;
	int offsetSprite;
	int nbFrameForAnimation;
	LouisColorEnum louisColor;
	boolean louisBurn;

	private int invincibleTime;

	public Player(World world, MultiBombermanGame mbGame, Level level, CharacterEnum character,
			CharacterColorEnum color, StartPlayer startPlayer, int bombeStrenght, int nbBombe) {
		this.startPlayer = startPlayer;
		this.character = character;
		this.color = color;
		this.previousDirection = PovDirection.south;
		this.direction = PovDirection.center;
		this.state = PlayerStateEnum.ON_LOUIS;
		this.world = world;
		this.mbGame = mbGame;
		this.level = level;
		this.bombeStrenght = bombeStrenght;
		this.nbBombe = nbBombe;
		this.walkSpeed = Constante.WALK_SPEED;
		this.bombeType = BombeTypeEnum.BOMBE_MAX;
		this.canPutLineOfBombe = false;
		this.canKickBombe = false;
		this.insideBombe = false;
		this.canRaiseBombe = false;
		this.insideFire = 0;
		this.shipSpeed = DEFAULT_SHIP_SPEED;
		this.louisColor = LouisColorEnum.BROWN;
		this.createBody();
	}

	@Override
	public void createBody() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(this.startPlayer.getX() + 0.5f, this.startPlayer.getY() + 0.5f);
		body = world.createBody(bodyDef);
		body.setFixedRotation(false);
		MassData data = new MassData();
		data.mass = 1f;
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
		this.level = null;
	}

	public void teleporte(Teleporter tel) {
		if (destinationTeleporter == null) {
			List<Teleporter> destination = this.level.getTeleporter().stream()
					.filter(t -> ((t.getX() != tel.getX() || t.getY() != tel.getY())
							&& this.level.getOccupedWallBrickBonus()[t.getX()][t.getY()] == null))
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
			// Gdx.app.log(CLASS_NAME, "teleporte end ok 2! ");
			destinationTeleporter = null;
		}
	}

	@Override
	public void update() {
		switch (this.state) {
		case BURNING:
			break;
		case CRYING:
			break;
		case DEAD:
			break;
		case INSIDE_TROLLEY:
			break;
		case CARRY_BOMBE:
		case ON_LOUIS:
		case NORMAL:
			if (insideFire > 0 && invincibleTime <= 0) {
				if (state == PlayerStateEnum.ON_LOUIS) {
					this.state = PlayerStateEnum.NORMAL;
					this.invincibleTime = 50;
				} else if (state == PlayerStateEnum.NORMAL) {
					this.state = PlayerStateEnum.BURNING;
				}
			}
			switch (this.direction) {
			case center:
				this.body.setLinearVelocity(0f, 0f);
				break;
			case east:
				this.body.setLinearVelocity(walkSpeed, 0f);
				break;
			case north:
				this.body.setLinearVelocity(0f, walkSpeed);
				break;
			case south:
				this.body.setLinearVelocity(0f, -walkSpeed);
				break;
			case west:
				this.body.setLinearVelocity(-walkSpeed, 0f);
				break;
			case southEast:
			case southWest:
			case northEast:
			case northWest:
			default:
				break;
			}
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
		if (this.body.getPosition().x > (float) Constante.GRID_SIZE_X) {
			this.body.setTransform(this.body.getPosition().x - (float) Constante.GRID_SIZE_X, this.body.getPosition().y,
					0f);
		}
		if (this.body.getPosition().x < 0f) {
			this.body.setTransform(this.body.getPosition().x + (float) Constante.GRID_SIZE_X, this.body.getPosition().y,
					0f);
		}
		if (this.body.getPosition().y > (float) Constante.GRID_SIZE_Y) {
			this.body.setTransform(this.body.getPosition().x, this.body.getPosition().y - (float) Constante.GRID_SIZE_Y,
					0f);
		}
		if (this.body.getPosition().y < 0f) {
			this.body.setTransform(this.body.getPosition().x, this.body.getPosition().y + (float) Constante.GRID_SIZE_Y,
					0f);
		}
		collisionBody.setTransform(this.body.getPosition(), body.getAngle());
		if (invincibleTime > 0) {
			invincibleTime--;
		}
	}

	public int getX() {
		return (int) (body.getPosition().x * 18f);
	}

	public int getY() {
		return (int) (body.getPosition().y * 16f);
	}

	public PovDirection getDirection() {
		return direction;
	}

	public void insideFire(boolean value) {
		if (value) {
			insideFire++;
		} else {
			insideFire--;
		}
	}

	public void insideBombe(boolean value) {
		insideBombe = value;
	}

	@Override
	public void move(PovDirection value) {
		Gdx.app.debug(CLASS_NAME, "press move : " + value.toString());
		if (value == PovDirection.east || value == PovDirection.south || value == PovDirection.north
				|| value == PovDirection.west) {
			this.previousDirection = value;
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

	/******************************************************
	 * --- PUT BOMBE ---
	 ******************************************************/
	@Override
	public void pressA() {
		if (this.nbBombe > 0 && !insideBombe) {
			putBombe((int) (body.getPosition().x), (int) (body.getPosition().y));
		}
	}

	/******************************************************
	 * --- EXPLODE BOMBE P ---
	 ******************************************************/
	@Override
	public void pressB() {
		if (bombeType == BombeTypeEnum.BOMBE_P) {
			this.level.getBombes().stream().filter(b -> b.bombeOfPlayer(this) && b.getType() == BombeTypeEnum.BOMBE_P)
					.forEach(Bombe::explode);
		}
	}

	/******************************************************
	 * --- PUT BOMBE LINE ---
	 ******************************************************/
	@Override
	public void pressX() {
		if (canPutLineOfBombe && !insideBombe) {
			int nb = nbBombe;
			switch (this.previousDirection) {
			case east:
				putBombeLineEast(nb);
				break;
			case north:
				putBombeLineNorth(nb);
				break;
			case south:
				putBombeLineSouth(nb);
				break;
			case west:
				putBombeLineWest(nb);
				break;
			case center:
			case northEast:
			case northWest:
			case southEast:
			case southWest:
			default:
				break;
			}
		}
	}

	private void putBombeLineWest(int nb) {
		int calcX;
		int calcY;
		if (nb > Constante.GRID_SIZE_X) {
			nb = Constante.GRID_SIZE_X - 1;
		}
		for (int i = 0; i < nb; i++) {
			calcX = GridUtils.calcIdxX((int) this.body.getPosition().x, -i);
			calcY = (int) this.body.getPosition().y;
			if (!putBombe(calcX, calcY)) {
				break;
			}
		}
	}

	private void putBombeLineSouth(int nb) {
		int calcX;
		int calcY;
		if (nb > Constante.GRID_SIZE_Y) {
			nb = Constante.GRID_SIZE_Y - 1;
		}
		for (int i = 0; i < nb; i++) {
			calcX = (int) this.body.getPosition().x;
			calcY = GridUtils.calcIdxY((int) this.body.getPosition().y, -i);
			if (!putBombe(calcX, calcY)) {
				break;
			}
		}
	}

	private void putBombeLineNorth(int nb) {
		int calcX;
		int calcY;
		if (nb > Constante.GRID_SIZE_Y) {
			nb = Constante.GRID_SIZE_Y - 1;
		}
		for (int i = 0; i < nb; i++) {
			calcX = (int) this.body.getPosition().x;
			calcY = GridUtils.calcIdxY((int) this.body.getPosition().y, i);
			if (!putBombe(calcX, calcY)) {
				break;
			}
		}
	}

	private void putBombeLineEast(int nb) {
		int calcX;
		int calcY;
		if (nb > Constante.GRID_SIZE_X) {
			nb = Constante.GRID_SIZE_X - 1;
		}
		for (int i = 0; i < nb; i++) {
			calcX = GridUtils.calcIdxX((int) this.body.getPosition().x, i);
			calcY = (int) this.body.getPosition().y;
			if (!putBombe(calcX, calcY)) {
				break;
			}
		}
	}

	public void bombeExploded() {
		this.nbBombe++;
	}

	private boolean putBombe(int x, int y) {
		if (x >= 0 && x < Constante.GRID_SIZE_X && y >= 0 && y < Constante.GRID_SIZE_Y
				&& this.level.getOccupedWallBrickBonus()[x][y] != null) {
			return false;
		} else {
			Bombe b = new Bombe(this.level, this.world, this.mbGame, this.bombeStrenght, x, y, this.bombeType, this,
					75);
			this.level.getBombes().add(b);
			this.nbBombe--;
		}
		return true;
	}

	@Override
	public void pressY() {
		// unused method
	}

	/******************************************************
	 * --- MANAGE SHIP SPEED ---
	 ******************************************************/
	@Override
	public void pressL() {
		this.shipSpeed += SHIP_SPEED_STEP;
	}

	@Override
	public void pressR() {
		this.shipSpeed += SHIP_SPEED_STEP;
	}

	@Override
	public void releaseL() {
		this.shipSpeed -= SHIP_SPEED_STEP;
	}

	@Override
	public void releaseR() {
		this.shipSpeed -= SHIP_SPEED_STEP;
	}

	/******************************************************
	 * --- MANAGE PICK UP BONUS ---
	 ******************************************************/
	public void takeBonus(BonusTypeEnum type) {
		Gdx.app.log(CLASS_NAME, "take bonus : " + type.name());
		switch (type) {
		case BOMBE:
			this.nbBombe++;
			break;
		case BOMBE_LINE:
			canPutLineOfBombe = true;
			break;
		case BOMBE_MAX:
			this.bombeType = BombeTypeEnum.BOMBE_MAX;
			break;
		case BOMBE_P:
			this.bombeType = BombeTypeEnum.BOMBE_P;
			break;
		case BOMBE_RUBBER:
			this.bombeType = BombeTypeEnum.BOMBE_RUBBER;
			break;
		case DEATH:
			takeDeathBonus();
			break;
		case EGGS:
			this.state = PlayerStateEnum.ON_LOUIS;
			break;
		case FIRE:
			this.bombeStrenght++;
			if (this.bombeStrenght > Constante.GRID_SIZE_X - 1) {
				this.bombeStrenght = Constante.GRID_SIZE_X - 1;
			}
			break;
		case FIRE_PLUS:
			this.bombeStrenght += 10;
			if (this.bombeStrenght > Constante.GRID_SIZE_X - 1) {
				this.bombeStrenght = Constante.GRID_SIZE_X - 1;
			}
			break;
		case GLOVE:
			this.canRaiseBombe = true;
			break;
		case KICK:
			canKickBombe = true;
			break;
		case ROLLER:
			this.walkSpeed += Constante.ADD_WALK_SPEED;
			break;
		case SHIELD:
			this.invincibleTime = INVINCIBLE_TIME;
			break;
		case SHOES:
			this.walkSpeed -= Constante.ADD_WALK_SPEED;
			break;
		case WALL:

			break;
		default:
			break;
		}
	}

	private void takeDeathBonus() {
		int val = ThreadLocalRandom.current().nextInt(0, 7);
		switch (val) {
		case 0:
			// walk like an old man

			break;
		case 1:
			// diarhee

			break;
		case 2:
			// constipation

			break;
		case 3:
			// echange avec un autre joueur

			break;
		case 4:
			// bombe lente

			break;
		case 5:
			// bombe rapide

			break;
		case 6:
			// course folle

			break;
		}
	}

	private void cancelLastMalus() {

	}

	/******************************************************
	 * --- UTILS ---
	 ******************************************************/
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

	/*************************************************
	 * --- DRAW ---
	 *************************************************/
	@Override
	public void drawIt() {
		switch (this.state) {
		case BURNING:
			drawBurning();
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
			drawStateNormal();
			break;
		case ON_LOUIS:
			drawStateOnLouis();
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
	}

	
	
	private void drawBurning() {
		boolean died = false;
		nbFrameForAnimation = SpriteService.getInstance().getAnimationSize(CharacterSpriteEnum.BURN);
		if (frameCounter > NB_FRAME) {
			frameCounter = 0;
			offsetSprite++;
			if (offsetSprite >= nbFrameForAnimation) {
				offsetSprite = 0;
				this.state = PlayerStateEnum.DEAD;
			}
		}
		mbGame.getBatch().draw(
				SpriteService.getInstance().getSprite(CharacterSpriteEnum.BURN, color, character, offsetSprite),
				(body.getPosition().x * 18f) - 15, (body.getPosition().y * 16f) - 5f);
		frameCounter++;
	}

	private void drawStateNormal() {
		nbFrameForAnimation = SpriteService.getInstance().getAnimationSize(LouisSpriteEnum.WALK_DOWN);
		if (direction != PovDirection.center) {
			if (frameCounter > NB_FRAME) {
				frameCounter = 0;
				offsetSprite++;
				if (offsetSprite >= nbFrameForAnimation) {
					offsetSprite = 0;
				}
			}
			frameCounter++;
		} else {
			offsetSprite = 0;
		}
		CharacterSpriteEnum drawSprite = CharacterSpriteEnum.WALK_DOWN;
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
		mbGame.getBatch().draw(SpriteService.getInstance().getSprite(drawSprite, color, character, offsetSprite),
				(body.getPosition().x * 18f) - 15, (body.getPosition().y * 16f) - 5f);
	}

	private void drawStateOnLouis() {
		nbFrameForAnimation = SpriteService.getInstance().getAnimationSize(LouisSpriteEnum.WALK_DOWN);
		if (direction != PovDirection.center) {
			if (frameCounter > NB_FRAME) {
				frameCounter = 0;
				offsetSprite++;
				if (offsetSprite >= nbFrameForAnimation) {
					offsetSprite = 0;
				}
			}
			frameCounter++;
		} else {
			offsetSprite = 0;
		}
		LouisSpriteEnum drawSpriteLouis = LouisSpriteEnum.WALK_DOWN;
		CharacterSpriteEnum drawSprite = CharacterSpriteEnum.ON_LOUIS_DOWN;
		switch (this.direction) {
		case center:
			if (previousDirection == PovDirection.west) {
				drawSprite = CharacterSpriteEnum.ON_LOUIS_LEFT;
				drawSpriteLouis = LouisSpriteEnum.WALK_LEFT;
			} else if (previousDirection == PovDirection.north) {
				drawSprite = CharacterSpriteEnum.ON_LOUIS_UP;
				drawSpriteLouis = LouisSpriteEnum.WALK_UP;
			} else if (previousDirection == PovDirection.east) {
				drawSprite = CharacterSpriteEnum.ON_LOUIS_RIGHT;
				drawSpriteLouis = LouisSpriteEnum.WALK_RIGHT;
			} else if (previousDirection == PovDirection.south) {
				drawSprite = CharacterSpriteEnum.ON_LOUIS_DOWN;
				drawSpriteLouis = LouisSpriteEnum.WALK_DOWN;
			}
			break;
		case east:
			drawSprite = CharacterSpriteEnum.ON_LOUIS_RIGHT;
			drawSpriteLouis = LouisSpriteEnum.WALK_RIGHT;
			break;
		case north:
			drawSprite = CharacterSpriteEnum.ON_LOUIS_UP;
			drawSpriteLouis = LouisSpriteEnum.WALK_UP;
			break;
		case south:
			drawSprite = CharacterSpriteEnum.ON_LOUIS_DOWN;
			drawSpriteLouis = LouisSpriteEnum.WALK_DOWN;
			break;
		case west:
			drawSprite = CharacterSpriteEnum.ON_LOUIS_LEFT;
			drawSpriteLouis = LouisSpriteEnum.WALK_LEFT;
			break;
		case northEast:
		case northWest:
		case southEast:
		case southWest:
		default:
			break;
		}
		if (this.direction == PovDirection.south || this.previousDirection == PovDirection.south) {
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(drawSprite, color, character, 0),
					(body.getPosition().x * 18f) - 15, (body.getPosition().y * 16f) - 5f);
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(drawSpriteLouis, louisColor, offsetSprite),
					(body.getPosition().x * 18f) - 15, (body.getPosition().y * 16f) - 5f);
		} else {
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(drawSpriteLouis, louisColor, offsetSprite),
					(body.getPosition().x * 18f) - 15, (body.getPosition().y * 16f) - 5f);
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(drawSprite, color, character, 0),
					(body.getPosition().x * 18f) - 15, (body.getPosition().y * 16f) - 5f);
		}
	}
}
