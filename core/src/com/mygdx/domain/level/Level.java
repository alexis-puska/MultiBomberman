package com.mygdx.domain.level;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.constante.Constante;
import com.mygdx.domain.enumeration.BrickStateEnum;
import com.mygdx.domain.game.Bombe;
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

	private boolean[][] reservedStartPlayer;
	private LevelElement[][] occupedWallBrick;
	private List<Integer> free;
	private List<Brick> bricks;
	private List<Bombe> bombes;
	private List<Fire> fires;

	private MultiBombermanGame mbGame;
	private World world;

	public void init(Game game, World world) {
		this.mbGame = game.getMultiBombermanGame();
		this.world = world;
		this.reservedStartPlayer = new boolean[Constante.GRID_SIZE_X][Constante.GRID_SIZE_Y];
		this.occupedWallBrick = new LevelElement[Constante.GRID_SIZE_X][Constante.GRID_SIZE_Y];
		for (int x = 0; x < Constante.GRID_SIZE_X; x++) {
			for (int y = 0; y < Constante.GRID_SIZE_Y; y++) {
				reservedStartPlayer[x][y] = false;
				occupedWallBrick[x][y] = null;
			}
		}
		this.bricks = new ArrayList<>();
		this.fires = new ArrayList<>();
		this.bombes = new ArrayList<>();
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
			for (int x = 0; x < Constante.GRID_SIZE_X; x++) {
				for (int y = 0; y < Constante.GRID_SIZE_Y; y++) {
					if (occupedWallBrick[x][y] == null && !reservedStartPlayer[x][y]
							&& ThreadLocalRandom.current().nextInt(0, 50) > 5) {
						Brick b = new Brick(world, game.getMultiBombermanGame(), this, this.defaultBrickAnimation, x, y);
						bricks.add(b);
						occupedWallBrick[x][y] = b;
					}
				}
			}
		}
	}

	public void update() {
		bombes.stream().forEach(Bombe::update);
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
}
