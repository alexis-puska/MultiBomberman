package com.mygdx.service.collision;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.domain.Player;
import com.mygdx.domain.level.Brick;
import com.mygdx.domain.level.Interrupter;

public class CustomContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		if (contact.getFixtureA().getUserData() != null && contact.getFixtureB() != null) {
			if (contact.getFixtureA().getUserData().getClass() == Brick.class
					&& contact.getFixtureB().getUserData().getClass() == Player.class) {
				Brick b = (Brick) contact.getFixtureA().getUserData();
				b.burn();
			} else if (contact.getFixtureB().getUserData().getClass() == Brick.class
					&& contact.getFixtureA().getUserData().getClass() == Player.class) {
				Brick b = (Brick) contact.getFixtureB().getUserData();
				b.burn();
			}
		}

		if (contact.getFixtureA().getUserData() != null && contact.getFixtureB() != null) {
			if (contact.getFixtureA().getUserData().getClass() == Interrupter.class
					&& contact.getFixtureB().getUserData().getClass() == Player.class) {
				Interrupter b = (Interrupter) contact.getFixtureA().getUserData();
				b.playerTouchBegin();
			} else if (contact.getFixtureB().getUserData().getClass() == Interrupter.class
					&& contact.getFixtureA().getUserData().getClass() == Player.class) {
				Interrupter b = (Interrupter) contact.getFixtureB().getUserData();
				b.playerTouchBegin();
			}
		}

		// a definir
	}

	@Override
	public void endContact(Contact contact) {
		if (contact.getFixtureA().getUserData() != null && contact.getFixtureB() != null) {
			if (contact.getFixtureA().getUserData().getClass() == Interrupter.class
					&& contact.getFixtureB().getUserData().getClass() == Player.class) {
				Interrupter b = (Interrupter) contact.getFixtureA().getUserData();
				b.playerTouchEnd();
			} else if (contact.getFixtureB().getUserData().getClass() == Interrupter.class
					&& contact.getFixtureA().getUserData().getClass() == Player.class) {
				Interrupter b = (Interrupter) contact.getFixtureB().getUserData();
				b.playerTouchEnd();
			}
		}

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// a definir
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// a definir
	}
}
