package com.mygdx.domain.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.constante.CollisionConstante;
import com.mygdx.constante.Constante;
import com.mygdx.domain.common.BodyAble;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.SpriteService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuddenDeathWall extends BodyAble {

	private static final float ANIMATION_TIME = 1f;

	protected int x;
	protected int y;
	private SpriteEnum animation;
	private int defaultTexture;
	private float time;
	private boolean transformedInStrandardWall;

	@Override
	public void drawIt() {
		if (this.time <= ANIMATION_TIME) {
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(this.animation, this.defaultTexture),
					this.x * Constante.GRID_PIXELS_SIZE_X, (this.y * Constante.GRID_PIXELS_SIZE_Y) + this.getOffset());
		} else {
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(this.animation, this.defaultTexture),
					this.x * Constante.GRID_PIXELS_SIZE_X, this.y * Constante.GRID_PIXELS_SIZE_Y);
		}
	}

	public float getOffset() {
		return (-336.0f * this.time) + 336f;
	}

	@Override
	protected void createBody() {
		if (this.level.getOccupedWallBrickBonus()[x][y] != null) {
			this.level.getOccupedWallBrickBonus()[x][y].immediateAction();
		}
		BodyDef groundBodyDef = new BodyDef();
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(0.5f, 0.5f);
		groundBodyDef.position.set(new Vector2((float) this.x + 0.5f, (float) this.y + 0.5f));
		this.body = world.createBody(groundBodyDef);
		Fixture fixture = body.createFixture(groundBox, 0.0f);
		fixture.setFriction(0f);
		fixture.setUserData(this);
		Filter filter = new Filter();
		filter.categoryBits = CollisionConstante.CATEGORY_SUDDEN_DEATH_WALL;
		filter.maskBits = CollisionConstante.CATEGORY_PLAYER;
		fixture.setFilterData(filter);
	}

	public SuddenDeathWall(World world, MultiBombermanGame mbGame, Level level, int x, int y, SpriteEnum animation,
			int defaultTexture) {
		this.world = world;
		this.mbGame = mbGame;
		this.level = level;
		this.x = x;
		this.y = y;
		this.animation = animation;
		this.defaultTexture = defaultTexture;
		this.time = 0f;
		this.transformedInStrandardWall = false;
	}

	public boolean hasBody() {
		return this.body != null;
	}

	@Override
	public void update() {
		this.time += Gdx.graphics.getDeltaTime();
		if (this.time >= ANIMATION_TIME) {
			if (this.body != null && !transformedInStrandardWall) {
				this.changeFilter();
			}
			if (this.body == null) {
				this.createBody();
			}
		}
	}

	private void changeFilter() {
		Filter f = new Filter();
		f.categoryBits = CollisionConstante.CATEGORY_WALL;
		for (int i = 0; i < this.body.getFixtureList().size; i++) {
			this.body.getFixtureList().get(i).setFilterData(f);
		}
		transformedInStrandardWall = true;
	}

	@Override
	public SpriteEnum getDrawSprite() {
		return this.animation;
	}

	@Override
	public int getDrawIndex() {
		return this.defaultTexture;
	}
}
