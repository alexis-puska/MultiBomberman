package com.mygdx.ia;

import com.badlogic.gdx.Gdx;

public class CountDown {

	private float countDown;

	public CountDown(float countDown) {
		this.countDown = countDown + 1.0f;
		Gdx.app.log("COUNTDOWN", "create time : " + this.countDown);
	}

	public boolean update() {
		this.countDown -= Gdx.graphics.getDeltaTime();
		Gdx.app.log("COUNTDOWN", "time : " + this.countDown);
		return this.countDown < 0.0f;
	}
}
