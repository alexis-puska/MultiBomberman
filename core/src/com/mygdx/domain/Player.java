package com.mygdx.domain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.PovDirection;
import com.mygdx.service.input_processor.ControlEventListener;

public class Player implements ControlEventListener {

	private static final String CLASS_NAME = "Player.class";

	@Override
	public void move(PovDirection value) {
		//unused method
		Gdx.app.debug(CLASS_NAME, "press move");
	}

	@Override
	public void dropBombe() {
		//unused method
	}

	@Override
	public void throwBombe() {
		//unused method
	}

	@Override
	public void speedUp() {
		//unused method
	}

	@Override
	public void speedDown() {
		//unused method
	}

}
