package com.mygdx.domain;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.mygdx.domain.enumeration.DeathBonusEnum;
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
import com.mygdx.enumeration.PlayerTypeEnum;
import com.mygdx.enumeration.SoundEnum;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.game.Game;
import com.mygdx.game.ia.Brain;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.Context;
import com.mygdx.service.SoundService;
import com.mygdx.service.SpriteService;
import com.mygdx.service.input_processor.ControlEventListener;
import com.mygdx.service.network.enumeration.NetworkPlayerStateEnum;
import com.mygdx.utils.GridUtils;

public class Player extends BodyAble implements ControlEventListener, Comparable<Player> {
	private static final String CLASS_NAME = "Player.class";
	private static final float RADIUS = 0.48f;

	private final Game game;

	private final PlayerTypeEnum type;
	private final CharacterEnum character;
	private final CharacterColorEnum color;
	private PovDirection direction;
	private PovDirection previousDirection;
	private PovDirection trolleyDirection;
	private PovDirection spaceShipDrawDirection;
	private Body collisionBody;
	private Body spaceShipBody;
	private Brain brain;

	private int score;

	// player start
	private int idx;
	private StartPlayer startPlayer;

	private float walkSpeed;
	private float shipSpeed;
	private int bombeStrenght;
	private int nbBombe;
	private int levelBombeStrenght;
	private int levelNbBombe;

	private boolean insideBombe;
	private int insideFire;
	private Player lastFireFrom;

	// state
	private PlayerStateEnum state;
	private PlayerStateEnum previousState;
	private BombeTypeEnum bombeType;

	// bonus
	private boolean canPutLineOfBombe;
	private boolean canKickBombe;
	private boolean canRaiseBombe;

	// teleporte
	private Teleporter destinationTeleporter;
	private int teleportCountDown;

	// animation part
	private LouisColorEnum louisColor;

	private float invincibleTime;
	private float malusTime;
	private DeathBonusEnum deathBonus;

	private Map<CharacterSpriteEnum, Animation<TextureRegion>> animations;
	private Map<LouisSpriteEnum, Animation<TextureRegion>> animationsLouis;
	private Animation<TextureRegion> footInWaterAnimation;
	private float animationTime;
	private boolean canPassWall;
	private float wallTimeout;
	private Bombe lastBombeTouched;
	private Bombe raisedBombe;
	private int nbBombeSpaceShip = 1;

	// network
	private NetworkPlayerStateEnum networkStateEnum;
	private CharacterSpriteEnum drawSprite;
	private LouisSpriteEnum drawSpriteLouis;
	private int spriteIndex;
	private int spriteIndexLouis;

	public Player(Game game, World world, MultiBombermanGame mbGame, Level level, PlayerTypeEnum type,
			CharacterEnum character, CharacterColorEnum color, StartPlayer startPlayer, int bombeStrenght, int nbBombe,
			int idx) {
		this.idx = idx;
		this.score = 0;
		this.startPlayer = startPlayer;
		this.type = type;
		this.character = character;
		this.color = color;
		this.previousDirection = PovDirection.south;
		this.direction = PovDirection.center;
		this.state = PlayerStateEnum.NORMAL;
		this.networkStateEnum = NetworkPlayerStateEnum.OTHER;
		this.previousState = PlayerStateEnum.NORMAL;
		this.game = game;
		this.world = world;
		this.mbGame = mbGame;
		this.level = level;
		this.bombeStrenght = bombeStrenght;
		this.levelBombeStrenght = bombeStrenght;
		this.nbBombe = nbBombe;
		this.levelNbBombe = nbBombe;
		this.walkSpeed = Constante.WALK_SPEED;
		this.bombeType = BombeTypeEnum.BOMBE;
		this.canPutLineOfBombe = false;
		this.canKickBombe = false;
		this.insideBombe = false;
		this.canRaiseBombe = false;
		this.canPassWall = false;
		this.wallTimeout = 0;
		this.insideFire = 0;
		this.shipSpeed = Constante.DEFAULT_SHIP_SPEED;
		this.louisColor = LouisColorEnum.random();
		this.trolleyDirection = PovDirection.east;
		this.animations = new EnumMap<>(CharacterSpriteEnum.class);
		for (CharacterSpriteEnum e : CharacterSpriteEnum.values()) {
			this.animations.put(e, new Animation<TextureRegion>((1f / 5f),
					SpriteService.getInstance().getSpriteForAnimation(e, this.color, this.character)));
		}
		this.animationsLouis = new EnumMap<>(LouisSpriteEnum.class);
		for (LouisSpriteEnum e : LouisSpriteEnum.values()) {
			this.animationsLouis.put(e, new Animation<TextureRegion>((1f / 5f),
					SpriteService.getInstance().getSpriteForAnimation(e, this.louisColor)));
		}
		footInWaterAnimation = new Animation<>((1f / 12),
				SpriteService.getInstance().getSpriteForAnimation(SpriteEnum.UNDERWATER));
		this.createBody();
		if (this.type == PlayerTypeEnum.CPU) {
			this.brain = new Brain(this);
		}
	}

	public void reset() {
		this.previousDirection = PovDirection.south;
		this.direction = PovDirection.center;
		this.state = PlayerStateEnum.NORMAL;
		this.previousState = PlayerStateEnum.NORMAL;
		this.bombeStrenght = levelBombeStrenght;
		this.nbBombe = levelNbBombe;
		this.walkSpeed = Constante.WALK_SPEED;
		this.bombeType = BombeTypeEnum.BOMBE;
		this.canPutLineOfBombe = false;
		this.canKickBombe = false;
		this.insideBombe = false;
		this.canRaiseBombe = false;
		this.canPassWall = false;
		this.wallTimeout = 0;
		this.insideFire = 0;
		this.shipSpeed = Constante.DEFAULT_SHIP_SPEED;
		this.trolleyDirection = PovDirection.east;
	}

