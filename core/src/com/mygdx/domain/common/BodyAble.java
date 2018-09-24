package com.mygdx.domain.common;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MultiBombermanGame;

public abstract class BodyAble extends Drawable {

	protected World world;
	protected Body body;

	public void init(World world, MultiBombermanGame game) {
		this.init(game);
		this.world = world;
		createBody();
	}

	public void dispose() {
		if (body != null) {
			this.world.destroyBody(body);
			body = null;
		}
	}

	public abstract void createBody();

	public void update() {
		if (enable && body == null) {
			createBody();
		}
		if (!enable && body != null) {
			dispose();
		}
	}

	@Override
	public abstract void drawIt();

}
