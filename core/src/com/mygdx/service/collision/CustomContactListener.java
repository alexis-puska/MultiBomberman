package com.mygdx.service.collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class CustomContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		//Gdx.app.log("contact1", "");
		// a definir
	}

	@Override
	public void endContact(Contact contact) {
		//Gdx.app.log("contact2", "");
		// a definir
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		//Gdx.app.log("contact3", "");
		// a definir
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		//Gdx.app.log("contact4", "");
		// a definir
	}

}
