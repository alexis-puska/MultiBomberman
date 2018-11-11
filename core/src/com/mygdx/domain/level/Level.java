package com.mygdx.domain.level;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.constante.Constante;
import com.mygdx.domain.enumeration.BonusTypeEnum;
import com.mygdx.domain.enumeration.BrickStateEnum;
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

	private LevelElement[][] occupedWallBrick;
	private List<Integer> freeForBonus;
	private List<Brick> bricks;
	private List<Bombe> bombes;
	private List<Fire> fires;
	private List<Bonus> bonuss;

	private MultiBombermanGame mbGame;
	private World world;

	public void init(Game game, World world) {
		this.mbGame = game.getMultiBombermanGame();
		this.world = world;
		boolean[][] reservedStartPlayer = new boolean[Constante.GRID_SIZE_X][Constante.GRID_SIZE_Y];
		this.occupedWallBrick = new LevelElement[Constante.GRID_SIZE_X][Constante.GRID_SIZE_Y];
		for (int x = 0; x < Constante.GRID_SIZE_X; x++) {
			for (int y = 0; y < Constante.GRID_SIZE_Y; y++) {
				reservedStartPlayer[x][y] = false;
				occupedWallBrick[x][y] = null;
			}
		}
		this.bricks = new ArrayList<>();
		this.freeForBonus = new ArrayList<>();
		this.fires = new ArrayList<>();
		this.bombes = new ArrayList<>();
		this.bonuss = new ArrayList<>();
		hole.stream().forEach(t -> t.init(world, game.getMultiBombermanGame(), this));
		rail.stream().forEach(t -> t.init(game.getMultiBombermanGame(), this));
		interrupter.stream().forEach(t -> t.init(world, game.getMultiBombermanGame(), this));
		mine.stream().forEach(t -> t.init(world, game.getMultiBombermanGame(), this));
		trolley.stream().forEach(t -> t.init(world, game.getMultiBombermanGame(), this));
		teleporter.stream().forEach(w -> w.init(world, game.getMultiBombermanGame(), this));
		wall.stream().forEach(w -> {
			w.init(world, game.getMultiBombermanGame(), this);
			occupedWallBrick[w.getX()][w.getY()] = w;
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
		initBonus(game, world);
	}

	private void initBonus(Game game, World world) {
		for (int i = 0; i < bonus.length; i++) {
			for (int j = 0; j < bonus[i]; j++) {
				int idx = ThreadLocalRandom.current().nextInt(0, freeForBonus.size());
				int chx = freeForBonus.get(idx);
				int x = chx % Constante.GRID_SIZE_X;
				int y = (chx - x) / Constante.GRID_SIZE_X;
				bonuss.add(new Bonus(this, world, mbGame, x, y, BonusTypeEnum.BOMBE));
				Gdx.app.log("BONUS", "created at : " + x + " " + y);
				freeForBonus.remove(idx);
			}
		}
	}

	private void initBricks(Game game, World world, boolean[][] reservedStartPlayer) {
		for (int x = 0; x < Constante.GRID_SIZE_X; x++) {
			for (int y = 0; y < Constante.GRID_SIZE_Y; y++) {
				if (occupedWallBrick[x][y] == null && !reservedStartPlayer[x][y]
						&& ThreadLocalRandom.current().nextInt(0, 50) > 5) {
					Brick b = new Brick(world, game.getMultiBombermanGame(), this, this.defaultBrickAnimation, x, y);
					bricks.add(b);
					occupedWallBrick[x][y] = b;
					freeForBonus.add(y * Constante.GRID_SIZE_X + x);
				}
			}
		}
	}

	public void update() {
		bombes.stream().filter(b -> !b.isExploded()).forEach(Bombe::update);
		bricks.stream().forEach(Brick::update);
		fires.stream().forEach(Fire::update);
		mine.stream().forEach(Mine::update);
		teleporter.stream().forEach(Teleporter::update);
		trolley.stream().forEach(Trolley::update);
		wall.stream().filter(w -> !w.isInit()).forEach(w -> {
			w.init(this.world, mbGame, this);
			occupedWallBrick[w.getX()][w.getY()] = w;
		});
	}

	public void cleanUp() {
		bombes.removeIf(Bombe::isExploded);
		bricks.removeIf(b -> b.getState() == BrickStateEnum.BURNED);
		fires.removeIf(Fire::isOff);
		trolley.stream().forEach(Trolley::isDestroyed);
	}

	public void dispose() {
		for (int x = 0; x < Constante.GRID_SIZE_X; x++) {
			for (int y = 0; y < Constante.GRID_SIZE_Y; y++) {
				occupedWallBrick[x][y] = null;
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
	}
}
