package com.mygdx.service.input_processor;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.domain.Player;

public class ControllerAdapter implements ControllerListener {

	private MenuListener menuListener;
	private Array<Controller> controllers;
	private Map<Controller, ControlEventListener> mapControl;

	public ControllerAdapter(final Array<Controller> controllers) {
		this.controllers = controllers;
		this.mapControl = new HashMap<>();
	}

	public void changeMenuListeners(MenuListener listener) {
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
			if (this.controllers != null) {
				if (this.controllers.first().equals(controller)) {
					switch (buttonCode) {
					case 0:
						menuListener.pressValide();
						break;
					case 1:
						menuListener.pressValide();
						break;
					case 8:
						menuListener.pressSelect();
						break;
					case 9:
						menuListener.pressStart();
						break;
					}
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
			if (this.controllers != null && this.controllers.first().equals(controller)) {
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
		if (mapControl.containsKey(controller)) {
			ControlEventListener listener = mapControl.get(controller);
			listener.move(value);
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

	public void addPlayer(Player player) {
		mapControl.put(controllers.first(), player);
	}

}
