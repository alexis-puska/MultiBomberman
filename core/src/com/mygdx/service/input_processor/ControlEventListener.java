package com.mygdx.service.input_processor;

import com.badlogic.gdx.controllers.PovDirection;

public interface ControlEventListener {
	public void move(PovDirection value);

	public void dropBombe();

	public void throwBombe();

	public void speedUp();

	public void speedDown();
}
