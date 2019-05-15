package com.mygdx.domain.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.constante.CollisionConstante;
import com.mygdx.constante.Constante;
import com.mygdx.domain.Player;
import com.mygdx.domain.common.BodyAble;
import com.mygdx.domain.enumeration.FireEnum;
import com.mygdx.domain.level.Level;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.game.Game;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.SpriteService;

public class Fire extends BodyAble {
	private Game game;
	protected int x;
	protected int y;
	private Player player;
	private FireEnum fireEnum;
	private boolean off;
	private Animation<TextureRegion> animation;
	private float time;

	public Fire(Game game, World world, MultiBombermanGame mbGame, Level level, Player player, int x, int y,
			FireEnum fireEnum) {
		this.game = game;
		this.world = world;
		this.mbGame = mbGame;
		this.level = level;
		this.player = player;
		this.x = x;
		this.y = y;
		this.fireEnum = fireEnum;
		this.animation = new Animation<>(fireEnum.getSpriteEnum().getFrameAnimationTime(),
				SpriteService.getInstance().getSpriteForAnimation(fireEnum.getSpriteEnum()));
		this.time = 0.0f;
		this.drawSprite = fireEnum.getSpriteEnum();
		this.drawIndex = 0;
		this.createBody();
		this.game.fireAppared(this.fireEnum, this.getGridIndex());
	}

	@Override
	public void createBody() {
		BodyDef groundBodyDef = new BodyDef();
		PolygonShape groundBox = new PolygonShape();
		switch (fireEnum) {
		case FIRE_DOWN_EXT:
			groundBox.setAsBox(0.4f, 0.45f);
			groundBodyDef.position.set(new Vector2((float) this.x + 0.5f, (float) this.y + 0.5f));
			break;
		case FIRE_LEFT:
		case FIRE_RIGHT:
			groundBox.setAsBox(0.5f, 0.4f);
			groundBodyDef.position.set(new Vector2((float) this.x + 0.5f, (float) this.y + 0.5f));
			break;
		case FIRE_LEFT_EXT:
			groundBox.setAsBox(0.45f, 0.4f);
			groundBodyDef.position.set(new Vector2((float) this.x + 0.55f, (float) this.y + 0.5f));
			break;
		case FIRE_RIGHT_EXT:
			groundBox.setAsBox(0.45f, 0.4f);
			groundBodyDef.position.set(new Vector2((float) this.x + 0.5f, (float) this.y + 0.45f));
			break;
		case FIRE_UP:
		case FIRE_DOWN:
			groundBox.setAsBox(0.4f, 0.5f);
			groundBodyDef.position.set(new Vector2((float) this.x + 0.5f, (float) this.y + 0.5f));
			break;
		case FIRE_UP_EXT:
			groundBox.setAsBox(0.4f, 0.45f);
			groundBodyDef.position.set(new Vector2((float) this.x + 0.5f, (float) this.y + 0.45f));
			break;
		case FIRE_CENTER:
		default:
			groundBox.setAsBox(0.5f, 0.5f);
			groundBodyDef.position.set(new Vector2((float) this.x + 0.5f, (float) this.y + 0.5f));
			break;
		}
		groundBodyDef.type = BodyType.DynamicBody;
		this.body = world.createBody(groundBodyDef);
		Fixture fixture = body.createFixture(groundBox, 0.0f);
		fixture.setFriction(0f);
		fixture.setUserData(this);
		Filter filter = new Filter();
		filter.categoryBits = CollisionConstante.CATEGORY_FIRE;
		filter.maskBits = CollisionConstante.CATEGORY_PLAYER_HITBOX | CollisionConstante.CATEGORY_BOMBE;
		fixture.setFilterData(filter);
	}

	@Override
	public void drawIt() {
		time += Gdx.graphics.getDeltaTime();
		this.drawIndex = animation.getKeyFrameIndex(time%animation.getAnimationDuration());
		mbGame.getBatch().draw(animation.getKeyFrame(time, false), (float) this.x * Constante.GRID_PIXELS_SIZE_X,
				(float) this.y * Constante.GRID_PIXELS_SIZE_Y);
	}

	@Override
	public void update() {
		if (this.animation.isAnimationFinished(time)) {
			this.dispose();
			off = true;
		}
	}

	public boolean isOff() {
		return off;
	}

	public Player getPlayerGenerateThisFire() {
		return this.player;
	}

	@Override
	public SpriteEnum getDrawSprite() {
		return drawSprite;
	}

	@Override
	public int getDrawIndex() {
		// TODO Auto-generated method stub
		return drawIndex;
	}
}
