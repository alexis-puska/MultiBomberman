package com.mygdx.domain.level;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.constante.CollisionConstante;
import com.mygdx.domain.common.BodyAble;
import com.mygdx.main.MultiBombermanGame;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trolley extends BodyAble implements Initiable {

	private static final float RADIUS = 0.48f;
	protected int x;
	protected int y;

	@Override
	public void createBody() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set((float) this.x + 0.5f, (float) this.y + 0.5f);
		this.body = world.createBody(bodyDef);
		this.body.setFixedRotation(false);
		MassData data = new MassData();
		data.mass = 10000f;
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
		filter.categoryBits = CollisionConstante.CATEGORY_TROLLEY;
		filter.maskBits = CollisionConstante.GROUP_TROLLEY;
		fixture.setFilterData(filter);
		diamondBody.dispose();
	}

	public void update() {
	}

	@Override
	public void init(World world, MultiBombermanGame mbGame, Level level) {
		this.mbGame = mbGame;
		this.world = world;
		this.level = level;
		createBody();
	}

	@Override
	public void drawIt() {
		// make
	}

	public boolean isDestroyed() {
		return false;
	}

	public boolean isMoving() {
		
		return false;
	}

	public void trolleyCollision() {
		// destroy the trolley, two trolley hurt each other and the two trolley move !
	}
}
