package com.mygdx.domain.level;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.constante.CollisionConstante;
import com.mygdx.constante.Constante;
import com.mygdx.domain.common.BodyAble;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.SpriteService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Interrupter extends BodyAble implements Initiable {

	protected int x;
	protected int y;

	private int nbPlayer;

	@Override
	public void init(World world, MultiBombermanGame mbGame, Level level) {
		this.drawSprite = SpriteEnum.BUTTON;
		this.drawIndex = 0;
		this.mbGame = mbGame;
		this.world = world;
		this.level = level;
		this.drawSprite = SpriteEnum.TELEPORTER;
		createBody();
	}

	@Override
	public void createBody() {
		BodyDef groundBodyDef = new BodyDef();
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(0.4f, 0.4f);
		groundBodyDef.position.set(new Vector2((float) this.x + 0.5f, (float) this.y + 0.5f));
		this.body = world.createBody(groundBodyDef);
		Fixture fixture = body.createFixture(groundBox, 0.0f);
		fixture.setFriction(0f);
		fixture.setUserData(this);
		Filter filter = new Filter();
		filter.categoryBits = CollisionConstante.CATEGORY_BUTTONS;
		filter.maskBits = CollisionConstante.CATEGORY_PLAYER_HITBOX;
		fixture.setFilterData(filter);
	}

	public void playerTouchBegin() {
		if (nbPlayer == 0) {
			this.level.getRail().stream().forEach(Rail::switchRail);
		}
		nbPlayer++;
	}

	public void playerTouchEnd() {
		nbPlayer--;
	}

	@Override
	public void drawIt() {
		this.drawSprite = SpriteEnum.BUTTON;
		if (nbPlayer > 0) {
			this.drawIndex = 1;
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.BUTTON, 1),
					this.x * Constante.GRID_PIXELS_SIZE_X, this.y * Constante.GRID_PIXELS_SIZE_Y);
			this.drawIndex = 1;
		} else {

			this.drawIndex = 0;
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.BUTTON, 0),
					this.x * Constante.GRID_PIXELS_SIZE_X, this.y * Constante.GRID_PIXELS_SIZE_Y);
			this.drawIndex = 0;
		}
	}

	@Override
	public SpriteEnum getDrawSprite() {
		return drawSprite;
	}

	@Override
	public int getDrawIndex() {
		return drawIndex;
	}
}
