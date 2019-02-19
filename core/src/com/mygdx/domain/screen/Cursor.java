package com.mygdx.domain.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.service.SpriteService;

public class Cursor {
	private int x;
	private int y;
	private float time;
	private Animation<TextureRegion> animation;

	public Cursor(int x, int y) {
		this.x = x;
		this.y = y;
		this.time = 0.0f;
		this.animation = new Animation<>(1f / 10f,
				SpriteService.getInstance().getSpriteForAnimation(SpriteEnum.CURSOR));
	}

	public void updateCursorPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void draw(SpriteBatch batch) {
		time += Gdx.graphics.getDeltaTime();
		batch.draw(animation.getKeyFrame(time, true), x, y);
	}
}
