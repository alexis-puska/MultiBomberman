package com.mygdx.domain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.PovDirection;
import com.mygdx.service.input_processor.ControlEventListener;

public class Player implements ControlEventListener {

	private static final String CLASS_NAME = "Player.class";

	@Override
	public void move(PovDirection value) {
		// TODO Auto-generated method stub
		Gdx.app.debug(CLASS_NAME, "press move");
	}

	@Override
	public void dropBombe() {
		// TODO Auto-generated method stub

	}

	@Override
	public void throwBombe() {
		// TODO Auto-generated method stub

	}

	@Override
	public void speedUp() {
		// TODO Auto-generated method stub

	}

	@Override
	public void speedDown() {
		// TODO Auto-generated method stub

	}

}
