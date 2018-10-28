package com.mygdx.domain.level;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.constante.CollisionConstante;
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
public class Wall extends BodyAble {

	protected int x;
	protected int y;
	private boolean draw;
	private boolean customSkin;
	private SpriteEnum animation;
	private int index;

	private SpriteEnum defaultAnimation;
	private int defaultTexture;

	public void init(World world, MultiBombermanGame mbGame, SpriteEnum defaultAnimation, int defaultTexture) {
		this.world = world;
		this.defaultAnimation = defaultAnimation;
		this.defaultTexture = defaultTexture;
		this.init(mbGame);
		createBody();
	}

	@Override
	public void drawIt() {
		if (draw) {
			if (customSkin) {
				mbGame.getBatch().draw(SpriteService.getInstance().getSprite(this.animation, this.index), this.x * 18,
						this.y * 16);
			} else {
				mbGame.getBatch().draw(
						SpriteService.getInstance().getSprite(this.defaultAnimation, this.defaultTexture), this.x * 18,
						this.y * 16);
			}
		}
	}

	@Override
	public void createBody() {
		BodyDef groundBodyDef = new BodyDef();
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(0.5f, 0.5f);
		groundBodyDef.position.set(new Vector2((float) this.x + 0.5f, (float) this.y + 0.5f));
		this.body = world.createBody(groundBodyDef);
		Fixture fixture = body.createFixture(groundBox, 0.0f);
		fixture.setFriction(0f);
		fixture.setUserData(this);
		Filter filter = new Filter();
		filter.categoryBits = CollisionConstante.CATEGORY_WALL;
		filter.maskBits = 0x0000000000000000;
		filter.maskBits = CollisionConstante.CATEGORY_PLAYER;
		fixture.setFilterData(filter);
	}

}
