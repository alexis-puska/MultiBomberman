package com.mygdx.service.collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.domain.Player;
import com.mygdx.domain.level.Brick;

public class CustomContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		Gdx.app.log("contact1", "");
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
		// a definir
	}

	@Override
	public void endContact(Contact contact) {
		Gdx.app.log("contact2", "");
		// a definir
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
