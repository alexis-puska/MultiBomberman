package com.mygdx.service.network.server;

import com.badlogic.gdx.Gdx;

public class HeartBeatService {

	private static final String CLASS_NAME = "HeartBeatService.class";

	private HeartBeat heart;

	public void start() {
		Gdx.app.log(CLASS_NAME, "Start hearthbeat");
		heart = new HeartBeat();
		heart.start();
	}

	public void stop() {
		if (heart != null) {
			heart.kill();
		}
	}
}
