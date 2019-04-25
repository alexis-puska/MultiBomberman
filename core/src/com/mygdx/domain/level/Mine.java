package com.mygdx.domain.level;

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
import com.mygdx.enumeration.SoundEnum;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.SoundService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mine extends BodyAble implements Initiable {

	protected int id;
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
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(0.4f, 0.4f);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = groundBox;
		fixtureDef.density = 0;
		fixtureDef.restitution = 0f;
		Fixture fixture = body.createFixture(fixtureDef);
		fixture.setFriction(0f);
		fixture.setUserData(this);
		Filter filter = new Filter();
		filter.categoryBits = CollisionConstante.CATEGORY_MINE;
		filter.maskBits = CollisionConstante.GROUP_MINE;
		fixture.setFilterData(filter);
		groundBox.dispose();

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

	}

	@Override
	public void dispose() {
		super.dispose();
		SoundService.getInstance().stopSound(SoundEnum.MINE, id);
	}

	public void start() {
		SoundService.getInstance().loopSound(SoundEnum.MINE);
	}

}
