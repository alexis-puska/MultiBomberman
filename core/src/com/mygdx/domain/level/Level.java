package com.mygdx.domain.level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.constante.CollisionConstante;
import com.mygdx.constante.Constante;
import com.mygdx.domain.enumeration.BonusStateEnum;
import com.mygdx.domain.enumeration.BonusTypeEnum;
import com.mygdx.domain.enumeration.BrickStateEnum;
import com.mygdx.domain.enumeration.SuddenDeathDirection;
import com.mygdx.domain.game.Bombe;
import com.mygdx.domain.game.Bonus;
import com.mygdx.domain.game.Brick;
import com.mygdx.domain.game.Fire;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.game.Game;
import com.mygdx.main.MultiBombermanGame;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Setter
public class Level {
	private List<Text> name;
	private List<Text> description;
	private int bombe;
	private int strenght;
	private float shadow;
	private Integer[] bonus;
	private boolean fillWithBrick;
	private boolean footInWater;
	private boolean levelUnderWater;
	private DefaultTexture defaultBackground;
	private DefaultTexture defaultWall;
	private SpriteEnum defaultBrickAnimation;
	private List<Hole> hole;
	private List<Rail> rail;
	private List<Interrupter> interrupter;
	private List<Mine> mine;
	private List<Trolley> trolley;
	private List<Teleporter> teleporter;
	private List<Wall> wall;
	private List<CustomTexture> customBackgroundTexture;
	private List<CustomTexture> customForegroundTexture;
	private List<StartPlayer> startPlayer;

	private LevelElement[][] occupedWallBrickBonus;
	private List<Integer> noWall;
	private List<Integer> occupedByBrick;
	private List<Brick> bricks;
	private List<Bombe> bombes;
	private List<Fire> fires;
	private List<Bonus> bonuss;
	private Map<Integer, Short> state;
	private List<Body> badBomberWall;

	private MultiBombermanGame mbGame;
	private World world;

	/**********************
	 * --- SUDDEN DEATH ---
	 **********************/
	private int suddenDeathBlock;
	private SuddenDeathDirection suddenDeathDirection;
	private int suddenDeathMinX;
	private int suddenDeathMaxX;
	private int suddenDeathMinY;
	private int suddenDeathMaxY;
	private int suddenDeathX;
	private int suddenDeathY;
	private List<SuddenDeathWall> suddenDeathWall;

	public void init(Game game, World world) {
		this.mbGame = game.getMultiBombermanGame();
		this.world = world;
		boolean[][] reservedStartPlayer = new boolean[Constante.GRID_SIZE_X][Constante.GRID_SIZE_Y];
		this.occupedWallBrickBonus = new LevelElement[Constante.GRID_SIZE_X][Constante.GRID_SIZE_Y];
		this.occupedByBrick = new ArrayList<>();
		this.noWall = new ArrayList<>();
		for (int x = 0; x < Constante.GRID_SIZE_X; x++) {
			for (int y = 0; y < Constante.GRID_SIZE_Y; y++) {
				reservedStartPlayer[x][y] = false;
				occupedWallBrickBonus[x][y] = null;
				noWall.add(x + y * Constante.GRID_SIZE_X);
			}
		}
		this.bricks = new ArrayList<>();
		this.fires = new ArrayList<>();
		this.bombes = new ArrayList<>();
		this.bonuss = new ArrayList<>();
		this.state = new HashMap<>();
		hole.stream().forEach(t -> t.init(world, game.getMultiBombermanGame(), this));
		rail.stream().forEach(t -> t.init(game.getMultiBombermanGame(), this));
		interrupter.stream().forEach(t -> t.init(world, game.getMultiBombermanGame(), this));
		mine.stream().forEach(t -> t.init(world, game.getMultiBombermanGame(), this));
		trolley.stream().forEach(t -> t.init(world, game.getMultiBombermanGame(), this));
		teleporter.stream().forEach(w -> w.init(world, game.getMultiBombermanGame(), this));
		wall.stream().forEach(w -> {
			w.init(world, game.getMultiBombermanGame(), this);
			occupedWallBrickBonus[w.getX()][w.getY()] = w;
			noWall.remove(new Integer((int) w.getX() + ((int) w.getBodyY() * Constante.GRID_SIZE_X)));
		});
		startPlayer.stream().forEach(sp -> {
			reservedStartPlayer[sp.getX() - 1][sp.getY() - 1] = true;
			reservedStartPlayer[sp.getX()][sp.getY() - 1] = true;
			reservedStartPlayer[sp.getX() + 1][sp.getY() - 1] = true;
			reservedStartPlayer[sp.getX() - 1][sp.getY()] = true;
			reservedStartPlayer[sp.getX()][sp.getY()] = true;
			reservedStartPlayer[sp.getX() + 1][sp.getY()] = true;
			reservedStartPlayer[sp.getX() - 1][sp.getY() + 1] = true;
			reservedStartPlayer[sp.getX()][sp.getY() + 1] = true;
			reservedStartPlayer[sp.getX() + 1][sp.getY() + 1] = true;
		});
		if (fillWithBrick) {
			initBricks(game, world, reservedStartPlayer);
		}
		initBonus(world);
		initBadBomberWall(world);
		initSuddenDeath();
	}

