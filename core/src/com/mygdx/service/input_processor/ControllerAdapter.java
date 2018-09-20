package com.mygdx.service.input_processor;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.service.PlayerService;

public class ControllerAdapter implements ControllerListener {

	private PlayerService playerService;

	public ControllerAdapter(PlayerService playerService) {
		this.playerService = playerService;
	}

	@Override
	public void connected(Controller controller) {
		// Unused method
	}

	@Override
	public void disconnected(Controller controller) {
		// Unused method
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		switch (buttonCode) {
		case 6:
			playerService.pressL(controller);
			break;
		case 7:
			playerService.pressR(controller);
			break;
		case 0:
			playerService.pressA(controller);
			break;
		case 1:
			playerService.pressB(controller);
			break;
		case 2:
			playerService.pressX(controller);
			break;
		case 3:
			playerService.pressY(controller);
			break;
		case 8:
			playerService.pressSelect(controller);
			break;
		case 9:
			playerService.pressStart(controller);
			break;
		default:
		}
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		switch (buttonCode) {
		case 6:
			playerService.releaseL(controller);
			break;
		case 7:
			playerService.releaseR(controller);
			break;
		default:
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
