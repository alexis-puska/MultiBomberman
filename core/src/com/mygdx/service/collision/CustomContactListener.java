package com.mygdx.service.collision;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.domain.Player;
import com.mygdx.domain.level.Brick;
import com.mygdx.domain.level.Hole;
import com.mygdx.domain.level.Interrupter;

public class CustomContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {

		// touche une brick
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

		// touche un interrupteur
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

		// touche un trou
		if (contact.getFixtureA().getUserData() != null && contact.getFixtureB() != null) {
			if (contact.getFixtureA().getUserData().getClass() == Hole.class
					&& contact.getFixtureB().getUserData().getClass() == Player.class) {
				Hole b = (Hole) contact.getFixtureA().getUserData();
				b.walkOn();
			} else if (contact.getFixtureB().getUserData().getClass() == Hole.class
					&& contact.getFixtureA().getUserData().getClass() == Player.class) {
				Hole b = (Hole) contact.getFixtureB().getUserData();
				b.walkOn();
			}
		}

		// a definir
	}

	@Override
	public void endContact(Contact contact) {
		// Sortie d'un interrupteur
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

		// Sortie d'un trou
		if (contact.getFixtureA().getUserData() != null && contact.getFixtureB() != null) {
			if (contact.getFixtureA().getUserData().getClass() == Hole.class
					&& contact.getFixtureB().getUserData().getClass() == Player.class) {
				Hole b = (Hole) contact.getFixtureA().getUserData();
				b.walkOff();
			} else if (contact.getFixtureB().getUserData().getClass() == Hole.class
					&& contact.getFixtureA().getUserData().getClass() == Player.class) {
				Hole b = (Hole) contact.getFixtureB().getUserData();
				b.walkOff();
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