	private void initBonus(World world) {
		if (isFillWithBrick()) {
			for (int i = 0; i < bonus.length; i++) {
				for (int j = 0; j < bonus[i]; j++) {
					int idx = ThreadLocalRandom.current().nextInt(0, occupedByBrick.size());
					int chx = occupedByBrick.get(idx);
					int x = chx % Constante.GRID_SIZE_X;
					int y = (chx - x) / Constante.GRID_SIZE_X;
					bonuss.add(new Bonus(this, world, mbGame, x, y, BonusTypeEnum.values()[i], BonusStateEnum.CREATED));
					occupedByBrick.remove(idx);
				}
			}
		}
	}

	private void initBadBomberWall(World world) {
		final float width = Constante.BAD_BOMBER_WALL_WIDTH;
		final float mult = Constante.BAD_BOMBER_WALL_MULT;
		final float sizeX = (float) Constante.GRID_SIZE_X - Constante.BAD_BOMBER_WALL_ZOOM;
		final float sizeY = (float) Constante.GRID_SIZE_Y - Constante.BAD_BOMBER_WALL_ZOOM;
		this.badBomberWall = new ArrayList<>();
		Filter filter = new Filter();
		filter.categoryBits = CollisionConstante.CATEGORY_RAIL_SPACESHIP;
		filter.maskBits = CollisionConstante.GROUP_RAIL_SPACESIP;
		// center
		BodyDef groundBodyDef = new BodyDef();
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(sizeX / 2f, sizeY / 2f);
		groundBodyDef.position.set(new Vector2((sizeX / 2f) + Constante.BAD_BOMBER_WALL_OFFSET,
				(sizeY / 2f) + Constante.BAD_BOMBER_WALL_OFFSET));
		Body body = world.createBody(groundBodyDef);
		body.setUserData(this);
		Fixture fixture = body.createFixture(groundBox, 0.0f);
		fixture.setFriction(0f);
		fixture.setUserData(this);
		fixture.setFilterData(filter);
		// north
		BodyDef groundBodyDefNorth = new BodyDef();
		PolygonShape groundBoxNorth = new PolygonShape();
		groundBoxNorth.setAsBox((sizeX / 2f) + ((mult + 1) * width), width);
		groundBodyDefNorth.position.set(new Vector2((sizeX / 2f) + Constante.BAD_BOMBER_WALL_OFFSET,
				(sizeY + (mult * width)) + Constante.BAD_BOMBER_WALL_OFFSET));
		Body bodyNorth = world.createBody(groundBodyDefNorth);
		bodyNorth.setUserData(this);
		Fixture fixtureNorth = bodyNorth.createFixture(groundBoxNorth, 0.0f);
		fixtureNorth.setFriction(0f);
		fixtureNorth.setUserData(this);
		fixtureNorth.setFilterData(filter);
		// west
		BodyDef groundBodyDefWest = new BodyDef();
		PolygonShape groundBoxWest = new PolygonShape();
		groundBoxWest.setAsBox(width, (sizeY / 2f) + ((mult - 1) * width));
		groundBodyDefWest.position.set(Constante.BAD_BOMBER_WALL_OFFSET - (mult * width),
				(sizeY / 2f) + Constante.BAD_BOMBER_WALL_OFFSET);
		Body bodyWest = world.createBody(groundBodyDefWest);
		bodyWest.setUserData(this);
		Fixture fixtureWest = bodyWest.createFixture(groundBoxWest, 0.0f);
		fixtureWest.setFriction(0f);
		fixtureWest.setUserData(this);
		fixtureWest.setFilterData(filter);
		// east
		BodyDef groundBodyDefEast = new BodyDef();
		PolygonShape groundBoxEast = new PolygonShape();
		groundBoxEast.setAsBox(width, (sizeY / 2f) + ((mult - 1) * width));
		groundBodyDefEast.position.set(new Vector2((sizeX + (mult * width)) + Constante.BAD_BOMBER_WALL_OFFSET,
				(sizeY / 2f) + Constante.BAD_BOMBER_WALL_OFFSET));
		Body bodyEast = world.createBody(groundBodyDefEast);
		bodyEast.setUserData(this);
		Fixture fixtureEast = bodyEast.createFixture(groundBoxEast, 0.0f);
		fixtureEast.setFriction(0f);
		fixtureEast.setUserData(this);
		fixtureEast.setFilterData(filter);
		// south
		BodyDef groundBodyDefSouth = new BodyDef();
		PolygonShape groundBoxSouth = new PolygonShape();
		groundBoxSouth.setAsBox((sizeX / 2f) + ((mult + 1) * width), width);
		groundBodyDefSouth.position.set(new Vector2((sizeX / 2f) + Constante.BAD_BOMBER_WALL_OFFSET,
				Constante.BAD_BOMBER_WALL_OFFSET - (mult * width)));
		Body bodySouth = world.createBody(groundBodyDefSouth);
		bodySouth.setUserData(this);
		Fixture fixtureSouth = bodySouth.createFixture(groundBoxSouth, 0.0f);
		fixtureSouth.setFriction(0f);
		fixtureSouth.setUserData(this);
		fixtureSouth.setFilterData(filter);
		badBomberWall.add(body);
		badBomberWall.add(bodyNorth);
		badBomberWall.add(bodyWest);
		badBomberWall.add(bodyEast);
		badBomberWall.add(bodySouth);
	}

