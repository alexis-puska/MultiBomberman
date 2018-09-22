package com.mygdx.service.input_processor;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.enumeration.ControllerButtonEnum;
import com.mygdx.service.PlayerService;

public class ControllerAdapter implements ControllerListener {

	private PlayerService playerService;
	private final ControllerMapper controllerMapper;

	public ControllerAdapter(PlayerService playerService) {
		this.playerService = playerService;
		this.controllerMapper = new ControllerMapper();
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
		ControllerButtonEnum controllerButtonEnum = controllerMapper.getButton(controller, buttonCode);
		if (controllerButtonEnum != null) {
			switch (controllerButtonEnum) {
			case A:
				playerService.pressA(controller);
				break;
			case B:
				playerService.pressB(controller);
				break;
			case X:
				playerService.pressX(controller);
				break;
			case Y:
				playerService.pressY(controller);
				break;
			case L1:
				playerService.pressL(controller);
				break;
			case R1:
				playerService.pressR(controller);
				break;
			case SELECT:
				playerService.pressSelect(controller);
				break;
			case START:
				playerService.pressStart(controller);
				break;
			case L2:
			case R2:
			case NONE:
			default:
			}
		}
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		ControllerButtonEnum controllerButtonEnum = controllerMapper.getButton(controller, buttonCode);
		if (controllerButtonEnum != null) {
			switch (controllerButtonEnum) {
			case L1:
				playerService.releaseL(controller);
				break;
			case R1:
				playerService.releaseR(controller);
				break;
			case A:
			case B:
			case X:
			case Y:
			case L2:
			case R2:
			case SELECT:
			case START:
			case NONE:
			default:
			}
		}
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		PovDirection dir = controllerMapper.getDirection(controller, axisCode, value);
		if (dir != null) {
			playerService.move(controller, dir);
		}
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
