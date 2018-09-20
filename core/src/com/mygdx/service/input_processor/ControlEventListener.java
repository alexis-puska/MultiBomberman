package com.mygdx.service.input_processor;

import com.badlogic.gdx.controllers.PovDirection;

public interface ControlEventListener {
	public void move(PovDirection value);

	public void pressStart();

	public void pressSelect();

	public void pressA();

	public void pressB();

	public void pressX();

	public void pressY();

	public void pressL();

	public void pressR();

	public void releaseL();

	public void releaseR();

}
