package com.mygdx.domain.level;

import java.util.Optional;

import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.constante.CollisionConstante;
import com.mygdx.constante.Constante;
import com.mygdx.domain.Player;
import com.mygdx.domain.common.BodyAble;
import com.mygdx.enumeration.SoundEnum;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.SoundService;
import com.mygdx.service.SpriteService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trolley extends BodyAble implements Initiable {

	private static final float RADIUS = 0.48f;
	protected int x;
	protected int y;
	private boolean move;
	private Player player;
	private PovDirection moveDirection;
	private PovDirection drawDirection;
	private boolean collideWithOtherTrolley;
	private long soundId;

	private Rail currentRail;
	private Rail previousRail;

	@Override
	public void createBody() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set((float) this.x + 0.5f, (float) this.y + 0.5f);
		this.body = world.createBody(bodyDef);
		this.body.setFixedRotation(false);
		MassData data = new MassData();
		data.mass = 10000f;
		this.body.setMassData(data);
		this.body.setUserData(this);
		PolygonShape diamondBody = new PolygonShape();
		Vector2[] vertices = new Vector2[4];
		vertices[0] = new Vector2(-RADIUS, 0);
		vertices[1] = new Vector2(0, RADIUS);
		vertices[2] = new Vector2(RADIUS, 0);
		vertices[3] = new Vector2(0, -RADIUS);
		diamondBody.set(vertices);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = diamondBody;
		fixtureDef.density = 0;
		fixtureDef.restitution = 0f;
		Fixture fixture = body.createFixture(fixtureDef);
		fixture.setFriction(0f);
		fixture.setUserData(this);
		Filter filter = new Filter();
		filter.categoryBits = CollisionConstante.CATEGORY_TROLLEY;
		filter.maskBits = CollisionConstante.GROUP_TROLLEY;
		fixture.setFilterData(filter);
		diamondBody.dispose();
	}

	@Override
	public void dispose() {
		super.dispose();
		SoundService.getInstance().stopSound(SoundEnum.TROLLEY, this.soundId);
	}

	@Override
	public void init(World world, MultiBombermanGame mbGame, Level level) {
		this.mbGame = mbGame;
		this.world = world;
		this.level = level;
		this.move = false;
		this.collideWithOtherTrolley = false;
		this.drawDirection = PovDirection.east;
		this.moveDirection = PovDirection.east;
		this.player = null;
		createBody();
		this.currentRail = this.getCurrentRailUnderTrolley();
		if (this.currentRail != null) {
			this.drawDirection = this.currentRail.getNextDirection(null);
			this.moveDirection = this.drawDirection;
		} else {
			this.drawDirection = PovDirection.east;
		}
	}

	@Override
	public void update() {
		if (collideWithOtherTrolley) {
			this.dispose();
		} else if (move) {
			this.player.trolleyMovePlayer(this.getBodyX(), this.getBodyY(), this.moveDirection);
			switch (this.moveDirection) {
			case center:
				break;
			case east:
				this.body.setLinearVelocity(Constante.TROLLEY_SPEED, 0f);
				break;
			case north:
				this.body.setLinearVelocity(0f, Constante.TROLLEY_SPEED);
				break;
			case northEast:
				break;
			case northWest:
				break;
			case south:
				this.body.setLinearVelocity(0f, -Constante.TROLLEY_SPEED);
				break;
			case southEast:
				break;
			case southWest:
				break;
			case west:
				this.body.setLinearVelocity(-Constante.TROLLEY_SPEED, 0f);
				break;
			default:
				break;
			}
			Rail rail = this.getCurrentRailUnderTrolley();
			if (rail != null && !rail.equals(currentRail)) {
				this.moveDirection = rail.getNextDirection(currentRail);
				if (this.moveDirection == PovDirection.center) {
					this.playerEjectFromTrolley();
					this.moveDirection = rail.getNextDirection(null);
					this.drawDirection = PovDirection.east;
					this.move = false;
				} else {
					this.drawDirection = this.moveDirection;
				}
				this.currentRail = rail;
			}
		} else {
			this.body.setLinearVelocity(0f, 0f);
		}

	}

	@Override
	public void drawIt() {
		switch (this.drawDirection) {
		case center:
			break;
		case east:
		case west:
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.TROLLEY, 0), getPixelX() - 15f,
					getPixelY() - 9f);
			break;
		case north:
		case south:
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.TROLLEY, 1), getPixelX() - 15f,
					getPixelY() - 9f);
			break;
		case northWest:
		case southEast:
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.TROLLEY, 2), getPixelX() - 15f,
					getPixelY() - 9f);
			break;
		case northEast:
		case southWest:
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.TROLLEY, 3), getPixelX() - 15f,
					getPixelY() - 9f);
			break;
		default:
			break;
		}
	}

	public boolean isDestroyed() {
		return false;
	}

	public boolean isMode() {
		return move;
	}

	public void playerTakeTrolley(Player player) {
		if (currentRail != null) {
			this.player = player;
			this.player.enterInTrolley();
			this.move = true;
			this.soundId = SoundService.getInstance().loopSound(SoundEnum.TROLLEY);
		}
	}

	private void playerEjectFromTrolley() {
		this.move = false;
		this.player.exitTrolley();
		this.player = null;
		SoundService.getInstance().stopSound(SoundEnum.TROLLEY, this.soundId);
	}

	public void trolleyCollision() {
		this.collideWithOtherTrolley = true;
	}

	private Rail getCurrentRailUnderTrolley() {
		Optional<Rail> rail = level.getRail().stream()
				.filter(r -> r.getIndex() == (int) this.getBodyX() + ((int) this.getBodyY() * Constante.GRID_SIZE_X))
				.findFirst();
		if (rail.isPresent()) {
			return rail.get();
		}
		return null;
	}
}