	private void initSuddenDeath() {
		this.suddenDeathBlock = 0;
		this.suddenDeathDirection = SuddenDeathDirection.RIGHT;
		this.suddenDeathMinX = 0;
		this.suddenDeathMaxX = Constante.GRID_SIZE_X - 1;
		this.suddenDeathMinY = 0;
		this.suddenDeathMaxY = Constante.GRID_SIZE_Y - 1;
		this.suddenDeathX = 0;
		this.suddenDeathY = Constante.GRID_SIZE_Y - 1;
		this.suddenDeathWall = new ArrayList<>();
	}

	public void randomBonus() {
		if (!isFillWithBrick()) {
			for (int i = 0; i < bonus.length; i++) {
				for (int j = 0; j < bonus[i]; j++) {
					if (noWall.isEmpty()) {
						break;
					} else {
						int idx = ThreadLocalRandom.current().nextInt(0, noWall.size());
						int chx = noWall.get(idx);
						int x = chx % Constante.GRID_SIZE_X;
						int y = (chx - x) / Constante.GRID_SIZE_X;
						bonuss.add(new Bonus(this, world, mbGame, x, y, BonusTypeEnum.values()[i],
								BonusStateEnum.REVEALED));
						noWall.remove(idx);
					}
				}
			}
		}
	}

	private void initBricks(Game game, World world, boolean[][] reservedStartPlayer) {
		for (int x = 0; x < Constante.GRID_SIZE_X; x++) {
			for (int y = 0; y < Constante.GRID_SIZE_Y; y++) {
				if (occupedWallBrickBonus[x][y] == null && !reservedStartPlayer[x][y]
						&& ThreadLocalRandom.current().nextInt(0, 50) > 5) {
					Brick b = new Brick(world, game.getMultiBombermanGame(), this, this.defaultBrickAnimation, x, y);
					bricks.add(b);
					occupedWallBrickBonus[x][y] = b;
					occupedByBrick.add(y * Constante.GRID_SIZE_X + x);
					noWall.remove(new Integer(y * Constante.GRID_SIZE_X + x));
				}
			}
		}
	}

	public void update() {
		bombes.stream().filter(b -> !b.isExploded()).forEach(Bombe::update);
		bricks.stream().forEach(Brick::update);
		fires.stream().forEach(Fire::update);
		bonuss.stream().forEach(Bonus::update);
		mine.stream().forEach(Mine::update);
		teleporter.stream().forEach(Teleporter::update);
		trolley.stream().forEach(Trolley::update);
		wall.stream().filter(w -> !w.isInitialised()).forEach(w -> {
			w.init(this.world, mbGame, this);
			occupedWallBrickBonus[w.getX()][w.getY()] = w;
		});
		suddenDeathWall.stream().forEach(SuddenDeathWall::update);
		this.buildState();
	}

