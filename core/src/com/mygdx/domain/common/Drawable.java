package com.mygdx.domain.common;

import com.mygdx.game.MultiBombermanGame;

public abstract class Drawable extends Enableable {

	protected MultiBombermanGame game;

	public void init(MultiBombermanGame game) {
		this.game = game;
	}

	public abstract void drawIt();
}
