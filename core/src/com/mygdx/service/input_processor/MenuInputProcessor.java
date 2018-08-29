package com.mygdx.service.input_processor;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

public class MenuInputProcessor implements InputProcessor {

	private boolean consumeEvent;

	private boolean up;
	private boolean down;
	private boolean left;
	private boolean right;
	private boolean valide;
	private boolean previous;
	private boolean next;

	/***************************
	 * --- KEYBOARD PART ---
	 ***************************/
	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Keys.ENTER:
			next = true;
			break;
		case Keys.SPACE:
			valide = true;
			break;
		case Keys.SHIFT_RIGHT:
			previous = true;
			break;
		case Keys.UP:
			up = true;
			break;
		case Keys.DOWN:
			down = true;
			break;
		case Keys.LEFT:
			left = true;
			break;
		case Keys.RIGHT:
			right = true;
			break;
		default:
		}
		consumeEvent = false;
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
		case Keys.ENTER:
			next = false;
			break;
		case Keys.SPACE:
			valide = false;
			break;
		case Keys.SHIFT_RIGHT:
			previous = false;
			break;
		case Keys.UP:
			up = false;
			break;
		case Keys.DOWN:
			down = false;
			break;
		case Keys.LEFT:
			left = false;
			break;
		case Keys.RIGHT:
			right = false;
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

	/***************************
	 * --- COMMON PART ---
	 ***************************/
	public boolean isConsumeEvent() {
		return consumeEvent;
	}

	public void setConsumeEvent(boolean consumeEvent) {
		this.consumeEvent = consumeEvent;
	}

	public boolean pressUp() {
		if (consumeEvent) {
			return false;
		} else if (up) {
			consumeEvent = true;
		}
		return up;
	}

	public boolean pressDown() {
		if (consumeEvent) {
			return false;
		} else if (down) {
			consumeEvent = true;
		}
		return down;
	}

	public boolean pressLeft() {
		if (consumeEvent) {
			return false;
		} else if (left) {
			consumeEvent = true;
		}
		return left;
	}

	public boolean pressRight() {
		if (consumeEvent) {
			return false;
		} else if (right) {
			consumeEvent = true;
		}
		return right;
	}

	public boolean pressValide() {
		if (consumeEvent) {
			return false;
		} else if (valide) {
			consumeEvent = true;
		}
		return valide;
	}

	public boolean pressPrevious() {
		if (consumeEvent) {
			return false;
		} else if (previous) {
			consumeEvent = true;
		}
		return previous;
	}

	public boolean pressNext() {
		if (consumeEvent) {
			return false;
		} else if (next) {
			consumeEvent = true;
		}
		return next;
	}

	public void reset() {
		consumeEvent = false;
		up = false;
		down = false;
		left = false;
		right = false;
		valide = false;
		previous = false;
		next = false;
	}
}
