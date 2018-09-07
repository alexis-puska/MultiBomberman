package com.mygdx.service.input_processor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

import lombok.Getter;

@Getter
public class ControllerAdapter implements ControllerListener {

	private MenuListener menuListener;
	private Controller controller;
	private final boolean first;

	public ControllerAdapter(final Controller controller, boolean first) {
		this.controller = controller;
		this.first = first;
		this.controller.addListener(this);
	}

	public void changeMenuListeners(MenuListener listener) {
		Gdx.app.log("Controller adapter ", "class : " + listener.getClass().getName());
		this.menuListener = listener;
	}

	@Override
	public void connected(Controller controller) {

	}

	@Override
	public void disconnected(Controller controller) {

	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		if (this.menuListener != null) {

			if (this.controller != null && this.controller.equals(controller)) {
				switch (buttonCode) {
				case 0:
					menuListener.pressValide();
					break;
				case 1:
					menuListener.pressValide();
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					break;
				case 5:
					break;
				case 6:
					break;
				case 7:
					break;
				case 8:
					menuListener.pressSelect();
					break;
				case 9:
					menuListener.pressStart();
					break;
				case 10:
					break;
				case 11:
					break;
				case 12:
					break;
				}
			}
		}
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		return false;
	}

	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		if (this.menuListener != null) {
			if (this.controller != null && this.controller.equals(controller)) {
				switch (value) {
				case center:
					break;
				case east:
					menuListener.pressRight();
					break;
				case north:
					menuListener.pressUp();
					break;
				case northEast:
					menuListener.pressUp();
					menuListener.pressRight();
					break;
				case northWest:
					menuListener.pressUp();
					menuListener.pressLeft();
					break;
				case south:
					menuListener.pressDown();
					break;
				case southEast:
					menuListener.pressDown();
					menuListener.pressRight();
					break;
				case southWest:
					menuListener.pressDown();
					menuListener.pressLeft();
					break;
				case west:
					menuListener.pressLeft();
					break;
				default:
					break;
				}
			}
		}
		return false;
	}

	@Override
	public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
		return false;
	}

	@Override
	public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
		return false;
	}

	@Override
	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
		return false;
	}

	public void setController(final Controller controller) {
		this.controller = controller;
	}

}
