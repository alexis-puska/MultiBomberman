package com.mygdx.domain.common;

import com.mygdx.main.MultiBombermanGame;

public abstract class Drawable {

	protected MultiBombermanGame mbGame;

	public void init(MultiBombermanGame mbGame) {
		this.mbGame = mbGame;
	}

	public abstract void drawIt();
}
