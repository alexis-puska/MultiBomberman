package com.mygdx.domain.common;

import com.mygdx.main.MultiBombermanGame;

public abstract class Drawable extends Identifiable {

	protected MultiBombermanGame game;

	public void init(MultiBombermanGame game) {
		this.game = game;
	}

	public abstract void drawIt();
}
