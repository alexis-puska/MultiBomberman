package com.mygdx.service.collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.domain.Player;
import com.mygdx.domain.game.Bombe;
import com.mygdx.domain.game.Fire;
import com.mygdx.domain.level.Hole;
import com.mygdx.domain.level.Interrupter;
import com.mygdx.domain.level.Teleporter;

public class CustomContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {

		// touche une brick
//		if (contact.getFixtureA().getUserData() != null && contact.getFixtureB() != null) {
//			if (contact.getFixtureA().getUserData().getClass() == Brick.class
//					&& contact.getFixtureB().getUserData().getClass() == Player.class) {
//				Brick b = (Brick) contact.getFixtureA().getUserData();
//				b.action();
//			} else if (contact.getFixtureB().getUserData().getClass() == Brick.class
//					&& contact.getFixtureA().getUserData().getClass() == Player.class) {
//				Brick b = (Brick) contact.getFixtureB().getUserData();
//				b.action();
//			}
//		}

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

		// touch teleporter
		if (contact.getFixtureA().getUserData() != null && contact.getFixtureB() != null) {
			if (contact.getFixtureA().getUserData().getClass() == Teleporter.class
					&& contact.getFixtureB().getUserData().getClass() == Player.class) {
				Teleporter t = (Teleporter) contact.getFixtureA().getUserData();
				Player p = (Player) contact.getFixtureB().getUserData();
				p.teleporte(t);
			} else if (contact.getFixtureB().getUserData().getClass() == Teleporter.class
					&& contact.getFixtureA().getUserData().getClass() == Player.class) {
				Teleporter t = (Teleporter) contact.getFixtureB().getUserData();
				Player p = (Player) contact.getFixtureA().getUserData();
				p.teleporte(t);
			}
		}

		// touch fire
		if (contact.getFixtureA().getUserData() != null && contact.getFixtureB() != null) {
			if (contact.getFixtureA().getUserData().getClass() == Fire.class
					&& contact.getFixtureB().getUserData().getClass() == Player.class) {
				contact.setEnabled(false);
				Player p = (Player) contact.getFixtureB().getUserData();
				p.fireIn();
			} else if (contact.getFixtureB().getUserData().getClass() == Fire.class
					&& contact.getFixtureA().getUserData().getClass() == Player.class) {
				contact.setEnabled(false);
				Player p = (Player) contact.getFixtureA().getUserData();
				p.fireIn();
			}
		}

		// a definir

		Gdx.app.log("collision",
				contact.getFixtureA().getUserData().getClass() + " " + contact.getFixtureB().getUserData().getClass());

		if (contact.getFixtureA().getUserData() != null && contact.getFixtureB() != null) {
			if (contact.getFixtureA().getUserData().getClass() == Fire.class
					&& contact.getFixtureB().getUserData().getClass() == Bombe.class) {
				contact.setEnabled(false);
				Bombe b = (Bombe) contact.getFixtureB().getUserData();
				b.inFire();
			} else if (contact.getFixtureB().getUserData().getClass() == Fire.class
					&& contact.getFixtureA().getUserData().getClass() == Bombe.class) {
				contact.setEnabled(false);
				Bombe b = (Bombe) contact.getFixtureA().getUserData();
				b.inFire();
			}
		}

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

		// sortie teleporter
		if (contact.getFixtureA().getUserData() != null && contact.getFixtureB() != null) {
			if (contact.getFixtureA().getUserData().getClass() == Teleporter.class
					&& contact.getFixtureB().getUserData().getClass() == Player.class) {
				Teleporter t = (Teleporter) contact.getFixtureA().getUserData();
				Player p = (Player) contact.getFixtureB().getUserData();
				p.teleporteEnd(t);
			} else if (contact.getFixtureB().getUserData().getClass() == Teleporter.class
					&& contact.getFixtureA().getUserData().getClass() == Player.class) {
				Teleporter t = (Teleporter) contact.getFixtureB().getUserData();
				Player p = (Player) contact.getFixtureA().getUserData();
				p.teleporteEnd(t);
			}
		}

		// touch fire
		if (contact.getFixtureA().getUserData() != null && contact.getFixtureB() != null) {
			if (contact.getFixtureA().getUserData().getClass() == Fire.class
					&& contact.getFixtureB().getUserData().getClass() == Player.class) {
				Player p = (Player) contact.getFixtureB().getUserData();
				p.fireOut();
			} else if (contact.getFixtureB().getUserData().getClass() == Fire.class
					&& contact.getFixtureA().getUserData().getClass() == Player.class) {
				Player p = (Player) contact.getFixtureA().getUserData();
				p.fireOut();
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
