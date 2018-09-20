package com.mygdx.domain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.PovDirection;
import com.mygdx.service.input_processor.ControlEventListener;

public class Player implements ControlEventListener {

	private static final String CLASS_NAME = "Player.class";

	@Override
	public void move(PovDirection value) {
		// unused method
		Gdx.app.debug(CLASS_NAME, "press move");
	}

	@Override
	public void pressStart() {
		// unused method

	}

	@Override
	public void pressSelect() {
		// unused method

	}

	@Override
	public void pressA() {
		// unused method

	}

	@Override
	public void pressB() {
		// unused method

	}

	@Override
	public void pressX() {
		// unused method

	}

	@Override
	public void pressY() {
		// unused method

	}

	@Override
	public void pressL() {
		// unused method

	}

	@Override
	public void pressR() {
		// unused method

	}

	@Override
	public void releaseL() {
		// unused method

	}

	@Override
	public void releaseR() {
		// unused method

	}

}
