package com.mygdx.domain.level;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.constante.CollisionConstante;
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
public class Teleporter extends BodyAble implements Initiable {

	private static final int NB_FRAME_TELEPORTER = 3;

	protected int x;
	protected int y;

	private boolean animate;
	private int frameCounter = 0;
	private int offsetSprite = 0;
	private int offsetSpriteAnimation = 0;
	private int nbFrameForAnimation = 7;
	private boolean playSound;

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
		filter.categoryBits = CollisionConstante.CATEGORY_TELEPORTER;
		filter.maskBits = CollisionConstante.CATEGORY_PLAYER_HITBOX;
		fixture.setFilterData(filter);
	}

	@Override
	public void init(World world, MultiBombermanGame mbGame, Level level) {
		this.mbGame = mbGame;
		this.world = world;
		this.level = level;
		createBody();
	}

	public void animate(boolean playSound) {
		this.animate = true;
		this.playSound = playSound;
		this.frameCounter = 0;
		this.offsetSprite = 0;
		this.offsetSpriteAnimation = 0;
		this.nbFrameForAnimation = 7;
	}

	public void update() {
		if (animate) {
			if (frameCounter > NB_FRAME_TELEPORTER) {
				frameCounter = 0;
				offsetSprite++;
				if (offsetSprite >= nbFrameForAnimation) {
					offsetSprite = 0;
				}
			}
			frameCounter++;
			this.offsetSpriteAnimation = 0;
			switch (offsetSprite) {
			case 0:
				if (playSound) {
					SoundService.getInstance().playSound(SoundEnum.TELEPORTER_OPEN);
				}
				offsetSpriteAnimation = 0;
				break;
			case 1:
				offsetSpriteAnimation = 1;
				break;
			case 2:
				offsetSpriteAnimation = 2;
				break;
			case 3:
				if (playSound) {
					SoundService.getInstance().playSound(SoundEnum.TELEPORTER_CLOSE);
				}
				offsetSpriteAnimation = 3;
				break;
			case 4:
				offsetSpriteAnimation = 2;
				break;
			case 5:
				offsetSpriteAnimation = 1;
				break;
			case 6:
				offsetSpriteAnimation = 0;
				offsetSprite = 0;
				frameCounter = 0;
				animate = false;
				break;
			}
		}
	}

	@Override
	public void drawIt() {
		mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.TELEPORTER, offsetSpriteAnimation),
				this.x * 18, this.y * 16);
	}
}
