package com.mygdx.domain.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.constante.CollisionConstante;
import com.mygdx.constante.Constante;
import com.mygdx.domain.common.BodyAble;
import com.mygdx.domain.enumeration.BrickStateEnum;
import com.mygdx.domain.level.Level;
import com.mygdx.domain.level.LevelElement;
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
public class Brick extends BodyAble implements LevelElement {
	private static final float ANIMATION_TIME = 0.16f;
	protected int x;
	protected int y;
	private SpriteEnum animation;

	private BrickStateEnum state;
	private float animationTime;
	private SpriteEnum defaultAnimation;

	private Animation<TextureRegion> burnAnimation;

	public Brick(final World world, final MultiBombermanGame mbGame, final Level level, SpriteEnum animation, int x,
			int y) {
		this.x = x;
		this.y = y;
		this.mbGame = mbGame;
		this.world = world;
		this.level = level;
		this.animation = animation;
		this.state = BrickStateEnum.CREATED;
		TextureRegion[] sprite = SpriteService.getInstance().getSpriteForAnimation(animation);
		TextureRegion[] spriteAnimation = new TextureRegion[sprite.length - 1];
		for (int i = 1; i < sprite.length; i++) {
			spriteAnimation[i - 1] = sprite[i];
		}
		burnAnimation = new Animation<>(ANIMATION_TIME, spriteAnimation);
		createBody();
	}

	@Override
	public void drawIt() {
		if (this.state == BrickStateEnum.CREATED) {
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(this.animation, 0),
					this.x * Constante.GRID_PIXELS_SIZE_X, this.y * Constante.GRID_PIXELS_SIZE_Y);
		} else if (this.state == BrickStateEnum.BURN) {
			mbGame.getBatch().draw(burnAnimation.getKeyFrame(animationTime, false),
					this.x * Constante.GRID_PIXELS_SIZE_X, this.y * Constante.GRID_PIXELS_SIZE_Y);
		}
	}

	@Override
	protected void createBody() {
		BodyDef groundBodyDef = new BodyDef();
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(0.5f, 0.5f);
		groundBodyDef.position.set(new Vector2((float) this.x + 0.5f, (float) this.y + 0.5f));
		this.body = world.createBody(groundBodyDef);
		Fixture fixture = body.createFixture(groundBox, 0.0f);
		fixture.setFriction(0f);
		fixture.setUserData(this);
		Filter filter = new Filter();
		filter.categoryBits = CollisionConstante.CATEGORY_BRICKS;
		fixture.setFilterData(filter);
	}

	@Override
	public void action() {
		if (state == BrickStateEnum.CREATED) {
			this.state = BrickStateEnum.BURN;
		}
	}

	@Override
	public void update() {
		if (this.state == BrickStateEnum.BURN) {
			animationTime += Gdx.graphics.getDeltaTime();
			if (burnAnimation.isAnimationFinished(animationTime)) {
				this.state = BrickStateEnum.BURNED;
				this.level.getOccupedWallBrickBonus()[this.getX()][this.getY()] = null;
				this.level.getBonuss().stream().filter(b -> b.x == this.x && b.y == this.y).forEach(Bonus::revealBonus);
				dispose();
			}
		}
	}

	@Override
	public void immediateAction() {
		this.state = BrickStateEnum.BURNED;
		this.level.getOccupedWallBrickBonus()[this.getX()][this.getY()] = null;
		dispose();
	}
}
