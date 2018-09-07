package com.mygdx.service.input_processor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.constante.Constante;

public class MenuInputProcessor implements InputProcessor {

	private MenuListener menuListener;

	private boolean fullscreen;
	private boolean ctrl;

	public void changeMenuListeners(MenuListener listener) {
		this.menuListener = listener;
	}

	/***************************
	 * --- KEYBOARD PART ---
	 ***************************/
	@Override
	public boolean keyDown(int keycode) {

		if (menuListener != null) {
			switch (keycode) {
			case Keys.ENTER:
				this.menuListener.pressStart();
				break;
			case Keys.SPACE:
				this.menuListener.pressValide();
				break;
			case Keys.SHIFT_RIGHT:
				this.menuListener.pressSelect();
				break;
			case Keys.UP:
				this.menuListener.pressUp();
				break;
			case Keys.DOWN:
				this.menuListener.pressDown();
				break;
			case Keys.LEFT:
				this.menuListener.pressLeft();
				break;
			case Keys.RIGHT:
				this.menuListener.pressRight();
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
			Gdx.app.log("SCREEN SIZE :", Gdx.graphics.getWidth() + " " + Gdx.graphics.getHeight());
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
			fullscreen = true;
		} else {
			Gdx.graphics.setWindowedMode(Constante.SCREEN_SIZE_X, Constante.SCREEN_SIZE_Y);
			fullscreen = false;
		}
		ctrl = false;
	}
}