	private void buildState() {
		this.state = new HashMap<>();
		hole.stream().forEach(h -> updateValueInState(h.getGridIndex(), CollisionConstante.CATEGORY_HOLE));
		interrupter.stream().forEach(h -> updateValueInState(h.getGridIndex(), CollisionConstante.CATEGORY_BUTTONS));
		mine.stream().forEach(h -> updateValueInState(h.getGridIndex(), CollisionConstante.CATEGORY_MINE));
		trolley.stream().forEach(h -> updateValueInState(h.getGridIndex(), CollisionConstante.CATEGORY_TROLLEY));
		teleporter.stream().forEach(h -> updateValueInState(h.getGridIndex(), CollisionConstante.CATEGORY_TELEPORTER));
		wall.stream().forEach(h -> updateValueInState(h.getGridIndex(), CollisionConstante.CATEGORY_WALL));
		bricks.stream().forEach(h -> updateValueInState(h.getGridIndex(), CollisionConstante.CATEGORY_BRICKS));
	}

	public Map<Integer, Short> getState() {
		return this.state;
	}

	private void updateValueInState(int index, short val) {
		if (index != -1) {
			short value = val;
			if (this.state.containsKey(index)) {
				value = (short) (value | this.state.get(index));
			}
			this.state.put(index, value);
		}
	}

	public void cleanUp() {
		bombes.removeIf(Bombe::isExploded);
		bricks.removeIf(b -> b.getState() == BrickStateEnum.BURNED);
		fires.removeIf(Fire::isOff);
		trolley.stream().forEach(Trolley::isDestroyed);
		bonuss.removeIf(b -> b.isBurned() || b.idDisposed());
	}

	public void dispose() {
		for (int x = 0; x < Constante.GRID_SIZE_X; x++) {
			for (int y = 0; y < Constante.GRID_SIZE_Y; y++) {
				occupedWallBrickBonus[x][y] = null;
			}
		}

		hole.stream().forEach(Hole::dispose);
		interrupter.stream().forEach(Interrupter::dispose);
		mine.stream().forEach(Mine::dispose);
		trolley.stream().forEach(Trolley::dispose);
		teleporter.stream().forEach(Teleporter::dispose);
		wall.stream().forEach(Wall::dispose);
		bricks.stream().forEach(Brick::dispose);
		bombes.stream().forEach(Bombe::dispose);
		fires.stream().forEach(Fire::dispose);
		badBomberWall.stream().forEach(w -> this.world.destroyBody(w));
		bonuss.stream().forEach(Bonus::dispose);
		suddenDeathWall.stream().forEach(SuddenDeathWall::dispose);
	}

	public void doSuddenDeathThings(float gameCountDown) {
		float suddenDeathTime = Constante.SUDDEN_DEATH_TIME - gameCountDown;
		float generateEachTime = Constante.SUDDEN_DEATH_TIME / (float) (Constante.GRID_SIZE_X * Constante.GRID_SIZE_Y);
		int mustGenerated = Math.round(suddenDeathTime / generateEachTime);
		while (mustGenerated > suddenDeathBlock) {
			if (this.getOccupedWallBrickBonus()[suddenDeathX][suddenDeathY] == null
					|| (this.getOccupedWallBrickBonus()[suddenDeathX][suddenDeathY] != null
							&& !(this.getOccupedWallBrickBonus()[suddenDeathX][suddenDeathY] instanceof Wall))) {
				suddenDeathWall.add(new SuddenDeathWall(world, mbGame, this, suddenDeathX, suddenDeathY,
						this.getDefaultWall().getAnimation(), this.getDefaultWall().getIndex()));
			}
			switch (suddenDeathDirection) {
			case RIGHT:
				if (suddenDeathX < suddenDeathMaxX) {
					suddenDeathX++;
				} else {
					suddenDeathMaxY--;
					suddenDeathY--;
					suddenDeathDirection = SuddenDeathDirection.DOWN;
				}
				break;
			case DOWN:
				if (suddenDeathY > suddenDeathMinY) {
					suddenDeathY--;
				} else {
					suddenDeathMaxX--;
					suddenDeathX--;
					suddenDeathDirection = SuddenDeathDirection.LEFT;
				}
				break;
			case LEFT:
				if (suddenDeathX > suddenDeathMinX) {
					suddenDeathX--;
				} else {
					suddenDeathMinY++;
					suddenDeathY++;
					suddenDeathDirection = SuddenDeathDirection.UP;
				}
				break;
			case UP:
			default:
				if (suddenDeathY < suddenDeathMaxY) {
					suddenDeathY++;
				} else {
					suddenDeathMinX++;
					suddenDeathX++;
					suddenDeathDirection = SuddenDeathDirection.RIGHT;
				}
				break;
			}
			suddenDeathBlock++;
		}
	}
}
