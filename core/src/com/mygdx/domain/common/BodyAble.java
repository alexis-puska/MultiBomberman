package com.mygdx.domain.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.domain.level.Level;
import com.mygdx.main.MultiBombermanGame;

public abstract class BodyAble extends Drawable {

	protected World world;
	protected Body body;
	protected Level level;

	public void init(World world, MultiBombermanGame mbGame) {
		this.init(mbGame);
		this.world = world;
		createBody();
	}

	public void dispose() {
		if (body != null) {
			Gdx.app.log("dispose", "destroy");
			this.world.destroyBody(body);
			body = null;
		}
	}

	public abstract void createBody();

	public void update() {

	}

	@Override
	public abstract void drawIt();

}
