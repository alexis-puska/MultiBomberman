package com.mygdx.domain.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.constante.CollisionConstante;
import com.mygdx.domain.common.BodyAble;
import com.mygdx.enumeration.SoundEnum;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.SoundService;
import com.mygdx.service.SpriteService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Hole extends BodyAble {

	protected int x;
	protected int y;

	private int walkCounter;
	private boolean wallCreated;

	public void init(final World world, final MultiBombermanGame mbGame, final Level level) {
		this.world = world;
		this.level = level;
		this.init(mbGame);
		this.wallCreated = false;
		this.createBody();
	}

	@Override
	public void createBody() {
		BodyDef groundBodyDef = new BodyDef();
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(0.4f, 0.4f);
		groundBodyDef.position.set(new Vector2((float) this.x + 0.5f, (float) this.y + 0.5f));
		this.body = world.createBody(groundBodyDef);
		Fixture fixture = body.createFixture(groundBox, 0.0f);
		fixture.setFriction(0f);
		fixture.setUserData(this);
		Filter filter = new Filter();
		filter.categoryBits = CollisionConstante.CATEGORY_HOLE;
		filter.maskBits = CollisionConstante.CATEGORY_PLAYER_HITBOX;
		fixture.setFilterData(filter);
	}

	public void walkOn() {
		Gdx.app.log("HOLE", " walk on");
		this.walkCounter++;
		switch (this.walkCounter) {
		case 1:
			SoundService.getInstance().playSound(SoundEnum.HOLE1);
			break;
		case 2:
			SoundService.getInstance().playSound(SoundEnum.HOLE2);
			break;
		case 3:
			SoundService.getInstance().playSound(SoundEnum.HOLE3);
			break;
		default:
		}
	}

	public void walkOff() {
		if (!wallCreated && walkCounter >= 3) {
			Wall wall = new Wall();
			wall.setX(this.x);
			wall.setY(this.y);
			wall.setDraw(false);
			this.level.getWall().add(wall);
			this.wallCreated = true;
		}
	}

	@Override
	public void drawIt() {
		if (walkCounter > 0 && walkCounter < 3) {
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.HOLE, 0), this.x * 18, this.y * 16);
		} else if (walkCounter >= 3) {
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.HOLE, 1), this.x * 18, this.y * 16);
		}
	}
}
