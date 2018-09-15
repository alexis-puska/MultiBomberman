package com.mygdx.service.input_processor;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.service.PlayerService;

public class ControllerAdapter implements ControllerListener {

	private PlayerService playerService;
	private Array<Controller> controllers;

	public ControllerAdapter(final Array<Controller> controllers, PlayerService playerService) {
		this.controllers = controllers;
		this.playerService = playerService;
	}

	@Override
	public void connected(Controller controller) {
	}

	@Override
	public void disconnected(Controller controller) {
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		switch (buttonCode) {
		case 4:
		case 5:
		case 6:
		case 7:
			playerService.speedUp(controller);
			break;
		case 0:
			if (controller == controllers.first()) {
				playerService.pressValide(controller);
			}
			playerService.dropBombe(controller);
			break;
		case 1:
			if (controller == controllers.first()) {
				playerService.pressValide(controller);
			}
			playerService.throwBombe(controller);
			break;
		case 2:
			break;
		case 8:
			playerService.pressSelect(controller);
			break;
		case 9:
			playerService.pressStart(controller);
			break;
		}
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		switch (buttonCode) {
		case 4:
		case 5:
		case 6:
		case 7:
			playerService.speedDown(controller);
		}
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		return false;
	}

	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		playerService.move(controller, value);
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
}
