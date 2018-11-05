package com.mygdx.domain.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.service.SpriteService;

public class Cursor {
	private int x;
	private int y;
	private int idx;

	public Cursor(int x, int y) {
		this.x = x;
		this.y = y;
		this.idx = 0;
	}
	
	public void updateCursorPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void draw(SpriteBatch batch) {
		batch.draw(SpriteService.getInstance().getSprite(SpriteEnum.CURSOR, idx), x, y);
		if (Gdx.graphics.getFrameId() % 2 == 0) {
			idx++;
			if (idx >= SpriteService.getInstance().getAnimationSize(SpriteEnum.CURSOR)) {
				idx = 0;
			}
		}
	}
}
