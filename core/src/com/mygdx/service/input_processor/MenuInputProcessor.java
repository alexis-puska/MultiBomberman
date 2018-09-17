package com.mygdx.service.input_processor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.PovDirection;
import com.mygdx.constante.Constante;
import com.mygdx.service.PlayerService;

public class MenuInputProcessor implements InputProcessor {
	
	private static final String LOG_NAME = "MenuInputProcessor.class";

	private PlayerService playerService;

	private boolean fullscreen;
	private boolean ctrl;

	public MenuInputProcessor(PlayerService playerService) {
		this.playerService = playerService;
	}

	/***************************
	 * --- KEYBOARD PART ---
	 ***************************/
	@Override
	public boolean keyDown(int keycode) {

		switch (keycode) {
		case Keys.ENTER:
			this.playerService.pressStart();
			break;
		case Keys.SPACE:
			this.playerService.pressValide();
			break;
		case Keys.SHIFT_RIGHT:
			this.playerService.pressSelect();
			break;
		case Keys.UP:
			this.playerService.move(PovDirection.north);
			break;
		case Keys.DOWN:
			this.playerService.move(PovDirection.south);
			break;
		case Keys.LEFT:
			this.playerService.move(PovDirection.west);
			break;
		case Keys.RIGHT:
			this.playerService.move(PovDirection.east);
			break;
		case Keys.X:
			this.playerService.dropBombe();
			break;
		case Keys.W:
			this.playerService.throwBombe();
			break;
		case Keys.A:
			this.playerService.speedUp();
			break;
		case Keys.Z:
			this.playerService.speedUp();
			break;
		case Keys.Q:
			this.playerService.speedUp();
			break;
		case Keys.S:
			this.playerService.speedUp();
			break;
		case Keys.CONTROL_LEFT:
		case Keys.CONTROL_RIGHT:
			ctrl = true;
			break;
		case Keys.F:
			if (ctrl) {
				toogleScreen();
			}
			break;
		default:
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
		case Keys.CONTROL_LEFT:
		case Keys.CONTROL_RIGHT:
			ctrl = false;
			break;
		case Keys.A:
			this.playerService.speedDown();
			break;
		case Keys.Z:
			this.playerService.speedDown();
			break;
		case Keys.Q:
			this.playerService.speedDown();
			break;
		case Keys.S:
			this.playerService.speedDown();
			break;
		default:
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	/***************************
	 * --- TOUCHSCREEN PART ---
	 ***************************/
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	private void toogleScreen() {
		if (!fullscreen) {
			Gdx.app.debug(LOG_NAME, "toogle screen : "+Gdx.graphics.getWidth() + " " + Gdx.graphics.getHeight());
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
			fullscreen = true;
		} else {
			Gdx.graphics.setWindowedMode(Constante.SCREEN_SIZE_X, Constante.SCREEN_SIZE_Y);
			fullscreen = false;
		}
		ctrl = false;
	}
}
