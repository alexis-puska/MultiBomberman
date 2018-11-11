package com.mygdx.domain.common;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.domain.level.Level;
import com.mygdx.main.MultiBombermanGame;

public abstract class BodyAble {

	protected MultiBombermanGame mbGame;
	protected World world;
	protected Body body;
	protected Level level;

	public void dispose() {
		if (body != null) {
			this.world.destroyBody(body);
			body = null;
		}
		this.level = null;
	}

	protected abstract void createBody();

	public void update() {

	}

	public abstract void drawIt();
	
	public Vector2 getPosition() {
		return body.getPosition();
	}
	
	public float getBodyX() {
		return this.body.getPosition().x;
	}
	
	public float getBodyY() {
		return this.body.getPosition().y;
	}

}
