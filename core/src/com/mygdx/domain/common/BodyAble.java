package com.mygdx.domain.common;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.constante.Constante;
import com.mygdx.domain.level.Level;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.main.MultiBombermanGame;

public abstract class BodyAble {

	protected MultiBombermanGame mbGame;
	protected World world;
	protected Body body;
	protected Level level;

	protected SpriteEnum drawSprite;
	protected int drawIndex;

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

	public abstract SpriteEnum getDrawSprite();

	public abstract int getDrawIndex();

	public Vector2 getPosition() {
		return body.getPosition();
	}

	public float getBodyX() {
		return this.body.getPosition().x;
	}

	public float getBodyY() {
		return this.body.getPosition().y;
	}

	public float getPixelX() {
		return (body.getPosition().x * Constante.GRID_PIXELS_SIZE_X);
	}

	public float getPixelY() {
		return (body.getPosition().y * Constante.GRID_PIXELS_SIZE_Y);
	}

	public int getGridIndex() {
		if (body != null) {
			return (int) (Math.floor(body.getPosition().y)) * Constante.GRID_SIZE_X
					+ ((int) (Math.floor(this.body.getPosition().x)));
		}
		return -1;
	}
}
