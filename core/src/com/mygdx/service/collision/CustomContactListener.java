package com.mygdx.service.collision;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.constante.CollisionConstante;
import com.mygdx.domain.Player;
import com.mygdx.domain.game.Bombe;
import com.mygdx.domain.game.Fire;
import com.mygdx.domain.level.Hole;
import com.mygdx.domain.level.Interrupter;
import com.mygdx.domain.level.Teleporter;

public class CustomContactListener implements ContactListener {

	private static final float DETECT = 0.49f;

	/************************************************
	 * --- BEGIN CONTACT ---
	 ************************************************/
	@Override
	public void beginContact(Contact contact) {
		beginContactPlayerInterrupter(contact);
		beginContactPlayerHole(contact);
		beginContactPlayerTeleporter(contact);
		beginContactPlayerFire(contact);
		beginContactPlayerBombe(contact);
		beginContactPlayerHitboxBombe(contact);
		beginContactBombeFire(contact);
	}

	private void beginContactPlayerInterrupter(Contact contact) {
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

	private void beginContactPlayerHole(Contact contact) {
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

	private void beginContactPlayerTeleporter(Contact contact) {
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

	private void beginContactPlayerFire(Contact contact) {
		if (contact.getFixtureA().getUserData().getClass() == Fire.class
				&& contact.getFixtureB().getUserData().getClass() == Player.class) {
			contact.setEnabled(false);
			Player p = (Player) contact.getFixtureB().getUserData();
			p.insideFire(true);
		} else if (contact.getFixtureB().getUserData().getClass() == Fire.class
				&& contact.getFixtureA().getUserData().getClass() == Player.class) {
			contact.setEnabled(false);
			Player p = (Player) contact.getFixtureA().getUserData();
			p.insideFire(true);
		}
	}

	private void beginContactBombeFire(Contact contact) {
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

	private void beginContactPlayerBombe(Contact contact) {
		if (contact.getFixtureA().getUserData().getClass() == Bombe.class
				&& contact.getFixtureB().getUserData().getClass() == Player.class) {
			contact.setEnabled(false);
			Player p = (Player) contact.getFixtureB().getUserData();
			Bombe b = (Bombe) contact.getFixtureA().getUserData();
			Vector2 pp = p.getPosition();
			Vector2 bp = b.getPosition();
			if (distance(pp, bp, DETECT)) {
				contact.setEnabled(false);
			} else {
				b.kick(p.getDirection());
			}
		} else if (contact.getFixtureB().getUserData().getClass() == Bombe.class
				&& contact.getFixtureA().getUserData().getClass() == Player.class) {
			Player p = (Player) contact.getFixtureA().getUserData();
			Bombe b = (Bombe) contact.getFixtureB().getUserData();
			Vector2 pp = p.getPosition();
			Vector2 bp = b.getPosition();
			if (distance(pp, bp, DETECT)) {
				contact.setEnabled(false);
			} else {
				b.kick(p.getDirection());
			}
		}
	}

	private void beginContactPlayerHitboxBombe(Contact contact) {
		if (contact.getFixtureA().getUserData().getClass() == Bombe.class
				&& contact.getFixtureB().getUserData().getClass() == Player.class
				&& contact.getFixtureA().getFilterData().categoryBits == CollisionConstante.CATEGORY_BOMBE
				&& contact.getFixtureB().getFilterData().categoryBits == CollisionConstante.CATEGORY_PLAYER_HITBOX) {
			contact.setEnabled(false);
			Player p = (Player) contact.getFixtureB().getUserData();
			p.insideBombe(true);
		} else if (contact.getFixtureB().getUserData().getClass() == Bombe.class
				&& contact.getFixtureA().getUserData().getClass() == Player.class
				&& contact.getFixtureB().getFilterData().categoryBits == CollisionConstante.CATEGORY_BOMBE
				&& contact.getFixtureA().getFilterData().categoryBits == CollisionConstante.CATEGORY_PLAYER_HITBOX) {
			contact.setEnabled(false);
			Player p = (Player) contact.getFixtureA().getUserData();
			p.insideBombe(true);
		}
	}

	/************************************************
	 * --- END CONTACT ---
	 ************************************************/
	@Override
	public void endContact(Contact contact) {
		endContactPlayerInterrupter(contact);
		endContactPlayerHole(contact);
		endContactPlayerTeleporter(contact);
		endContactPlayerFire(contact);
		endContactPlayerHitboxBombe(contact);
	}

	private void endContactPlayerInterrupter(Contact contact) {
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

	private void endContactPlayerHole(Contact contact) {
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

	private void endContactPlayerTeleporter(Contact contact) {
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

	private void endContactPlayerFire(Contact contact) {
		// touch fire
		if (contact.getFixtureA().getUserData().getClass() == Fire.class
				&& contact.getFixtureB().getUserData().getClass() == Player.class) {
			Player p = (Player) contact.getFixtureB().getUserData();
			p.insideFire(false);
		} else if (contact.getFixtureB().getUserData().getClass() == Fire.class
				&& contact.getFixtureA().getUserData().getClass() == Player.class) {
			Player p = (Player) contact.getFixtureA().getUserData();
			p.insideFire(false);
		}
	}

	private void endContactPlayerHitboxBombe(Contact contact) {
		if (contact.getFixtureA().getUserData().getClass() == Bombe.class
				&& contact.getFixtureB().getUserData().getClass() == Player.class
				&& contact.getFixtureA().getFilterData().categoryBits == CollisionConstante.CATEGORY_BOMBE
				&& contact.getFixtureB().getFilterData().categoryBits == CollisionConstante.CATEGORY_PLAYER_HITBOX) {
			contact.setEnabled(false);
			Player p = (Player) contact.getFixtureB().getUserData();
			p.insideBombe(false);
		} else if (contact.getFixtureB().getUserData().getClass() == Bombe.class
				&& contact.getFixtureA().getUserData().getClass() == Player.class
				&& contact.getFixtureB().getFilterData().categoryBits == CollisionConstante.CATEGORY_BOMBE
				&& contact.getFixtureA().getFilterData().categoryBits == CollisionConstante.CATEGORY_PLAYER_HITBOX) {
			contact.setEnabled(false);
			Player p = (Player) contact.getFixtureA().getUserData();
			p.insideBombe(false);
		}
	}

	/************************************************
	 * --- PRE SOLVE ---
	 ************************************************/
	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		preSolvePlayerBombe(contact);
		preSolvePlayerFire(contact);
	}

	private void preSolvePlayerBombe(Contact contact) {
		Player player = null;
		Bombe bombe = null;
		if (contact.getFixtureA().getUserData().getClass() == Bombe.class
				&& contact.getFixtureB().getUserData().getClass() == Player.class
				&& contact.getFixtureA().getFilterData().categoryBits == CollisionConstante.CATEGORY_BOMBE
				&& contact.getFixtureB().getFilterData().categoryBits == CollisionConstante.CATEGORY_PLAYER) {
			player = (Player) contact.getFixtureB().getUserData();
			bombe = (Bombe) contact.getFixtureA().getUserData();
		} else if (contact.getFixtureB().getUserData().getClass() == Bombe.class
				&& contact.getFixtureA().getUserData().getClass() == Player.class
				&& contact.getFixtureB().getFilterData().categoryBits == CollisionConstante.CATEGORY_BOMBE
				&& contact.getFixtureA().getFilterData().categoryBits == CollisionConstante.CATEGORY_PLAYER) {
			player = (Player) contact.getFixtureA().getUserData();
			bombe = (Bombe) contact.getFixtureB().getUserData();
		}
		if (player != null && bombe != null) {
			Vector2 pp = player.getPosition();
			Vector2 bp = bombe.getPosition();
			if (distance(pp, bp, DETECT)) {
				contact.setEnabled(false);
			}
		}
	}

	private void preSolvePlayerFire(Contact contact) {
		if ((contact.getFixtureA().getUserData().getClass() == Fire.class
				&& contact.getFixtureB().getUserData().getClass() == Player.class)
				|| (contact.getFixtureB().getUserData().getClass() == Fire.class
						&& contact.getFixtureA().getUserData().getClass() == Player.class)) {
			contact.setEnabled(false);
		}
	}

	/************************************************
	 * --- POST SOLVE ---
	 ************************************************/
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// a definir
	}

	private boolean distance(Vector2 p1, Vector2 p2, float distance) {
		double d = Math.sqrt(Math.abs(p1.x - p2.x)) + Math.sqrt(Math.abs(p1.y - p2.y));
		double dd = Math.sqrt(distance);
//		Gdx.app.log("calc distance",
//				"p1 : " + p1.x + " " + p1.y + ",p2 : " + p2.x + " " + p2 + ",distance : " + distance + " ,x2+y2 : "
//						+ d + " ,sqrt : " + distance * distance + " " + (d < dd));
		return d < dd;

	}
}