	@Override
	public void createBody() {
		this.createBodyWithPosition(this.startPlayer.getX() + 0.5f, this.startPlayer.getY() + 0.5f);
	}

	public void createBodyWithPosition(float x, float y) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		this.body = world.createBody(bodyDef);
		this.body.setFixedRotation(false);
		MassData data = new MassData();
		data.mass = 1f;
		this.body.setMassData(data);
		this.body.setUserData(this);
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
		filter.maskBits = CollisionConstante.GROUP_PLAYER_BODY;
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

	public void createSpaceShipBody(float x, float y) {
		float calcY;
		if (y <= ((float) Constante.GRID_SIZE_Y) / 2f) {
			calcY = 0.5f - Constante.BAD_BOMBER_WALL_WIDTH;
		} else {
			calcY = ((float) Constante.GRID_SIZE_Y - 0.5f) + Constante.BAD_BOMBER_WALL_WIDTH;
		}
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, calcY);
		CircleShape circle = new CircleShape();
		circle.setRadius(0.05f);
		this.spaceShipBody = world.createBody(bodyDef);
		this.spaceShipBody.setUserData(this);
		Fixture fixture = spaceShipBody.createFixture(circle, 0.0f);
		fixture.setFriction(0f);
		fixture.setUserData(this);
		Filter filter = new Filter();
		filter.categoryBits = CollisionConstante.CATEGORY_SPACESHIP;
		filter.maskBits = CollisionConstante.GROUP_SPACESHIP;
		fixture.setFilterData(filter);
	}

	@Override
	public void dispose() {
		if (this.body != null) {
			this.world.destroyBody(body);
			this.body = null;
		}
		if (this.collisionBody != null) {
			this.world.destroyBody(this.collisionBody);
			this.collisionBody = null;
		}
		this.level = null;
	}

	public PlayerTypeEnum getType() {
		return this.type;
	}

	public PlayerStateEnum getState() {
		return this.state;
	}

	/************************************************************************************************************
	 * -------------------------------------------- TROLLEY PART
	 * ------------------------------------------------
	 ************************************************************************************************************/
	public void crush() {
		this.previousDirection = PovDirection.center;
		this.direction = PovDirection.center;
		this.changeState(PlayerStateEnum.BURNING);
		SoundService.getInstance().playSound(SoundEnum.BURN);
	}

	public void enterInTrolley() {
		this.previousDirection = this.direction;
		this.direction = PovDirection.center;
		this.changeState(PlayerStateEnum.INSIDE_TROLLEY);
	}

	public void exitTrolley() {
		this.state = this.previousState;
	}

	public void trolleyMovePlayer(float x, float y, PovDirection trolleyDirection) {
		this.body.setTransform(x, y, 0f);
		this.collisionBody.setTransform(x, y, 0f);
		this.trolleyDirection = trolleyDirection;
	}

	public boolean isInsideTrolley() {
		return this.state == PlayerStateEnum.INSIDE_TROLLEY;
	}

	/************************************************************************************************************
	 * -------------------------------------------- BAD BOMBER PART
	 * ------------------------------------------------
	 ************************************************************************************************************/
	public void playerToBadBomber() {
		if (this.lastFireFrom != null && this.lastFireFrom.isBadBomber()) {
			this.lastFireFrom.youBurnMeBadBomber(this);
		}
		this.createSpaceShipBody(this.getBodyX(), this.getBodyY());
		if (this.body != null) {
			this.world.destroyBody(body);
			this.body = null;
		}
		if (this.collisionBody != null) {
			this.world.destroyBody(this.collisionBody);
			this.collisionBody = null;
		}
		this.nbBombeSpaceShip = 1;
		this.changeState(PlayerStateEnum.BAD_BOMBER);
	}

	public void badBomberToPlayer(Player playerKilled) {
		if (lastFireFrom == null) {
			if (this.spaceShipBody != null) {
				this.world.destroyBody(spaceShipBody);
				this.spaceShipBody = null;
			}
			this.createBodyWithPosition(playerKilled.getBodyX(), playerKilled.getBodyY());
			this.changeState(PlayerStateEnum.NORMAL);
		}
	}

	public void endBadBomberTime() {
		this.changeState(PlayerStateEnum.DEAD);
		this.networkStateEnum = NetworkPlayerStateEnum.DEAD;
	}

	private void youBurnMeBadBomber(Player playerKilled) {
		this.badBomberToPlayer(playerKilled);
	}

	/************************************************************************************************************
	 * -------------------------------------------- COMMONS
	 * ----------------------------------------------------
	 ************************************************************************************************************/

	@Override
	public void update() {
		if (this.type != PlayerTypeEnum.HUMAN && this.type != PlayerTypeEnum.NET && this.brain != null) {
			this.brain.think();
		}
		switch (this.state) {
		case BURNING:
			break;
		case CRYING:
			if (insideFire > 0 && invincibleTime <= 0f) {
				SoundService.getInstance().playSound(SoundEnum.BURN);
				this.changeState(PlayerStateEnum.BURNING);
			}
			break;
		case DEAD:
			this.networkStateEnum = NetworkPlayerStateEnum.DEAD;
			this.dispose();
			break;
		case INSIDE_TROLLEY:
			break;
		case CARRY_BOMBE:
		case ON_LOUIS:
		case NORMAL:
			if (insideFire > 0 && invincibleTime <= 0f) {
				if (state == PlayerStateEnum.ON_LOUIS) {
					SoundService.getInstance().playSound(SoundEnum.BURN);
					this.changeState(PlayerStateEnum.NORMAL);
					this.invincibleTime = Constante.INVINCIBLE_TIME;
				} else if (state == PlayerStateEnum.NORMAL) {
					this.direction = PovDirection.center;
					this.previousDirection = PovDirection.center;
					SoundService.getInstance().playSound(SoundEnum.BURN);
					changeState(PlayerStateEnum.BURNING);
				} else if (state == PlayerStateEnum.CARRY_BOMBE) {
					this.raisedBombe.explode();
					this.direction = PovDirection.center;
					this.previousDirection = PovDirection.center;
					SoundService.getInstance().playSound(SoundEnum.BURN);
					changeState(PlayerStateEnum.BURNING);
				}
			}
			switch (this.direction) {
			case center:
				this.body.setLinearVelocity(0f, 0f);
				break;
			case east:
				if (this.deathBonus == DeathBonusEnum.FAST_MOVE) {
					this.body.setLinearVelocity(Constante.FAST_WALK_SPEED, 0f);
				} else if (this.deathBonus == DeathBonusEnum.SLOW_MOVE) {
					this.body.setLinearVelocity(Constante.SLOW_WALK_SPEED, 0f);
				} else {
					this.body.setLinearVelocity(walkSpeed, 0f);
				}
				break;
			case north:
				if (this.deathBonus == DeathBonusEnum.FAST_MOVE) {
					this.body.setLinearVelocity(0f, Constante.FAST_WALK_SPEED);
				} else if (this.deathBonus == DeathBonusEnum.SLOW_MOVE) {
					this.body.setLinearVelocity(0f, Constante.SLOW_WALK_SPEED);
				} else {
					this.body.setLinearVelocity(0f, walkSpeed);
				}
				break;
			case south:
				if (this.deathBonus == DeathBonusEnum.FAST_MOVE) {
					this.body.setLinearVelocity(0f, -Constante.FAST_WALK_SPEED);
				} else if (this.deathBonus == DeathBonusEnum.SLOW_MOVE) {
					this.body.setLinearVelocity(0f, -Constante.SLOW_WALK_SPEED);
				} else {
					this.body.setLinearVelocity(0f, -walkSpeed);
				}
				break;
			case west:
				if (this.deathBonus == DeathBonusEnum.FAST_MOVE) {
					this.body.setLinearVelocity(-Constante.FAST_WALK_SPEED, 0f);
				} else if (this.deathBonus == DeathBonusEnum.SLOW_MOVE) {
					this.body.setLinearVelocity(-Constante.SLOW_WALK_SPEED, 0f);
				} else {
					this.body.setLinearVelocity(-walkSpeed, 0f);
				}
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
				this.changeState(this.previousState);
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
		case BAD_BOMBER:
			switch (this.direction) {
			case center:
				this.spaceShipBody.setLinearVelocity(0f, 0f);
				break;
			case east:
				this.spaceShipBody.setLinearVelocity(shipSpeed, 0f);
				break;
			case north:
				this.spaceShipBody.setLinearVelocity(0f, shipSpeed);
				break;
			case south:
				this.spaceShipBody.setLinearVelocity(0f, -shipSpeed);
				break;
			case west:
				this.spaceShipBody.setLinearVelocity(-shipSpeed, 0f);
				break;
			case southEast:
			case southWest:
			case northEast:
			case northWest:
			default:
				break;
			}
			if (this.spaceShipBody.getPosition().x < Constante.BAD_BOMBER_WALL_OFFSET) {
				this.spaceShipDrawDirection = PovDirection.east;
			} else if (this.spaceShipBody.getPosition().x > (float) Constante.GRID_SIZE_X
					- Constante.BAD_BOMBER_WALL_OFFSET) {
				this.spaceShipDrawDirection = PovDirection.west;
			} else if (this.spaceShipBody.getPosition().y > (float) Constante.GRID_SIZE_Y
					- Constante.BAD_BOMBER_WALL_OFFSET) {
				this.spaceShipDrawDirection = PovDirection.south;
			} else if (this.spaceShipBody.getPosition().y < Constante.BAD_BOMBER_WALL_OFFSET) {
				this.spaceShipDrawDirection = PovDirection.north;
			} else {
				this.spaceShipDrawDirection = PovDirection.east;
			}
			break;
		default:
			break;
		}
		if (this.body != null && this.state != PlayerStateEnum.INSIDE_TROLLEY && this.state != PlayerStateEnum.DEAD
				&& this.state != PlayerStateEnum.BURNING && this.state != PlayerStateEnum.CRYING) {
			if (this.body.getPosition().x > (float) Constante.GRID_SIZE_X) {
				this.body.setTransform(this.body.getPosition().x - (float) Constante.GRID_SIZE_X,
						this.body.getPosition().y, 0f);
			}
			if (this.body.getPosition().x < 0f) {
				this.body.setTransform(this.body.getPosition().x + (float) Constante.GRID_SIZE_X,
						this.body.getPosition().y, 0f);
			}
			if (this.body.getPosition().y > (float) Constante.GRID_SIZE_Y) {
				this.body.setTransform(this.body.getPosition().x,
						this.body.getPosition().y - (float) Constante.GRID_SIZE_Y, 0f);
			}
			if (this.body.getPosition().y < 0f) {
				this.body.setTransform(this.body.getPosition().x,
						this.body.getPosition().y + (float) Constante.GRID_SIZE_Y, 0f);
			}
			this.collisionBody.setTransform(this.body.getPosition(), this.body.getAngle());
			if (this.raisedBombe != null && this.body != null) {
				this.raisedBombe.playerCarryThatBombe(this.getBodyX(), this.getBodyY());
			}
		}
		if (this.invincibleTime > 0f) {
			this.invincibleTime -= Gdx.graphics.getDeltaTime();
		}
		if (this.malusTime > 0f) {
			this.malusTime -= Gdx.graphics.getDeltaTime();
			if (this.malusTime <= 0f) {
				this.cancelLastMalus();
			}
		}
		if (this.wallTimeout <= 0f && canPassWall && !isInsideBrick()) {
			for (int i = 0; i < this.body.getFixtureList().size; i++) {
				Filter f = new Filter();
				f.categoryBits = CollisionConstante.CATEGORY_PLAYER;
				f.maskBits = CollisionConstante.GROUP_PLAYER_BODY;
				this.body.getFixtureList().get(i).setFilterData(f);
			}
			canPassWall = false;
		} else {
			wallTimeout -= Gdx.graphics.getDeltaTime();
		}

		if (this.deathBonus == DeathBonusEnum.DIAREE && this.state != PlayerStateEnum.BURNING
				&& this.state != PlayerStateEnum.INSIDE_TROLLEY && this.state != PlayerStateEnum.DEAD
				&& this.nbBombe > 0 && !this.insideBombe && body != null) {
			putBombe((int) (this.body.getPosition().x), (int) (this.body.getPosition().y));
		}
	}

	public int getIndex() {
		return this.idx;
	}

	public void winTheGame() {
		if (this.state == PlayerStateEnum.ON_LOUIS) {
			this.state = PlayerStateEnum.VICTORY_ON_LOUIS;
		} else {
			this.state = PlayerStateEnum.VICTORY;
		}
		this.score++;
	}

	public int getScore() {
		return this.score;
	}

	public PovDirection getDirection() {
		return this.direction;
	}

	public void insideFire(boolean value) {
		if (value) {
			this.insideFire++;
		} else {
			this.insideFire--;
		}
	}

	public void insideBombe(boolean value) {
		this.insideBombe = value;
	}

	public void carriedBombeExplode(Bombe bombe) {
		this.raisedBombe = null;
		this.changeState(PlayerStateEnum.NORMAL);
	}

	public void bombeExploded() {
		if (this.state != PlayerStateEnum.BAD_BOMBER) {
			this.nbBombe++;
		} else {
			this.nbBombeSpaceShip++;
			if (this.nbBombeSpaceShip > 1) {
				this.nbBombeSpaceShip = 1;
				this.nbBombe++;
			}
		}
	}

	public boolean isInsideBrick() {
		return this.level.getOccupedWallBrickBonus()[(int) this.getBodyX()][(int) this.getBodyY()] != null;
	}

	public void setLastFireFrom(Player playerGenerateThisFire) {
		this.lastFireFrom = playerGenerateThisFire;
	}

	public float getShipPixelY() {
		return (spaceShipBody.getPosition().y * Constante.GRID_PIXELS_SIZE_Y);
	}

	public float getShipPixelX() {
		return (spaceShipBody.getPosition().x * Constante.GRID_PIXELS_SIZE_X);
	}

	public float getShipY() {
		return spaceShipBody.getPosition().y;
	}

	public float getShipX() {
		return spaceShipBody.getPosition().x;
	}

	public void teleporte(Teleporter tel) {
		if (this.destinationTeleporter == null) {
			List<Teleporter> destination = this.level.getTeleporter().stream()
					.filter(t -> ((t.getX() != tel.getX() || t.getY() != tel.getY())
							&& this.level.getOccupedWallBrickBonus()[t.getX()][t.getY()] == null))
					.collect(Collectors.toList());
			if (!destination.isEmpty()) {
				int i = ThreadLocalRandom.current().nextInt(0, destination.size());
				this.destinationTeleporter = destination.get(i);
				this.changeState(PlayerStateEnum.TELEPORT);
				this.teleportCountDown = 6;
				tel.animate(true);
				this.destinationTeleporter.animate(false);
			}
		}
	}

	public void teleporteEnd(Teleporter tel) {
		if (tel.equals(destinationTeleporter) && this.teleportCountDown == 0
				&& this.state != PlayerStateEnum.TELEPORT) {
			this.destinationTeleporter = null;
		}
	}

	public void touchBombe(Bombe b) {
		lastBombeTouched = b;
	}

	public void untouchBombe(Bombe b) {
		if (lastBombeTouched != null && lastBombeTouched.equals(b)) {
			lastBombeTouched = null;
		}
	}

	private void changeState(PlayerStateEnum newState) {
		this.animationTime = 0;
		this.previousState = this.state;
		this.state = newState;
	}

	public void flyBombeHurtMyHead() {
		if (state != PlayerStateEnum.BAD_BOMBER || state != PlayerStateEnum.BURNING
				|| state != PlayerStateEnum.CRYING) {
			this.changeState(PlayerStateEnum.CRYING);
		}
	}

	public Map<Integer, Short> getLevelDefinition() {
		return this.level.getState();
	}

	public boolean isCanKickBombe() {
		return canKickBombe;
	}

	public boolean isDead() {
		return this.state == PlayerStateEnum.DEAD;
	}

	public boolean isBurning() {
		return this.state == PlayerStateEnum.BURNING;
	}

	public boolean isBadBomber() {
		return this.state == PlayerStateEnum.BAD_BOMBER;
	}

	/************************************************************
	 * --- network ---
	 ************************************************************/
	public NetworkPlayerStateEnum getNetworkStateEnum() {
		return networkStateEnum;
	}

	public CharacterSpriteEnum getDrawSprite() {
		return drawSprite;
	}

	public LouisSpriteEnum getDrawSpriteLouis() {
		return drawSpriteLouis;
	}

	public LouisColorEnum getLouisColor() {
		return this.louisColor;
	}

	public int getSpriteIndex() {
		return spriteIndex;
	}

	public int getSpriteIndexLouis() {
		return spriteIndexLouis;
	}

	/************************************************************************************************************
	 * -------------------------------------------- CONTROLE
	 * ----------------------------------------------------
	 ************************************************************************************************************/
	@Override
	public void move(PovDirection value) {
		Gdx.app.debug(CLASS_NAME, "press move : " + value.toString());
		if (this.deathBonus == DeathBonusEnum.REVERSE_MOVE) {
			value = inverteDirection(value);
		}
		if (value == PovDirection.east || value == PovDirection.south || value == PovDirection.north
				|| value == PovDirection.west) {
			this.previousDirection = value;
		}
		this.direction = value;
	}

	private PovDirection inverteDirection(PovDirection value) {
		PovDirection tmp = value;
		switch (value) {
		case center:
			break;
		case east:
			tmp = PovDirection.west;
			break;
		case north:
			tmp = PovDirection.south;
			break;
		case northEast:
			tmp = PovDirection.southWest;
			break;
		case northWest:
			tmp = PovDirection.southEast;
			break;
		case south:
			tmp = PovDirection.north;
			break;
		case southEast:
			tmp = PovDirection.northWest;
			break;
		case southWest:
			tmp = PovDirection.northEast;
			break;
		case west:
			tmp = PovDirection.east;
			break;
		default:
			break;
		}
		return tmp;
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
		if (this.state != PlayerStateEnum.BURNING && this.state != PlayerStateEnum.DEAD
				&& this.state != PlayerStateEnum.BAD_BOMBER && this.nbBombe > 0 && !insideBombe) {
			putBombe((int) (body.getPosition().x), (int) (body.getPosition().y));
		} else if (this.state == PlayerStateEnum.BAD_BOMBER
				&& ((this.spaceShipBody.getPosition().x >= 1.0f
						&& this.spaceShipBody.getPosition().x <= (float) Constante.GRID_SIZE_X - 1.0f)
						|| (this.spaceShipBody.getPosition().y >= 1.0f
								&& this.spaceShipBody.getPosition().y <= (float) Constante.GRID_SIZE_Y - 1.0f))
				&& this.nbBombeSpaceShip > 0) {
			Bombe b = new Bombe(this.level, this.world, this.mbGame, 2, (int) this.getShipX(), (int) this.getShipY(),
					BombeTypeEnum.BOMBE, this, Constante.DEFAULT_BOMBE, spaceShipDrawDirection);
			this.level.getBombes().add(b);
			this.nbBombeSpaceShip--;
			if (lastFireFrom != null) {
				lastFireFrom = null;
			}
		}
	}

	/******************************************************
	 * --- EXPLODE BOMBE P ---
	 ******************************************************/
	@Override
	public void pressB() {
		if (this.state != PlayerStateEnum.BURNING && this.state != PlayerStateEnum.DEAD
				&& this.state != PlayerStateEnum.BAD_BOMBER && bombeType == BombeTypeEnum.BOMBE_P) {
			this.level.getBombes().stream().filter(b -> b.bombeOfPlayer(this) && b.getType() == BombeTypeEnum.BOMBE_P)
					.forEach(Bombe::explode);
		}
	}

	/******************************************************
	 * --- PUT BOMBE LINE ---
	 ******************************************************/
	@Override
	public void pressX() {
		if (this.state != PlayerStateEnum.BURNING && this.state != PlayerStateEnum.DEAD
				&& this.state != PlayerStateEnum.BAD_BOMBER && canPutLineOfBombe && !insideBombe) {
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

	/******************************************************
	 * --- Throw Bombe ---
	 ******************************************************/
	@Override
	public void pressY() {
		if (canRaiseBombe && lastBombeTouched != null && this.state != PlayerStateEnum.CARRY_BOMBE
				&& this.state != PlayerStateEnum.BAD_BOMBER && this.state != PlayerStateEnum.BURNING
				&& this.state != PlayerStateEnum.CRYING && this.state != PlayerStateEnum.DEAD
				&& this.state != PlayerStateEnum.INSIDE_TROLLEY && this.state != PlayerStateEnum.ON_LOUIS
				&& this.state != PlayerStateEnum.TELEPORT && this.state != PlayerStateEnum.VICTORY
				&& this.state != PlayerStateEnum.VICTORY_ON_LOUIS) {
			this.changeState(PlayerStateEnum.CARRY_BOMBE);
			this.raisedBombe = lastBombeTouched;
			this.raisedBombe.gotCarried();
		} else if (this.state == PlayerStateEnum.CARRY_BOMBE) {
			this.animationTime = 0f;
			this.state = PlayerStateEnum.THROW_BOMBE;
			this.raisedBombe
					.iBelieveICanFly(this.direction == PovDirection.center ? this.previousDirection : this.direction);
			this.raisedBombe = null;
		}
	}

	/******************************************************
	 * --- MANAGE SHIP SPEED ---
	 ******************************************************/
	@Override
	public void pressL() {
		this.shipSpeed += Constante.SHIP_SPEED_STEP;
	}

	@Override
	public void pressR() {
		this.shipSpeed += Constante.SHIP_SPEED_STEP;
	}

	@Override
	public void releaseL() {
		this.shipSpeed -= Constante.SHIP_SPEED_STEP;
	}

	@Override
	public void releaseR() {
		this.shipSpeed -= Constante.SHIP_SPEED_STEP;
	}

	/************************************************************************************************************
	 * ------------------------------------------- DROP BOMBE
	 * ---------------------------------------------------
	 ************************************************************************************************************/
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

	private boolean putBombe(int x, int y) {
		if (x >= 0 && x < Constante.GRID_SIZE_X && y >= 0 && y < Constante.GRID_SIZE_Y
				&& this.level.getOccupedWallBrickBonus()[x][y] != null) {
			return false;
		} else {
			float bombeTime = Constante.NORMAL_BOMBE_TIME;
			if (this.deathBonus != null) {
				switch (this.deathBonus) {
				case DIAREE:
					break;
				case EXCHANGE:
					break;
				case FAST_BOMBE:
					bombeTime = Constante.FASTE_BOMBE_TIME;
					break;
				case FAST_MOVE:
					break;
				case LOW_BOMBE:
					break;
				case REVERSE_MOVE:
					break;
				case SLOW_BOMBE:
					bombeTime = Constante.SLOW_BOMBE_TIME;
					break;
				case SLOW_MOVE:
					break;
				default:
					break;
				}
			}
			if (deathBonus == null || deathBonus != DeathBonusEnum.CONSTIPATION) {
				Bombe b = new Bombe(this.level, this.world, this.mbGame, this.bombeStrenght, x, y, this.bombeType, this,
						bombeTime);
				this.level.getBombes().add(b);
				this.nbBombe--;
			}
		}
		return true;
	}

	/*******************************************************************************
	 * -----------------------------------------------------------------------------
	 * --------------------------- MANAGE PICK UP BONUS ----------------------------
	 * -----------------------------------------------------------------------------
	 *******************************************************************************/
	public void takeBonus(BonusTypeEnum type) {
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
			this.animationsLouis = new EnumMap<>(LouisSpriteEnum.class);
			this.louisColor = LouisColorEnum.random();
			for (LouisSpriteEnum e : LouisSpriteEnum.values()) {
				this.animationsLouis.put(e, new Animation<TextureRegion>((1f / 5f),
						SpriteService.getInstance().getSpriteForAnimation(e, this.louisColor)));
			}
			this.changeState(PlayerStateEnum.ON_LOUIS);
			SoundService.getInstance().playSound(SoundEnum.LOUIS);
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
			this.canKickBombe = false;
			break;
		case KICK:
			this.canRaiseBombe = false;
			this.canKickBombe = true;
			break;
		case ROLLER:
			this.walkSpeed += Constante.ADD_WALK_SPEED;
			break;
		case SHIELD:
			this.invincibleTime = Constante.INVINCIBLE_TIME;
			break;
		case SHOES:
			this.walkSpeed -= Constante.ADD_WALK_SPEED;
			break;
		case WALL:
			for (int i = 0; i < this.body.getFixtureList().size; i++) {
				Filter f = new Filter();
				f.categoryBits = CollisionConstante.CATEGORY_PLAYER;
				f.maskBits = CollisionConstante.GROUP_PLAYER_BODY_PASS_WALL;
				this.body.getFixtureList().get(i).setFilterData(f);
			}
			this.canPassWall = true;
			this.wallTimeout = 250;
			break;
		default:
			break;
		}
	}

	private void takeDeathBonus() {
		this.cancelLastMalus();
		this.deathBonus = DeathBonusEnum.random();
		switch (this.deathBonus) {
		case CONSTIPATION:
		case DIAREE:
			break;
		case EXCHANGE:
			this.deathBonus = null;
			break;
		case FAST_BOMBE:
			break;
		case FAST_MOVE:
			break;
		case LOW_BOMBE:
			break;
		case REVERSE_MOVE:
			if (this.deathBonus == DeathBonusEnum.REVERSE_MOVE) {
				direction = inverteDirection(this.direction);
			}
			break;
		case SLOW_BOMBE:
			break;
		case SLOW_MOVE:
			break;
		default:
			break;
		}
		malusTime = Constante.MALUS_TIME;
	}

	private void cancelLastMalus() {
		if (this.deathBonus != null) {
			this.deathBonus = null;
		}
	}

	@Override
	public void drawIt() {
		animationTime += Gdx.graphics.getDeltaTime();
		switch (this.state) {
		case BURNING:
			drawBurning();
			break;
		case CARRY_BOMBE:
			drawCarryBombe();
			break;
		case CRYING:
			drawCrying();
			break;
		case DEAD:
			break;
		case INSIDE_TROLLEY:
			drawInsideTrolley();
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
			drawThrowBombe();
			break;
		case VICTORY:
			drawVictory();
			break;
		case VICTORY_ON_LOUIS:
			drawVictoryOnLouis();
			break;
		case BAD_BOMBER:
			drawBadBomber();
			break;
		default:
			break;
		}
		if (this.level != null && this.level.isFootInWater() && this.body != null) {
			mbGame.getBatch().draw(footInWaterAnimation.getKeyFrame(animationTime, true), getPixelX() - 9f,
					getPixelY() - 5f);
		}
	}

	private void drawBadBomber() {
		spriteIndex = 0;
		this.networkStateEnum = NetworkPlayerStateEnum.BADBOMBER;
		switch (spaceShipDrawDirection) {
		case center:
			break;
		case east:
			drawSprite = CharacterSpriteEnum.ON_LOUIS_RIGHT;
			mbGame.getBatch().draw(animations.get(drawSprite).getKeyFrame(spriteIndex, false), getShipPixelX() - 15f,
					getShipPixelY() - 20f);
			break;
		case north:
			drawSprite = CharacterSpriteEnum.ON_LOUIS_UP;
			mbGame.getBatch().draw(animations.get(drawSprite).getKeyFrame(spriteIndex, false), getShipPixelX() - 15f,
					getShipPixelY() - 20f);
			break;
		case northEast:
			break;
		case northWest:
			break;
		case south:
			drawSprite = CharacterSpriteEnum.ON_LOUIS_DOWN;
			mbGame.getBatch().draw(animations.get(drawSprite).getKeyFrame(spriteIndex, false), getShipPixelX() - 15f,
					getShipPixelY() - 20f);
			break;
		case southEast:
			break;
		case southWest:
			break;
		case west:
			drawSprite = CharacterSpriteEnum.ON_LOUIS_LEFT;
			mbGame.getBatch().draw(animations.get(drawSprite).getKeyFrame(spriteIndex, false), getShipPixelX() - 15f,
					getShipPixelY() - 20f);
			break;
		default:
			break;
		}
		mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.SPACESHIP, 0), getShipPixelX() - 15f,
				getShipPixelY() - 20f);
	}

	private void drawVictoryOnLouis() {
		this.networkStateEnum = NetworkPlayerStateEnum.LOUIS;
		this.drawSprite = CharacterSpriteEnum.ON_LOUIS_DOWN;
		this.spriteIndex = 0;
		this.drawSpriteLouis = LouisSpriteEnum.VICTORY;
		this.spriteIndexLouis = animationsLouis.get(LouisSpriteEnum.VICTORY)
				.getKeyFrameIndex(this.direction == PovDirection.center ? 0 : animationTime);
		mbGame.getBatch().draw(animations.get(CharacterSpriteEnum.ON_LOUIS_DOWN).getKeyFrame(0, false),
				getPixelX() - 15f, getPixelY() - 15f);
		mbGame.getBatch()
				.draw(animationsLouis.get(LouisSpriteEnum.VICTORY)
						.getKeyFrame(this.direction == PovDirection.center ? 0 : animationTime, true), getPixelX() - 15,
						getPixelY() - 5f);
	}

	private void drawVictory() {
		this.networkStateEnum = NetworkPlayerStateEnum.OTHER;
		this.drawSprite = CharacterSpriteEnum.VICTORY;
		this.spriteIndex = animations.get(CharacterSpriteEnum.VICTORY).getKeyFrameIndex(animationTime);
		mbGame.getBatch().draw(animations.get(CharacterSpriteEnum.VICTORY).getKeyFrame(animationTime, true),
				getPixelX() - 15, getPixelY() - 5f);
	}

	private void drawCrying() {
		this.networkStateEnum = NetworkPlayerStateEnum.OTHER;
		this.drawSprite = CharacterSpriteEnum.ANGRY;
		this.spriteIndex = animations.get(CharacterSpriteEnum.ANGRY).getKeyFrameIndex(animationTime);
		mbGame.getBatch().draw(animations.get(CharacterSpriteEnum.ANGRY).getKeyFrame(animationTime, true),
				getPixelX() - 15f, getPixelY() - 5f);
		if (animationTime > Constante.CRYING_TIME) {
			changeState(PlayerStateEnum.NORMAL);
		}
	}

	private void drawBurning() {
		this.networkStateEnum = NetworkPlayerStateEnum.OTHER;
		this.drawSprite = CharacterSpriteEnum.BURN;
		this.spriteIndex = animations.get(drawSprite).getKeyFrameIndex(animationTime);
		mbGame.getBatch().draw(animations.get(drawSprite).getKeyFrame(animationTime, false), getPixelX() - 15f,
				getPixelY() - 5f);
		if (animations.get(drawSprite).isAnimationFinished(animationTime)) {
			if (Context.isBadBomber() && !this.game.isSuddentDeathTime()) {
				this.playerToBadBomber();
			} else {
				changeState(PlayerStateEnum.DEAD);
			}
		}
	}

	private void drawThrowBombe() {
		this.networkStateEnum = NetworkPlayerStateEnum.OTHER;
		drawSprite = CharacterSpriteEnum.THROW_BOMBE_DOWN;
		switch (this.direction) {
		case center:
			if (previousDirection == PovDirection.west) {
				drawSprite = CharacterSpriteEnum.THROW_BOMBE_LEFT;
			} else if (previousDirection == PovDirection.north) {
				drawSprite = CharacterSpriteEnum.THROW_BOMBE_UP;
			} else if (previousDirection == PovDirection.east) {
				drawSprite = CharacterSpriteEnum.THROW_BOMBE_RIGHT;
			} else if (previousDirection == PovDirection.south) {
				drawSprite = CharacterSpriteEnum.THROW_BOMBE_DOWN;
			}
			break;
		case east:
			drawSprite = CharacterSpriteEnum.THROW_BOMBE_RIGHT;
			break;
		case north:
			drawSprite = CharacterSpriteEnum.THROW_BOMBE_UP;
			break;
		case south:
			drawSprite = CharacterSpriteEnum.THROW_BOMBE_DOWN;
			break;
		case west:
			drawSprite = CharacterSpriteEnum.THROW_BOMBE_LEFT;
			break;
		case northEast:
		case northWest:
		case southEast:
		case southWest:
		default:
			break;
		}
		this.spriteIndex = animations.get(drawSprite)
				.getKeyFrameIndex(this.direction == PovDirection.center ? 0 : animationTime);
		mbGame.getBatch().draw(
				animations.get(drawSprite).getKeyFrame(this.direction == PovDirection.center ? 0 : animationTime, true),
				getPixelX() - 15f, getPixelY() - 5f);
		if (animations.get(drawSprite).isAnimationFinished(animationTime)) {
			changeState(
					this.previousState == PlayerStateEnum.THROW_BOMBE ? PlayerStateEnum.NORMAL : this.previousState);
		}
	}

	private void drawInsideTrolley() {
		this.drawSprite = CharacterSpriteEnum.INSIDE_TROLLEY;
		this.networkStateEnum = NetworkPlayerStateEnum.TROLLEY;
		this.spriteIndex = 0;

		switch (this.trolleyDirection) {
		case center:
			break;
		case east:
			this.spriteIndex = 0;
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(this.drawSprite, this.color, this.character,
					this.spriteIndex), getPixelX() - 15f, getPixelY() - 9f);
			break;
		case north:
			this.spriteIndex = 5;
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(this.drawSprite, this.color, this.character,
					this.spriteIndex), getPixelX() - 15f, getPixelY() - 9f);
			break;
		case northEast:
			this.spriteIndex = 7;
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(this.drawSprite, this.color, this.character,
					this.spriteIndex), getPixelX() - 15f, getPixelY() - 9f);
			break;
		case northWest:
			this.spriteIndex = 6;
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(this.drawSprite, this.color, this.character,
					this.spriteIndex), getPixelX() - 15f, getPixelY() - 9f);
			break;
		case south:
			this.spriteIndex = 1;
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(this.drawSprite, this.color, this.character,
					this.spriteIndex), getPixelX() - 15f, getPixelY() - 9f);
			break;
		case southEast:
			this.spriteIndex = 2;
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(this.drawSprite, this.color, this.character,
					this.spriteIndex), getPixelX() - 15f, getPixelY() - 9f);
			break;
		case southWest:
			this.spriteIndex = 3;
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(this.drawSprite, this.color, this.character,
					this.spriteIndex), getPixelX() - 15f, getPixelY() - 9f);
			break;
		case west:
			this.spriteIndex = 4;
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(this.drawSprite, this.color, this.character,
					this.spriteIndex), getPixelX() - 15f, getPixelY() - 9f);
			break;
		default:
			break;
		}
	}

	private void drawCarryBombe() {
		this.networkStateEnum = NetworkPlayerStateEnum.OTHER;
		drawSprite = CharacterSpriteEnum.CARRY_WALK_DOWN;
		switch (this.direction) {
		case center:
			if (previousDirection == PovDirection.west) {
				drawSprite = CharacterSpriteEnum.CARRY_WALK_LEFT;
			} else if (previousDirection == PovDirection.north) {
				drawSprite = CharacterSpriteEnum.CARRY_WALK_UP;
			} else if (previousDirection == PovDirection.east) {
				drawSprite = CharacterSpriteEnum.CARRY_WALK_RIGHT;
			} else if (previousDirection == PovDirection.south) {
				drawSprite = CharacterSpriteEnum.CARRY_WALK_DOWN;
			}
			break;
		case east:
			drawSprite = CharacterSpriteEnum.CARRY_WALK_RIGHT;
			break;
		case north:
			drawSprite = CharacterSpriteEnum.CARRY_WALK_UP;
			break;
		case south:
			drawSprite = CharacterSpriteEnum.CARRY_WALK_DOWN;
			break;
		case west:
			drawSprite = CharacterSpriteEnum.CARRY_WALK_LEFT;
			break;
		case northEast:
		case northWest:
		case southEast:
		case southWest:
		default:
			break;
		}

		this.spriteIndex = animations.get(drawSprite)
				.getKeyFrameIndex(this.direction == PovDirection.center ? 0 : animationTime);
		mbGame.getBatch().draw(
				animations.get(drawSprite).getKeyFrame(this.direction == PovDirection.center ? 0 : animationTime, true),
				getPixelX() - 15f, getPixelY() - 5f);
	}

	private void drawStateNormal() {
		this.networkStateEnum = NetworkPlayerStateEnum.OTHER;
		drawSprite = CharacterSpriteEnum.WALK_DOWN;
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
		this.spriteIndex = animations.get(drawSprite)
				.getKeyFrameIndex(this.direction == PovDirection.center ? 0 : animationTime);
		mbGame.getBatch().draw(
				animations.get(drawSprite).getKeyFrame(this.direction == PovDirection.center ? 0 : animationTime, true),
				getPixelX() - 15f, getPixelY() - 5f);
	}

	private void drawStateOnLouis() {
		drawSpriteLouis = LouisSpriteEnum.WALK_DOWN;
		drawSprite = CharacterSpriteEnum.ON_LOUIS_DOWN;
		this.networkStateEnum = NetworkPlayerStateEnum.LOUIS;
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
		this.spriteIndex = 0;
		this.spriteIndexLouis = animationsLouis.get(drawSpriteLouis)
				.getKeyFrameIndex(this.direction == PovDirection.center ? 0 : animationTime);
		if (this.direction == PovDirection.south || this.previousDirection == PovDirection.south) {
			mbGame.getBatch().draw(animations.get(drawSprite).getKeyFrame(spriteIndex, true), getPixelX() - 15f,
					getPixelY() - 5f);
			mbGame.getBatch()
					.draw(animationsLouis.get(drawSpriteLouis)
							.getKeyFrame(this.direction == PovDirection.center ? 0 : animationTime, true),
							getPixelX() - 15f, getPixelY() - 5f);
		} else {
			mbGame.getBatch()
					.draw(animationsLouis.get(drawSpriteLouis)
							.getKeyFrame(this.direction == PovDirection.center ? 0 : animationTime, true),
							getPixelX() - 15f, getPixelY() - 5f);
			mbGame.getBatch().draw(animations.get(drawSprite).getKeyFrame(spriteIndex, true), getPixelX() - 15f,
					getPixelY() - 5f);
		}
	}

	public TextureRegion getMiniatureHappy() {
		return SpriteService.getInstance().getSprite(CharacterSpriteEnum.SCORE_HAPPY, this.color, this.character, 0);
	}

	public TextureRegion getMiniatureCry() {
		return SpriteService.getInstance().getSprite(CharacterSpriteEnum.SCORE_CRY, this.color, this.character, 0);
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
		if (this.body != null && o.body != null) {
			if (this.body.getPosition().y < o.body.getPosition().y) {
				return 1;
			} else if (this.body.getPosition().y > o.body.getPosition().y) {
				return -1;
			}
		}
		return 0;
	}
}
